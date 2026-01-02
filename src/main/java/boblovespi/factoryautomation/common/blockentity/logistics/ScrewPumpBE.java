package boblovespi.factoryautomation.common.blockentity.logistics;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.common.block.logistics.ScrewPump;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.function.Function;

public class ScrewPumpBE extends FABE implements ITickable
{
	private static final float TIME_IN_SECONDS = 5;
	private static final float MIN_TORQUE = 0.9f;
	private static final int TIME_IN_TICKS = (int) (20 * TIME_IN_SECONDS);

	private final MechanicalManager mechanicalManager;
	private float counter;

	public ScrewPumpBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.SCREW_PUMP_TYPE.get(), pos, state);
		mechanicalManager = new MechanicalManager("rot", Function.identity(), Function.identity(), this::setChangedAndUpdateClient);
		counter = TIME_IN_TICKS;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.save(tag);
		tag.putFloat("counter", counter);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		mechanicalManager.load(tag);
		counter = tag.getFloat("counter");
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

	}

	@Override
	public void tick()
	{
		if (mechanicalManager.getTorque() >= MIN_TORQUE)
		{
			counter -= mechanicalManager.getSpeed();
			if (counter < 0)
			{
				counter = TIME_IN_TICKS;
				var waterSides = (int) Direction.Plane.HORIZONTAL.stream()
																 .filter(dir -> level.getFluidState(worldPosition.relative(dir)).isSourceOfType(Fluids.WATER))
																 .count();
				var isWater = level.getFluidState(worldPosition).is(Fluids.WATER);

				if (waterSides >= 2 || isWater)
				{
					var facing = getBlockState().getValue(ScrewPump.FACING);
					var capability = level.getCapability(Capabilities.FluidHandler.BLOCK, worldPosition.above().relative(facing), facing);
					if (capability != null)
						capability.fill(new FluidStack(Fluids.WATER, 1000), IFluidHandler.FluidAction.EXECUTE);

					if (waterSides < 2)
						level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ScrewPump.WATERLOGGED, false));
					else if (!isWater)
						level.setBlockAndUpdate(worldPosition, getBlockState().setValue(ScrewPump.WATERLOGGED, true));
				}
			}
			setChanged();
		}
	}

	@Nullable
	public IMechanicalInput input(@Nullable Direction dir)
	{
		if (dir == Direction.UP)
			return mechanicalManager;
		return null;
	}
}
