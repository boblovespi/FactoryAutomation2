package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.Splitter;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.function.Function;

public class SplitterBE extends FABE implements IClientTickable
{
	private final MechanicalManager manager;

	public SplitterBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.SPLITTER_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", Function.identity(), t -> 0.5f * t, this::updateInputs);
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		updateWith(manager);
	}

	private void updateWith(IMechanicalOutput output)
	{
		if (getBlockState().getValue(Splitter.VERTICAL))
		{
			var facing = getBlockState().getValue(Splitter.FACING);
			var cw = facing.getClockWise();
			var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(cw), null, null, cw.getOpposite());
			if (cap != null)
				cap.update(output);
			var ccw = facing.getCounterClockWise();
			var cap2 = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(ccw), null, null, ccw.getOpposite());
			if (cap2 != null)
				cap2.update(output);
		}
		else
		{
			var facing = getBlockState().getValue(Splitter.FACING);
			var cw = facing.getClockWise();
			var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(cw), null, null, cw.getOpposite());
			if (cap != null)
				cap.update(output);
			var ccw = facing.getCounterClockWise();
			var cap2 = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(ccw), null, null, ccw.getOpposite());
			if (cap2 != null)
				cap2.update(output);
		}
	}

	@Nullable
	public IMechanicalOutput output(Direction direction)
	{
		return direction.getAxis() == getBlockState().getValue(Splitter.FACING).getClockWise().getAxis() ? manager : null;
	}

	@Nullable
	public IMechanicalInput input(Direction direction)
	{
		if (getBlockState().getValue(Splitter.VERTICAL))
		{
			var facing = getBlockState().getValue(Splitter.FACING);
			return facing.getAxisDirection() == Direction.AxisDirection.POSITIVE && direction == Direction.UP ||
					facing.getAxisDirection() == Direction.AxisDirection.NEGATIVE && direction == Direction.DOWN ? manager : null;
		}
		else
			return direction.getOpposite() == getBlockState().getValue(Splitter.FACING) ? manager : null;
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
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
		updateWith(MechanicalManager.ZERO);
	}

	@Override
	public void clientTick()
	{

	}
}
