package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.block.mechanical.LargeWaterwheel;
import boblovespi.factoryautomation.common.blockentity.*;
import boblovespi.factoryautomation.common.multiblock.IMultiblockBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import boblovespi.factoryautomation.common.util.Triplet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
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

public class LargeWaterwheelBE extends FABE implements ITickable, IClientTickable, IMultiblockBE, GeoBlockEntity, IRotate
{
	private static final IMechanicalOutput STOPPED = MechanicalManager.ZERO;
	// see https://en.wikipedia.org/wiki/Water_wheel#The_power_of_a_wheel
	private static final float UNDERSHOT_SPEED = 9 * 25 / 18f / 6 * 2 * Mth.PI / 60f;
	private static final float OVERSHOT_SPEED = 21 / Mth.sqrt(6) * Mth.PI * 2 / 60f;
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.large_waterwheel.active");
	private static final RawAnimation STOPPED_STATE = RawAnimation.begin().thenLoop("state.large_waterwheel.stopped");
	private final MechanicalManager manager;
	private final AnimatableInstanceCache cache;
	// block pos of water, does this pos make it overshot, the normal
	private final List<Triplet<BlockPos, Boolean, Vec3>> values;
	private boolean running;
	private float rot;
	private boolean counterclockwise;
	private boolean breaking;

	public LargeWaterwheelBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.LARGE_WATERWHEEL_TYPE.get(), pos, state);
		manager = new MechanicalManager("mech", this::updateInputs);
		cache = GeckoLibUtil.createInstanceCache(this);
		var dir = state.getValue(LargeWaterwheel.FACING).getClockWise();
		//       0  1  2
		//    15 .  .  .  3
		// 14 .     .     .  4
		// 13 .  .  C  .  .  5
		// 12 .     .     .  6
		//    11 .  .  .  7
		//       10 9  8

		var forwardNormal = Vec3.atLowerCornerOf(dir.getNormal());
		var backwardNormal = Vec3.atLowerCornerOf(dir.getOpposite().getNormal());
		var downNormal = new Vec3(0, -1, 0);
		var upNormal = downNormal.scale(-1);
		values = List.of(
				new Triplet<>(pos.relative(dir, -1).above(3), true, forwardNormal),
				new Triplet<>(pos.relative(dir, 0).above(3), true, forwardNormal),
				new Triplet<>(pos.relative(dir, 1).above(3), true, forwardNormal),
				new Triplet<>(pos.relative(dir, 2).above(2), true, downNormal),
				new Triplet<>(pos.relative(dir, 3).above(1), true, downNormal),
				new Triplet<>(pos.relative(dir, 3).above(0), true, downNormal),
				new Triplet<>(pos.relative(dir, 3).above(-1), true, downNormal),
				new Triplet<>(pos.relative(dir, 2).above(-2), false, backwardNormal),
				new Triplet<>(pos.relative(dir, 1).above(-3), false, backwardNormal),
				new Triplet<>(pos.relative(dir, 0).above(-3), false, backwardNormal),
				new Triplet<>(pos.relative(dir, -1).above(-3), false, backwardNormal),
				new Triplet<>(pos.relative(dir, -2).above(-2), false, backwardNormal),
				new Triplet<>(pos.relative(dir, -3).above(-1), true, upNormal),
				new Triplet<>(pos.relative(dir, -3).above(0), true, upNormal),
				new Triplet<>(pos.relative(dir, -3).above(1), true, upNormal),
				new Triplet<>(pos.relative(dir, -2).above(2), true, upNormal)
						);
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
		save(tag, registries);
		tag.putBoolean("counterclockwise", counterclockwise);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		load(tag, registries);
		counterclockwise = tag.getBoolean("counterclockwise");
	}

	@Override
	public void onDestroy()
	{
		if (getBlockState().getValue(LargeWaterwheel.MULTIBLOCK_COMPLETE))
		{
			breaking = true;
			Multiblocks.LARGE_WATERWHEEL.destroy(level, worldPosition, getBlockState().getValue(LargeWaterwheel.FACING).getClockWise());
		}
		var dir = getBlockState().getValue(LargeWaterwheel.FACING);
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

	@Override
	public void tick()
	{
		updateWater();
	}

	private void updateInputs()
	{
		setChangedAndUpdateClient();
		var dir = getBlockState().getValue(LargeWaterwheel.FACING);
		var be = level.getBlockEntity(worldPosition.relative(dir));
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), null, be, dir.getOpposite());
		if (cap != null)
			cap.update(manager);
	}

	private void updateWater()
	{
		setChangedAndUpdateClient();
		var pair = values.stream()
						 .map(t -> t.match((p, d, n) -> {
							 var flow = level.getFluidState(p).getFlow(level, p);
							 var flowDot = flow.dot(n);
							 return new Pair<>(flowDot, !d || Math.abs(flowDot) < 0.2f);
						 }))
						 .reduce(new Pair<>(0d, true), (l, r) -> new Pair<>(l.getFirst() + r.getFirst(), l.getSecond() && r.getSecond()));
		var netFlow = Mth.abs(Mth.clamp(pair.getFirst().floatValue() / 10.5f, 0, 1));
		counterclockwise = pair.getFirst() * getBlockState().getValue(LargeWaterwheel.FACING).getAxisDirection().getStep() >= 0;
		var isUndershot = pair.getSecond();
		if (netFlow >= 3 / 11f - 0.01f)
			setRunning(isUndershot ? UNDERSHOT_SPEED : Mth.clampedLerp(0, OVERSHOT_SPEED, netFlow), netFlow * 1800);
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

	@Override
	public void onMultiblockBuilt()
	{

	}

	@Override
	public void onMultiblockDestroyed()
	{
		if (!breaking)
		{
			setStopped();
			level.setBlock(worldPosition, getBlockState().setValue(LargeWaterwheel.MULTIBLOCK_COMPLETE, false), 2);
		}
	}

	@Override
	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (counterclockwise ? 1 : -1) * ((rot + delta * (float) (Math.toDegrees(manager.getSpeed()) / 20)) % 360);
	}

	private record RunningOutput(float getSpeed, float getTorque) implements IMechanicalOutput {}
}
