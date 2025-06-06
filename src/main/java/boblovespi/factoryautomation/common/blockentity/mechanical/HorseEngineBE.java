package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
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

import java.util.UUID;

public class HorseEngineBE extends FABE implements ITickable
{
	private static final float blocksPerTick = 43 / 20f;
	private static final float radiusCircle = 4;
	private static final float radiansPerTick = blocksPerTick / radiusCircle;
	private final MechanicalManager mech;
	private boolean hasHorse = false;
	@Nullable
	private UUID horseId;
	@Nullable
	private Mob horse;
	private int moveTimer = 0;
	private float angle = 0;

	public HorseEngineBE(BlockPos pos, BlockState state)
	{
		super(FABETypes.HORSE_ENGINE_TYPE.get(), pos, state);
		mech = new MechanicalManager("mech", () -> {});
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		mech.save(tag);
		if (hasHorse)
			tag.putUUID("horseId", horseId);
		tag.putBoolean("hasHorse", hasHorse);
		tag.putFloat("moveTimer", moveTimer);
		tag.putFloat("angle", angle);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		mech.load(tag);
		hasHorse = tag.getBoolean("hasHorse");
		if (hasHorse)
			horseId = tag.getUUID("horseId");
		moveTimer = tag.getInt("moveTimer");
		angle = tag.getFloat("angle");
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
				mech.update(MechanicalManager.ZERO);
				updateInputs();
				return;
			}
			horse = ((Mob) ((ServerLevel) level).getEntity(horseId));
			if (horse == null)
			{
				horseId = null;
				hasHorse = false;
				mech.update(MechanicalManager.ZERO);
				updateInputs();
				return;
			}
		}
		float x, y, z;
		angle -= (float) (radiansPerTick * horse.getAttributeValue(Attributes.MOVEMENT_SPEED));
		angle = angle % (2 * Mth.PI);
		x = worldPosition.getX() + 0.5f + radiusCircle * Mth.cos(angle);
		y = worldPosition.getY();
		z = worldPosition.getZ() + 0.5f + radiusCircle * Mth.sin(angle);
		horse.getMoveControl().setWantedPosition(x, y, z, 2);
		horse.getNavigation().stop();
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
		var output = new Output((float) (horse.getAttributeValue(Attributes.JUMP_STRENGTH) * 40), (float) (horse.getAttributeValue(Attributes.MOVEMENT_SPEED) * radiansPerTick * 20f));
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
