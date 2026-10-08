package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.IHeatUser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;

public class HeatManager implements IHeatUser
{
	private final String nbtId;
	private float temperature;
	private float heatCapacity;
	private float conductivity;
	private float contactParameter;
	private float emissivity;

	public HeatManager(String nbtId, float heatCapacity, float conductivity, float contactParameter, float emissivity)
	{
		this.nbtId = nbtId;
		this.heatCapacity = heatCapacity;
		this.conductivity = conductivity;
		this.contactParameter = contactParameter;
		this.emissivity = emissivity;
		temperature = 300;
	}

	public void heat(float energy)
	{
		temperature += energy / heatCapacity;
		if (temperature < 0)
			temperature = 0;
	}

	@Override
	public boolean canConvect(BlockPos pos, Direction face)
	{
		return true;
	}

	@Override
	public float getConvectionSpeed(Direction face)
	{
		return 0;
	}

	public float cool(float energy)
	{
		var maxCooling = Math.min(energy / heatCapacity, temperature);
		temperature -= maxCooling;
		return maxCooling * heatCapacity;
	}

	public void save(CompoundTag tag)
	{
		var nbt = new CompoundTag();
		nbt.putFloat("temperature", temperature);
		nbt.putFloat("heatCapacity", heatCapacity);
		nbt.putFloat("conductivity", conductivity);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		temperature = nbt.getFloat("temperature");
		heatCapacity = nbt.getFloat("heatCapacity");
		conductivity = nbt.getFloat("conductivity");
	}

	public float getTemperature()
	{
		return temperature;
	}

	public float getHeatCapacity()
	{
		return heatCapacity;
	}

	@Override
	public float getConductivity()
	{
		return conductivity;
	}

	@Override
	public float getContactParameter()
	{
		return contactParameter;
	}

	@Override
	public float getEmissivity()
	{
		return emissivity;
	}

	public void increaseHeatCapacity(float shc)
	{
		heatCapacity += shc;
		if (heatCapacity < 0)
			heatCapacity = 0;
	}

	public void setHeatCapacity(float shc)
	{
		heatCapacity = shc;
		if (heatCapacity < 0)
			heatCapacity = 0;
	}
}
