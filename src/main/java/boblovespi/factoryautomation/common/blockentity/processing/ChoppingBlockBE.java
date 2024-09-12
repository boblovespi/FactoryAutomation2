package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
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
		if (rm.hasRecipe() && tool.is(ItemTags.AXES))
		{
			level.playSound(null, worldPosition, FASounds.USE_CHOPPING_BLOCK.get(), SoundSource.BLOCKS, 1, 1.1f + level.random.nextFloat() / 5);
			tool.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
			rm.progress();
			if (rm.isComplete())
			{
				var result = rm.getCompleted();
				rm.clearRecipe();

				var input = new SingleRecipeInput(inv.getStackInSlot(0));
				var assembled = result.value().assemble(input, level.registryAccess());
				if (tool.is(FATags.Items.GOOD_AXES))
					assembled.setCount(assembled.getCount() * 2);
				inv.setStackInSlot(0, assembled);
				setChangedAndUpdateClient();
			}
			else
				setChanged();
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

	public ItemStack getRenderStack()
	{
		if (level.isClientSide)
			return inv.getStackInSlot(0);
		return ItemStack.EMPTY;
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

	@Override
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}
}
