package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.util.MechanicalPowerPropagatorManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class CreativeMechanicalSourceBE extends FABE implements IMechanicalOutput
{
	private float torque;
	private float speed;
	private final MechanicalPowerPropagatorManager propManager;

	public CreativeMechanicalSourceBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.CREATIVE_MECHANICAL_SOURCE_TYPE.get(), pPos, pBlockState);
		propManager = new MechanicalPowerPropagatorManager(List.of(Direction.values()), () -> !isRemoved(), d -> {}, this::updateInputs);
	}

	@Override
	protected void save(CompoundTag tag, HolderLookup.Provider registries)
	{
		tag.putFloat("torque", torque);
		tag.putFloat("speed", speed);
	}

	@Override
	protected void load(CompoundTag tag, HolderLookup.Provider registries)
	{
		torque = tag.getFloat("torque");
		speed = tag.getFloat("speed");
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
	public float getTorque()
	{
		return torque;
	}

	@Override
	public float getSpeed()
	{
		return speed;
	}

	@Override
	public void onLoad()
	{
		if (!level.isClientSide && level instanceof ServerLevel serverLevel)
			propManager.load(serverLevel, worldPosition);
	}

	public void changeSpeed(float d)
	{
		speed += d;
		if (speed < 0)
			speed = 0;
		setChanged();
		propManager.updateAll();
	}

	public void changeTorque(float d)
	{
		torque += d;
		if (torque < 0)
			torque = 0;
		setChanged();
		propManager.updateAll();
	}

	public void updateInputs(Direction dir)
	{
		var cap = propManager.get(dir);
		if (cap != null)
			cap.update(this);
	}
}
