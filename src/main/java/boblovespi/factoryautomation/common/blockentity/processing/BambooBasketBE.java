package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.recipe.BasketDryingRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.Codecs;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class BambooBasketBE extends FABE implements ITickable
{
	private final List<RecipeManager<BasketDryingRecipe>> recipes;
	private final ItemStackHandler inv;

	public BambooBasketBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.BAMBOO_BASKET_TYPE.get(), pos, state);
		inv = new ItemStackHandler(5);
		recipes = IntStream.of(0, 1, 2, 3, 4).mapToObj(i -> new RecipeManager<>("recipe_" + i, r -> isValid(r, i), () -> findMatchingRecipe(i))).toList();
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.put("inv", inv.serializeNBT(registries));
		recipes.forEach(r -> r.save(tag));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		inv.deserializeNBT(registries, tag.getCompound("inv"));
		recipes.forEach(r -> r.load(tag, registries));
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
	public void onDestroy()
	{
		ItemHelper.dropAllItems(level, worldPosition.getCenter(), inv);
	}

	@Override
	public void tick()
	{
		var update = false;
		for (int i = 0; i < recipes.size(); i++)
		{
			var manager = recipes.get(i);
			if (manager.hasRecipe())
			{
				manager.progress();
				if (manager.isComplete())
				{
					var result = manager.getCompleted();
					var assembled = result.value().assemble(new BasketDryingRecipe.Input(inv.extractItem(i, 1, false)), level.registryAccess());
					inv.insertItem(i, assembled, false);
					update = true;
				}
			}
		}
		if (update)
			setChangedAndUpdateClient();
		else
			setChanged();
	}

	public boolean placeItem(ItemStack stack, int slot)
	{
		if (!inv.getStackInSlot(slot).isEmpty() || stack.isEmpty())
			return false;
		if (level.getRecipeManager().getRecipeFor(RecipeThings.BASKET_DRYING_TYPE.get(), new BasketDryingRecipe.Input(stack), level).isEmpty())
			return false;
		if (stack.getItem() instanceof BlockItem block)
			level.playSound(null, worldPosition, block.getBlock().getSoundType(block.getBlock().defaultBlockState(), level, worldPosition, null).getPlaceSound(),
					SoundSource.BLOCKS);
		else
			level.playSound(null, worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
		inv.insertItem(slot, stack.split(1), false);
		recipes.get(slot).updateRecipe();
		setChangedAndUpdateClient();
		return true;
	}

	public ItemStack takeItem(int slot)
	{
		var stack = inv.extractItem(slot, Item.ABSOLUTE_MAX_STACK_SIZE, false);
		recipes.get(slot).updateRecipe();
		setChangedAndUpdateClient();
		return stack;
	}

	public boolean takeOrPlace(ItemStack stack, Player player, int slot)
	{
		if (!inv.getStackInSlot(slot).isEmpty())
		{
			var taken = takeItem(slot);
			var b = !taken.isEmpty();
			ItemHelper.putItemsInInventoryOrDropAt(player, taken, level, Vec3.atLowerCornerWithOffset(worldPosition, 0.5f, 7 / 16f, 0.5f));
			return b;
		}
		else
			return placeItem(stack, slot);
	}

	private boolean isValid(RecipeHolder<BasketDryingRecipe> r, int slot)
	{
		return r.value().matches(new BasketDryingRecipe.Input(inv.getStackInSlot(slot)), level);
	}

	@Nullable
	private RecipeHolder<BasketDryingRecipe> findMatchingRecipe(int slot)
	{
		return level.getRecipeManager().getRecipeFor(RecipeThings.BASKET_DRYING_TYPE.get(), new BasketDryingRecipe.Input(inv.getStackInSlot(slot)), level).orElse(null);
	}

	public ItemStackHandler getRenderStacks()
	{
		if (level.isClientSide)
			return inv;
		return new ItemStackHandler();
	}

	public List<ItemStack> makeViewStacks()
	{
		var list = new ArrayList<ItemStack>(5);
		for (var i = 0; i < inv.getSlots(); i++)
		{
			var stack = inv.getStackInSlot(i).copy();
			if (stack.isEmpty())
				continue;
			if (!recipes.get(i).isComplete())
			{
				var finalI = i;
				stack.update(DataComponents.CUSTOM_DATA, CustomData.EMPTY, t -> t.update(NbtOps.INSTANCE, Codecs.JADE_COOKING, recipes.get(finalI).getProgress()).getOrThrow());
			}
			list.add(stack);
		}
		return list;
	}
}
