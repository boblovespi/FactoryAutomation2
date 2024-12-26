package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.HandCrank;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class HandCrankBE extends FABE implements ITickable, IClientTickable
{
	private static final IMechanicalOutput STOPPED = new StoppedOutput();
	private static final IMechanicalOutput RUNNING = new RunningOutput();
	private final MechanicalManager manager;
	private int timer;
	private float rot;

	public HandCrankBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.HANDCRANK_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", Function.identity(), Function.identity(), this::updateInputs);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
		tag.putInt("timer", timer);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
		timer = tag.getInt("timer");
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
		var dir = getBlockState().getValue(HandCrank.HANGING) ? Direction.UP : Direction.DOWN;
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), null, null, dir.getOpposite());
		if (cap != null)
			cap.update(MechanicalManager.ZERO);
	}

	@Override
	public void clientTick()
	{
		rot += (float) (Math.toDegrees(manager.getSpeed()) / 20);
		rot %= 360;
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (rot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360;
	}

	@Override
	public void tick()
	{
		if (timer > 0)
			timer--;
		if (timer == 0)
		{
			manager.update(STOPPED);
			timer = -1;
		}
	}

	public void setRunning()
	{
		manager.update(RUNNING);
		timer = 360;
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		var dir = getBlockState().getValue(HandCrank.HANGING) ? Direction.UP : Direction.DOWN;
		var be = level.getBlockEntity(worldPosition.relative(dir));
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), null, be, dir.getOpposite());
		if (cap != null)
			cap.update(manager);
	}

	private static class StoppedOutput implements IMechanicalOutput
	{
		@Override
		public float getTorque()
		{
			return 0;
		}

		@Override
		public float getSpeed()
		{
			return 0;
		}
	}

	private static class RunningOutput implements IMechanicalOutput
	{
		@Override
		public float getTorque()
		{
			return 1;
		}

		@Override
		public float getSpeed()
		{
			return 1;
		}
	}
}
