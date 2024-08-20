package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.client.gui.WorkbenchMenu;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.Workbench;
import boblovespi.factoryautomation.common.recipe.WorkbenchRecipe;
import boblovespi.factoryautomation.common.recipe.WorkbenchRecipeInput;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Optional;

public abstract class WorkbenchBE extends FABE implements IMenuProviderProvider
{
	private final RecipeManager<WorkbenchRecipe> recipeManager;
	private final ItemStackHandler inv;
	private final int size;
	private final int toolIndex;
	private final int partIndex;
	private final int craftIndex;
	private boolean isUpdatingChanges = false;

	public WorkbenchBE(BlockEntityType<?> type, BlockPos pPos, BlockState pBlockState, int size)
	{
		super(type, pPos, pBlockState);
		this.size = size;
		recipeManager = new RecipeManager<>("recipeManager", this::isValid, this::findRecipe, this::getRecipe);
		inv = new ItemStackHandler(size * size + size * 2 + 1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				if (level.isClientSide)
					return;
				if (!isUpdatingChanges && slot == 0 && getStackInSlot(0).isEmpty() && recipeManager.hasRecipe())
				{
					var recipe = recipeManager.complete();
					isUpdatingChanges = true;
					for (int i = 0; i < size * size; i++)
						extractItem(craftIndex + i, 1, false);
					var parts = recipe.value().getParts();
					for (var part : parts.entrySet())
					{
						for (int i = 0; i < size; i++)
						{
							var stack = getStackInSlot(partIndex + i);
							var data = stack.getItemHolder().getData(Workbench.PART_DATA);
							if (data != null && data.name().equals(part.getKey()) && data.tier() >= part.getValue().tier() && stack.getCount() >= part.getValue().usage())
							{
								extractItem(partIndex + i, part.getValue().usage(), false);
								break;
							}
						}
					}
					var tools = recipe.value().getTools();
					for (var tool : tools.entrySet())
					{
						for (int i = 0; i < size; i++)
						{
							var stack = getStackInSlot(toolIndex + i);
							var data = stack.getItemHolder().getData(Workbench.TOOL_DATA);
							if (data != null && data.name().equals(tool.getKey()) && data.tier() >= tool.getValue().tier() &&
								stack.getMaxDamage() - stack.getDamageValue() >= tool.getValue().usage())
							{
								stack.hurtAndBreak(tool.getValue().usage(), (ServerLevel) level, null, a -> {});
								break;
							}
						}
					}
					isUpdatingChanges = false;
				}
				if (!isUpdatingChanges)
				{
					recipeManager.updateRecipe();
					if (recipeManager.hasRecipe())
					{
						isUpdatingChanges = true;
						setStackInSlot(0, recipeManager.complete().value().assemble(getInput(), level.registryAccess()));
						isUpdatingChanges = false;
					}
				}
				setChangedAndUpdateClient();
			}
		};
		toolIndex = 1;
		partIndex = toolIndex + size;
		craftIndex = partIndex + size;
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation location)
	{
		return level.getRecipeManager().byKey(location);
	}

	@Nullable
	private RecipeHolder<WorkbenchRecipe> findRecipe()
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.WORKBENCH_RECIPE_TYPE.get(), getInput(), level).orElse(null);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		recipeManager.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		recipeManager.load(tag);
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
	}

	@Override
	public void onLoad()
	{
		recipeManager.onLoad();
	}

	@Override
	public void onDestroy()
	{

	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new WorkbenchMenu(i, v, inv, null, ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.stone_crucible.name"));
	}

	private boolean isValid(RecipeHolder<WorkbenchRecipe> r)
	{
		return r.value().matches(getInput(), level);
	}

	private WorkbenchRecipeInput getInput()
	{
		var tools = new ArrayList<ItemStack>(size);
		for (int i = 0; i < size; i++)
		{
			if (!inv.getStackInSlot(toolIndex + i).isEmpty())
				tools.add(inv.getStackInSlot(toolIndex + i));
		}
		var parts = new ArrayList<ItemStack>(size);
		for (int i = 0; i < size; i++)
		{
			if (!inv.getStackInSlot(partIndex + i).isEmpty())
				parts.add(inv.getStackInSlot(partIndex + i));
		}
		var craft = new ArrayList<ItemStack>(size * size);
		int mWidth = 0;
		int mHeight = 0;
		for (int i = 0; i < size * size; i++)
		{
			if (!inv.getStackInSlot(craftIndex + i).isEmpty())
			{
				mWidth = Math.max(mWidth, i % size + 1);
				mHeight = Math.max(mHeight, i / size + 1);
			}
		}
		for (int i = 0; i < mWidth * mHeight; i++)
			craft.add(inv.getStackInSlot(craftIndex + i));
		return new WorkbenchRecipeInput(mWidth, mHeight, craft, parts, tools);
	}

	public static class Stone extends WorkbenchBE
	{
		public Stone(BlockPos pPos, BlockState pBlockState)
		{
			super(FABETypes.STONE_WORKBENCH_TYPE.get(), pPos, pBlockState, 3);
		}
	}
}
