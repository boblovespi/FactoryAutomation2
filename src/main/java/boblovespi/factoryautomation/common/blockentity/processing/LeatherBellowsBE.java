package boblovespi.factoryautomation.common.blockentity.processing;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.capability.BellowsCapability;
import boblovespi.factoryautomation.common.block.processing.LeatherBellows;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nullable;

public class LeatherBellowsBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private int lerp;
	private float counter = 100;
	private static final RawAnimation BLOW_ONCE = RawAnimation.begin().thenPlay("action.bellows.blow_once").thenLoop("state.bellows.base");
	private static final RawAnimation STANDBY_STATE = RawAnimation.begin().thenLoop("state.bellows.base");
	private final AnimatableInstanceCache cache;
	private final MechanicalManager mechanicalManager;

	public LeatherBellowsBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.LEATHER_BELLOWS_TYPE.get(), pPos, pBlockState);
		cache = GeckoLibUtil.createInstanceCache(this);
		mechanicalManager = new MechanicalManager("rot", this::setChangedAndUpdateClient);
	}

	private PlayState handleAnim(AnimationState<LeatherBellowsBE> s)
	{
		return lerp > 0 ? s.setAndContinue(BLOW_ONCE) : s.setAndContinue(STANDBY_STATE);
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

	}

	@Override
	public void clientTick()
	{
		if (lerp > 0)
			lerp--;
		counter -= counterModification();
		if (counter <= 0)
		{
			counter = 100;
			blow();
		}
	}

	public void blow()
	{
		level.playSound(null, worldPosition, FASounds.BELLOWS_BLOWS.get(), SoundSource.BLOCKS, 0.8f, 1.5f);
		var facing = getBlockState().getValue(LeatherBellows.FACING);
		if (level.isClientSide)
		{
			var offset = Vec3.atLowerCornerOf(facing.getNormal()).multiply(0.5, 0.5, 0.5);
			var pos2 = worldPosition.getBottomCenter().add(offset);
			for (int i = 0; i < 3; i++)
				level.addParticle(ParticleTypes.CLOUD, pos2.x, pos2.y, pos2.z,
						offset.x * 0.3 + level.random.triangle(0, 0.1),
						offset.y + level.random.triangle(0.05, 0.1),
						offset.z * 0.3 + level.random.triangle(0, 0.1));
			if (lerp == 0)
				lerp = 20;
		}
		else
		{
			var cap = level.getCapability(BellowsCapability.BLOCK, worldPosition.relative(facing), facing.getOpposite());
			if (cap != null)
				cap.blow(0.75f, 400);
		}
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, this::handleAnim));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	@Override
	public void tick()
	{
		counter -= counterModification();
		if (counter <= 0)
		{
			counter = 100;
			blow();
		}
	}

	@Nullable
	public IMechanicalInput input(@Nullable Direction direction)
	{
		if (direction != null && direction == getBlockState().getValue(LeatherBellows.FACING).getOpposite())
			return mechanicalManager;
		return null;
	}

	private float counterModification()
	{
		if (isFastEnoughForTorque())
		{
			var speed = mechanicalManager.getSpeed() / 20f;
			if (speed < 0.01f)
				return 0;
			var timeInTicks = 2.5f * 20 * speed;
			return 100f / timeInTicks;
		}
		return 0;
	}

	private boolean isFastEnoughForTorque()
	{
		var efficiency = 2 * (calculateEfficiency() - 0.5f);
		var speed = mechanicalManager.getSpeed();
		return efficiency * 20 <= speed;
	}

	private float calculateEfficiency()
	{
		return Mth.clamp(mechanicalManager.getTorque() / 30f, 0.5f, 1f);
	}
}
