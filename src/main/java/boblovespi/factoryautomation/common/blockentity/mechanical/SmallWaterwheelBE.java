package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.SmallWaterwheel;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.Triplet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Function;

public class SmallWaterwheelBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private static final IMechanicalOutput STOPPED = MechanicalManager.ZERO;
	private static final IMechanicalOutput RUNNING = new RunningOutput(25 / 18f / 12f * 16, 100);
	private static final float UNDERSHOT_SPEED = 9 * 25 / 18f / 1.5f * 2 * Mth.PI / 60f;
	private static final float OVERSHOT_SPEED = 21 / Mth.sqrt(1.5f) * Mth.PI * 2 / 60f;
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.small_waterwheel.active");
	private static final RawAnimation STOPPED_STATE = RawAnimation.begin().thenLoop("state.small_waterwheel.stopped");
	private final MechanicalManager manager;
	private final AnimatableInstanceCache cache;
	private final List<Triplet<BlockPos, Direction, Vec3>> values;
	private boolean running;

	public SmallWaterwheelBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.SMALL_WATERWHEEL_TYPE.get(), pos, state);
		manager = new MechanicalManager("mech", this::updateInputs);
		cache = GeckoLibUtil.createInstanceCache(this);
		var axisDir = state.getValue(SmallWaterwheel.FACING);
		var axis = axisDir.getAxis();
		values = Direction.stream()
						  .filter(d -> d.getAxis() != axis)
						  .map(Triplet.apply3(pos::relative, Function.identity(), d -> Vec3.atLowerCornerOf(d.getClockWise(axis).getNormal())))
						  .toList();
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
		var pair = values.stream()
						 .map(t -> t.match((p, d, n) -> {
							 var flow = level.getFluidState(p).getFlow(level, p);
							 var flowDot = flow.dot(n);
							 return new Pair<>(flowDot, (d == Direction.DOWN || Math.abs(flowDot) < 0.2f));
						 }))
						 .reduce(new Pair<>(0d, true), (l, r) -> new Pair<>(l.getFirst() + r.getFirst(), l.getSecond() && r.getSecond()));
		var velocity = Mth.abs(pair.getFirst().floatValue());
		var isUndershot = pair.getSecond();
		if (velocity >= 0.5f)
			setRunning(isUndershot ? UNDERSHOT_SPEED : Mth.clampedLerp(velocity / 2, 0, OVERSHOT_SPEED), velocity * 100);
		else
			setStopped();
	}

	private void setStopped()
	{
		running = false;
		manager.update(STOPPED);
	}

	private void setRunning(float speed, float torque)
	{
		running = true;
		manager.update(new RunningOutput(speed, torque));
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

	private record RunningOutput(float getSpeed, float getTorque) implements IMechanicalOutput {}
}
