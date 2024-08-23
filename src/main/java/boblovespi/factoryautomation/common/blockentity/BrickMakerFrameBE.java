package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.recipe.BrickDryingRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.ItemHelper;
import boblovespi.factoryautomation.common.util.RecipeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Optional;

public class BrickMakerFrameBE extends FABE implements ITickable
{
	private final RecipeManager<BrickDryingRecipe> left;
	private final RecipeManager<BrickDryingRecipe> right;
	private final ItemStackHandler inv;
	private Block leftRender;
	private Block rightRender;

	public BrickMakerFrameBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_MAKER_FRAME_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(2);
		left = new RecipeManager<>("left", r -> isValid(r, 0), () -> findMatchingRecipe(0), this::getRecipe);
		right = new RecipeManager<>("right", r -> isValid(r, 1), () -> findMatchingRecipe(1), this::getRecipe);
		leftRender = Blocks.AIR;
		rightRender = Blocks.AIR;
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
		var ops = registries.createSerializationContext(NbtOps.INSTANCE);
		var leftBlock = left.getRecipe() == null ? Blocks.AIR : left.isComplete() ? left.getRecipe().getOutBlock() : left.getRecipe().getInBlock();
		var rightBlock = right.getRecipe() == null ? Blocks.AIR : right.isComplete() ? right.getRecipe().getOutBlock() : right.getRecipe().getInBlock();
		BuiltInRegistries.BLOCK.byNameCodec().encodeStart(ops, leftBlock).ifSuccess(tag1 -> tag.put("leftBlock", tag1));
		BuiltInRegistries.BLOCK.byNameCodec().encodeStart(ops, rightBlock).ifSuccess(tag1 -> tag.put("rightBlock", tag1));
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		var ops = registries.createSerializationContext(NbtOps.INSTANCE);
		BuiltInRegistries.BLOCK.byNameCodec().decode(ops, tag.get("leftBlock")).ifSuccess(p -> leftRender = p.getFirst());
		BuiltInRegistries.BLOCK.byNameCodec().decode(ops, tag.get("rightBlock")).ifSuccess(p -> rightRender = p.getFirst());
		requestModelDataUpdate();
		if (level != null && level.isClientSide)
			level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_IMMEDIATE);
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

	@Override
	public ModelData getModelData()
	{
		var b = ModelData.builder();
		var props = new Block[] {leftRender, rightRender};
		b.with(FactoryAutomation.PARTIAL_DYNAMIC_TEXTURE_PROPERTY, props);
		return b.build();
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
