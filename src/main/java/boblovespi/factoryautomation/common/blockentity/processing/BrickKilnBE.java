package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.block.processing.BrickKiln;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.menu.BrickKilnMenu;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.recipe.KilnRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.HeatManager;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import boblovespi.factoryautomation.common.util.jade.IJadeViewable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class BrickKilnBE extends FABE implements ITickable, IJadeViewable, IMultiblockBE, IMenuProviderProvider
{
	private final RecipeManager<KilnRecipe> recipeManager;
	private final HeatManager heatManager;
	private final ItemStackHandler inv;
	private boolean breaking;

	public BrickKilnBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.BRICK_KILN_TYPE.get(), pos, state);
		recipeManager = new RecipeManager<>("recipe", this::isValid, this::findMatchingRecipe, this::getRecipe);
		heatManager = new HeatManager("heat", 1000, 1000);
		inv = new ItemStackHandler(2)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
				recipeManager.updateRecipe();
			}
		};
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.save(tag);
		heatManager.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		recipeManager.load(tag);
		heatManager.load(tag);
		inv.deserializeNBT(registries, tag.getCompound("inv"));
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{

	}

	@Override
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
		if (getBlockState().getValue(BrickKiln.MULTIBLOCK_COMPLETE))
		{
			breaking = true;
			Multiblocks.BRICK_KILN.destroy(level, worldPosition, getBlockState().getValue(BrickKiln.FACING));
		}
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		recipeManager.onLoad();
	}

	@Override
	public void tick()
	{
		if (recipeManager.hasRecipe())
		{
			recipeManager.progress();
			if (recipeManager.isComplete())
			{
				var result = recipeManager.getCompleted();
				var assemble = result.value().assemble(getInput(), level.registryAccess());
				inv.insertItem(1, assemble, false);
				inv.extractItem(0, 1, false);
				recipeManager.complete();
				setChangedAndUpdateClient();
			}
			else
				setChanged();
		}
	}

	private KilnRecipe.Input getInput()
	{
		return new KilnRecipe.Input(inv.getStackInSlot(0), heatManager.getTemperature());
	}

	@Override
	public List<ItemStack> makeViewStacks()
	{
		return List.of();
	}

	private boolean fits(KilnRecipe recipe)
	{
		return inv.insertItem(1, recipe.getResultItem(level.registryAccess()), true).isEmpty();
	}

	private boolean isValid(RecipeHolder<KilnRecipe> r)
	{
		return r.value().matches(getInput(), level) && fits(r.value());
	}

	@Nullable
	private RecipeHolder<KilnRecipe> findMatchingRecipe()
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.KILN_TYPE.get(), getInput(), level).filter(r -> fits(r.value())).orElse(null);
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation k)
	{
		return level.getRecipeManager().byKey(k);
	}

	@Override
	public void onMultiblockBuilt()
	{

	}

	@Override
	public void onMultiblockDestroyed()
	{
		if (!breaking)
			level.setBlock(worldPosition, getBlockState().setValue(BrickKiln.MULTIBLOCK_COMPLETE, false), 2);
	}

	@Override
	public MenuProvider getMenuProvider()
	{
		return new SimpleMenuProvider((i, v, p) -> new BrickKilnMenu(i, v, inv, new Data(), ContainerLevelAccess.create(level, worldPosition)),
				Component.translatable("gui.brick_kiln.name"));
	}

	private class Data implements ContainerData
	{
		@Override
		public int get(int index)
		{
			return index == 1 ? Float.floatToIntBits(heatManager.getTemperature()) : Float.floatToIntBits(recipeManager.getProgressRatio());
		}

		@Override
		public void set(int index, int value)
		{

		}

		@Override
		public int getCount()
		{
			return 2;
		}
	}
}
