package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.common.recipe.BrickDryingRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class BrickMakerFrameBE extends FABE implements ITickable
{
	private final RecipeManager<BrickDryingRecipe> left;
	private final RecipeManager<BrickDryingRecipe> right;
	private final ItemStackHandler inv;

	public BrickMakerFrameBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_MAKER_FRAME_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(2);
		left = new RecipeManager<>("left", r -> isValid(r, 0), () -> findMatchingRecipe(0), this::getRecipe);
		right = new RecipeManager<>("right", r -> isValid(r, 1), () -> findMatchingRecipe(1), this::getRecipe);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		left.save(tag);
		right.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		left.load(tag);
		right.load(tag);
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
	public void onLoad()
	{
		super.onLoad();
		left.onLoad();
		right.onLoad();
	}

	@Override
	public void onDestroy()
	{

	}

	@Override
	public void tick()
	{
		var update = false;
		if (left.hasRecipe())
		{
			left.progress();
			if (left.isComplete())
			{
				var result = left.complete();
				left.clearRecipe();
				var assembled = result.value().assemble(new BrickDryingRecipe.Input(inv.extractItem(0, 1, false)), level.registryAccess());
				inv.insertItem(0, assembled, false);
				update = true;
			}
		}
		if (right.hasRecipe())
		{
			right.progress();
			if (right.isComplete())
			{
				var result = right.complete();
				right.clearRecipe();
				var assembled = result.value().assemble(new BrickDryingRecipe.Input(inv.extractItem(1, 1, false)), level.registryAccess());
				inv.insertItem(1, assembled, false);
				update = true;
			}
		}
		if (update)
			setChangedAndUpdateClient();
		else
			setChanged();
	}

	public void placeItem(ItemStack stack, int slot)
	{
		if (!inv.getStackInSlot(slot).isEmpty() || stack.isEmpty())
			return;
		inv.insertItem(slot, stack.split(1), false);
		(slot == 0 ? left : right).updateRecipe();
		setChangedAndUpdateClient();
	}

	public ItemStack takeItem(int slot)
	{
		var stack = inv.extractItem(slot, Item.ABSOLUTE_MAX_STACK_SIZE, false);
		(slot == 0 ? left : right).updateRecipe();
		setChangedAndUpdateClient();
		return stack;
	}

	public void takeOrPlace(ItemStack stack, Player player, int slot)
	{
		if (!inv.getStackInSlot(slot).isEmpty())
		{
			var taken = takeItem(slot);
			ItemHelper.putItemsInInventoryOrDropAt(player, taken, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
		}
		else
			placeItem(stack, slot);
	}

	private boolean isValid(RecipeHolder<BrickDryingRecipe> r, int slot)
	{
		return r.value().matches(new BrickDryingRecipe.Input(inv.getStackInSlot(slot)), level);
	}

	@Nullable
	private RecipeHolder<BrickDryingRecipe> findMatchingRecipe(int slot)
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.BRICK_DRYING_TYPE.get(), new BrickDryingRecipe.Input(inv.getStackInSlot(slot)), level).orElse(null);
	}

	private Optional<RecipeHolder<?>> getRecipe(ResourceLocation k)
	{
		return level.getRecipeManager().byKey(k);
	}
}
