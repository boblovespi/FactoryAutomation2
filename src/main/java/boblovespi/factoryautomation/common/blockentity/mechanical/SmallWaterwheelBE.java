package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.SmallWaterwheel;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class SmallWaterwheelBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private static final IMechanicalOutput STOPPED = MechanicalManager.ZERO;
	private static final IMechanicalOutput RUNNING = new RunningOutput();
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.small_waterwheel.active");
	private static final RawAnimation STOPPED_STATE = RawAnimation.begin().thenLoop("state.small_waterwheel.stopped");
	private final MechanicalManager manager;
	private final AnimatableInstanceCache cache;
	private boolean running;

	public SmallWaterwheelBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.SMALL_WATERWHEEL_TYPE.get(), pPos, pBlockState);
		manager = new MechanicalManager("mech", this::updateInputs);
		cache = GeckoLibUtil.createInstanceCache(this);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.save(tag);
		tag.putBoolean("running", running);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		manager.load(tag);
		running = tag.getBoolean("running");
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putBoolean("running", running);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		running = tag.getBoolean("running");
	}

	@Override
	public void onDestroy()
	{
		var dir = getBlockState().getValue(SmallWaterwheel.FACING);
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), null, null, dir.getOpposite());
		if (cap != null)
			cap.update(MechanicalManager.ZERO);
	}

	@Override
	public void clientTick()
	{

	}

	@Override
	public void tick()
	{

	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		var dir = getBlockState().getValue(SmallWaterwheel.FACING);
		var be = level.getBlockEntity(worldPosition.relative(dir));
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), null, be, dir.getOpposite());
		if (cap != null)
			cap.update(manager);
	}

	public void updateWater()
	{
		setChangedAndUpdateClient();
		var dir = getBlockState().getValue(SmallWaterwheel.FACING).getClockWise();
		var poses = List.of(worldPosition.above(), worldPosition.below(), worldPosition.relative(dir), worldPosition.relative(dir.getOpposite()));
		var waterCount = 0;
		for (var pos : poses)
		{
			if (level.getBlockState(pos).is(Blocks.WATER))
				waterCount++;
		}
		if (waterCount >= 2)
			setRunning();
		else
			setStopped();
	}

	private void setStopped()
	{
		running = false;
		manager.update(STOPPED);
	}

	private void setRunning()
	{
		running = true;
		manager.update(RUNNING);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<GeoAnimatable>(this, s -> {
			if (running)
				return s.setAndContinue(ACTIVE_STATE);
			else
				return s.setAndContinue(STOPPED_STATE);
		}));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	private static class RunningOutput implements IMechanicalOutput
	{
		@Override
		public float getTorque()
		{
			return 100;
		}

		@Override
		public float getSpeed()
		{
			return 0.5f;
		}
	}
}
