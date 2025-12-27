package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class HorseEngineBE extends FABE implements ITickable, IClientTickable, GeoBlockEntity
{
	private static final RawAnimation ACTIVE_STATE = RawAnimation.begin().thenLoop("state.horse_engine.active");
	private static final float blocksPerTick = 43 / 20f;
	private static final float radiusCircle = 4;
	private static final float radiansPerTick = blocksPerTick / radiusCircle;
	private final AnimatableInstanceCache cache;
	private final MechanicalManager mech;
	private boolean hasHorse = false;
	@Nullable
	private UUID horseId;
	@Nullable
	private Mob horse;
	private int moveTimer = 0;
	private float angle = 0;
	private float horseSpeed;

	public HorseEngineBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.HORSE_ENGINE_TYPE.get(), pos, state);
		cache = GeckoLibUtil.createInstanceCache(this);
		mech = new MechanicalManager("mech", () -> {});
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		mech.save(tag);
		if (hasHorse)
			tag.putUUID("horseId", horseId);
		tag.putBoolean("hasHorse", hasHorse);
		tag.putFloat("horseSpeed", horseSpeed);
		tag.putFloat("moveTimer", moveTimer);
		tag.putFloat("angle", angle);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		mech.load(tag);
		hasHorse = tag.getBoolean("hasHorse");
		horseSpeed = tag.getFloat("horseSpeed");
		if (hasHorse)
			horseId = tag.getUUID("horseId");
		moveTimer = tag.getInt("moveTimer");
		angle = tag.getFloat("angle");
	}

	@Override
	protected void saveMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putFloat("horseSpeed", horseSpeed);
		tag.putFloat("angle", angle);
	}

	@Override
	protected void loadMini(CompoundTag tag, HolderLookup.Provider registries)
	{
		horseSpeed = tag.getFloat("horseSpeed");
		angle = tag.getFloat("angle");
	}

	@Override
	public void onDestroy()
	{
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(Direction.DOWN), Direction.DOWN.getOpposite());
		if (cap != null)
			cap.update(MechanicalManager.ZERO);
	}

	@Override
	public void tick()
	{
		if (!hasHorse)
			return;
		if (horse == null)
		{
			if (horseId == null)
			{
				hasHorse = false;
				horseSpeed = 0;
				mech.update(MechanicalManager.ZERO);
				updateInputs();
				return;
			}
			horse = ((Mob) ((ServerLevel) level).getEntity(horseId));
			if (horse == null)
			{
				horseId = null;
				hasHorse = false;
				horseSpeed = 0;
				mech.update(MechanicalManager.ZERO);
				updateInputs();
				return;
			}
		}
		float x, y, z;
		angle -= radiansPerTick * horseSpeed;
		angle = angle % (2 * Mth.PI);
		x = worldPosition.getX() + 0.5f + radiusCircle * Mth.cos(angle);
		y = worldPosition.getY();
		z = worldPosition.getZ() + 0.5f + radiusCircle * Mth.sin(angle);
		horse.getMoveControl().setWantedPosition(x, y, z, 2);
		horse.getNavigation().stop();
	}

	@Override
	public void clientTick()
	{
		angle -= radiansPerTick * horseSpeed;
		angle = angle % (2 * Mth.PI);
	}

	public float getRenderRot(float delta)
	{
		if (!level.isClientSide)
			return 0;
		return (float) Math.toDegrees(angle - radiansPerTick * horseSpeed * delta);
	}

	@Override
	public void registerControllers(AnimatableManager.ControllerRegistrar controllers)
	{
		controllers.add(new AnimationController<>(this, s -> s.setAndContinue(ACTIVE_STATE)));
	}

	@Override
	public AnimatableInstanceCache getAnimatableInstanceCache()
	{
		return cache;
	}

	public void updateInputs()
	{
		setChangedAndUpdateClient();
		var dir = Direction.DOWN;
		var cap = level.getCapability(MechanicalCapability.INPUT, worldPosition.relative(dir), dir.getOpposite());
		if (cap != null)
			cap.update(mech);
	}

	public boolean attachHorse(AbstractHorse horse)
	{
		if (hasHorse)
			return false;
		hasHorse = true;
		this.horse = horse;
		horseId = horse.getUUID();
		horseSpeed = (float) horse.getAttributeValue(Attributes.MOVEMENT_SPEED);
		var output = new Output((float) (horse.getAttributeValue(Attributes.JUMP_STRENGTH) * 40), horseSpeed * radiansPerTick * 20f);
		mech.update(output);
		updateInputs();
		setChangedAndUpdateClient();
		return true;
	}

	public void removeHorse()
	{
		if (!hasHorse)
			return;
		hasHorse = false;
		horse = null;
		horseId = null;
		horseSpeed = 0;
		mech.update(MechanicalManager.ZERO);
		updateInputs();
		setChangedAndUpdateClient();
	}

	public boolean hasHorse()
	{
		return hasHorse;
	}

	private record Output(float torque, float speed) implements IMechanicalOutput
	{
		@Override
		public float getTorque()
		{
			return torque;
		}

		@Override
		public float getSpeed()
		{
			return speed;
		}
	}
}
