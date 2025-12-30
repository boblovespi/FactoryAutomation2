package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.IHeatUser;
import boblovespi.factoryautomation.api.capability.HeatCapability;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.BurnerManager;
import boblovespi.factoryautomation.common.util.FuelInfo;
import boblovespi.factoryautomation.common.util.HeatManager;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class BrickFireboxBE extends FABE implements ITickable
{
	private final BurnerManager burner;
	private final HeatManager heat;
	private final ItemStackHandler inv;
	private final float efficiency = 0.5f;

	public BrickFireboxBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.BRICK_FIREBOX_TYPE.get(), pPos, pBlockState);
		inv = new ItemStackHandler(1)
		{
			@Override
			protected void onContentsChanged(int slot)
			{
				setChanged();
			}

			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				return stack.getItemHolder().getData(FuelInfo.FUEL_DATA) != null;
			}
		};
		heat = new HeatManager("heat", 2300 * 1000, 50000, 0.9f);
		burner = new BurnerManager("burner", () -> inv.getStackInSlot(0), this::takeFuel, (t, e) -> {
			if (t * efficiency + 273 * (1 - efficiency) >= heat.getTemperature())
				heat.heat(e * efficiency * 0.5f);
		}, this::notifyBurning);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		burner.save(tag);
		heat.save(tag);
		tag.put("inv", inv.serializeNBT(registries));
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		burner.load(tag);
		heat.load(tag);
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
	}

	@Override
	public void tick()
	{
		burner.progress();
		var cap = level.getCapability(HeatCapability.BLOCK, worldPosition.above(), Direction.DOWN);
		if (cap != null)
			cap.conductWith(heat);
		setChanged();
	}

	private FuelInfo takeFuel()
	{
		var stack = inv.extractItem(0, 1, false);
		if (stack.getItemHolder().getData(FuelInfo.FUEL_DATA) == null)
		{
			inv.setStackInSlot(0, ItemStack.EMPTY);
			FactoryAutomation.LOGGER.error("Stack <{}> has no fuel data!", stack);
		}
		return Objects.requireNonNullElse(stack.getItemHolder().getData(FuelInfo.FUEL_DATA), new FuelInfo(0, 0, 0));
	}

	@Nullable
	public IHeatUser heatManager(@Nullable Direction direction)
	{
		if (direction == Direction.UP)
			return heat;
		return null;
	}

	@Nullable
	public IItemHandler itemHandler(@Nullable Direction direction)
	{
		return inv;
	}

	private void notifyBurning(boolean b)
	{
		if (level.isClientSide)
			return;
		if (b)
		{
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(StoneCrucible.LIT, true));
			setChangedAndUpdateClient();
		}
		else
		{
			level.setBlockAndUpdate(worldPosition, getBlockState().setValue(StoneCrucible.LIT, false));
			setChangedAndUpdateClient();
		}
	}
}
