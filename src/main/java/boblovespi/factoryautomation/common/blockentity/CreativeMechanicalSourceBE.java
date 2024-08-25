package boblovespi.factoryautomation.common.blockentity;

import boblovespi.factoryautomation.api.IMechanicalOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;

public class CreativeMechanicalSourceBE extends FABE implements IMechanicalOutput
{
	private float torque;
	private float speed;

	public CreativeMechanicalSourceBE(BlockPos pPos, BlockState pBlockState)
	{
		super(FABETypes.CREATIVE_MECHANICAL_SOURCE_TYPE.get(), pPos, pBlockState);
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

	public void changeSpeed(float d)
	{
		speed += d;
		if (speed < 0)
			speed = 0;
		setChanged();
		updateInputs();
	}

	public void changeTorque(float d)
	{
		torque += d;
		if (torque < 0)
			torque = 0;
		setChanged();
		updateInputs();
	}

	private void updateInputs()
	{

	}
}
