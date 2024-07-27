package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ChoppingBlockBE extends FABE
{
	private final ItemStackHandler inv;
	private final RecipeManager<ChoppingBlockRecipe> rm;

	public ChoppingBlockBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.CHOPPING_BLOCK_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(1);
		rm = new RecipeManager<>("recipeManager",
				r -> r.value().matches(new SingleRecipeInput(inv.getStackInSlot(0)), level),
				() -> level.getRecipeManager().getRecipeFor(RecipeThings.CHOPPING_BLOCK_TYPE.get(), new SingleRecipeInput(inv.getStackInSlot(0)), level).orElse(null),
				k -> level.getRecipeManager().byKey(k));
	}

	public void leftClick(LivingEntity user, ItemStack tool)
	{
		if (rm.hasRecipe())
		{
			if (tool.is(ItemTags.AXES))
			{
				tool.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
				rm.progress();
				if (rm.isComplete())
				{
					var result = rm.complete();
					rm.clearRecipe();

					// TODO: add good/bad axe logic
					var input = new SingleRecipeInput(inv.getStackInSlot(0));
					inv.setStackInSlot(0, result.value().assemble(input, level.registryAccess()));
					setChangedAndUpdateClient();
				}
				else
					setChanged();
			}
		}
	}

	@Override
	public void onLoad()
	{
		super.onLoad();
		rm.onLoad();
	}

	public void placeItem(ItemStack stack)
	{
		if (!inv.getStackInSlot(0).isEmpty() || stack.isEmpty())
			return;
		inv.insertItem(0, stack.split(1), false);
		rm.updateRecipe();
		setChangedAndUpdateClient();
	}

	public ItemStack takeItem()
	{
		var stack = inv.extractItem(0, Item.ABSOLUTE_MAX_STACK_SIZE, false);
		rm.updateRecipe();
		setChangedAndUpdateClient();
		return stack;
	}

	public void takeOrPlace(ItemStack stack, Player player)
	{
		if (!inv.getStackInSlot(0).isEmpty())
		{
			var taken = takeItem();
			ItemHelper.putItemsInInventoryOrDropAt(player, taken, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
		}
		else
		{
			placeItem(stack);
		}
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		rm.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		rm.load(tag);
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		save(tag, registries);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
	}
}
