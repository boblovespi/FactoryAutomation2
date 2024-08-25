package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Function;

public class MechanicalManager implements IMechanicalOutput, IMechanicalInput
{
	private final String nbtId;
	private final Function<Float, Float> speedTransformer;
	private final Function<Float, Float> torqueTransformer;
	private final Updater onUpdate;
	private float speed;
	private float torque;

	public MechanicalManager(String nbtId, Function<Float, Float> speedTransformer, Function<Float, Float> torqueTransformer, Updater onUpdate)
	{
		this.nbtId = nbtId;
		this.speedTransformer = speedTransformer;
		this.torqueTransformer = torqueTransformer;
		this.onUpdate = onUpdate;
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getTorque()
	{
		return torque;
	}

	public void save(CompoundTag tag)
	{
		var nbt = new CompoundTag();
		nbt.putFloat("speed", speed);
		nbt.putFloat("torque", torque);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		speed = nbt.getFloat("speed");
		torque = nbt.getFloat("torque");
	}

	@Override
	public void update(IMechanicalOutput output)
	{
		speed = speedTransformer.apply(output.getSpeed());
		torque = torqueTransformer.apply(output.getTorque());
		onUpdate.update();
	}

	@FunctionalInterface
	public interface Updater
	{
		void update();
	}
}
