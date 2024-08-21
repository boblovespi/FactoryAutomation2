package boblovespi.factoryautomation.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BurnerManager
{
	private final String nbtId;
	private final FuelFinder fuelFinder;
	private final FuelExtractor fuelExtractor;
	private final HeatAcceptor heatAcceptor;
	private int burnTime;
	private int maxBurnTime;
	private float burnEnergy;
	private float burnTemp;

	public BurnerManager(String nbtId, FuelFinder fuelFinder, FuelExtractor fuelExtractor, HeatAcceptor heatAcceptor)
	{
		this.nbtId = nbtId;
		this.fuelFinder = fuelFinder;
		this.fuelExtractor = fuelExtractor;
		this.heatAcceptor = heatAcceptor;
	}

	public void progress()
	{
		if (burnTime > 0)
		{
			burnTime--;
			heatAcceptor.heat(burnTemp, burnEnergy / maxBurnTime);
		}
		if (burnTime == 0)
		{
			var fuel = fuelFinder.nextFuel();
			if (!fuel.isEmpty())
			{
				var fuelInfo = fuelExtractor.take();
				maxBurnTime = burnTime = fuelInfo.time();
				burnEnergy = fuelInfo.energy();
				burnTemp = fuelInfo.temp();
			}
			else
			{
				maxBurnTime = 1;
				burnTime = 0;
				burnTemp = 0;
				burnEnergy = 0;
			}
		}
	}

	public boolean isBurning()
	{
		return burnTime > 0;
	}

	public void save(CompoundTag tag)
	{
		var nbt = new CompoundTag();
		nbt.putInt("time", burnTime);
		nbt.putInt("maxBurnTime", maxBurnTime);
		nbt.putFloat("burnEnergy", burnEnergy);
		nbt.putFloat("temp", burnTemp);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		burnTime = nbt.getInt("time");
		maxBurnTime = nbt.getInt("maxBurnTime");
		burnEnergy = nbt.getFloat("burnEnergy");
		burnTemp = nbt.getFloat("temp");
	}

	public float getBurnTime()
	{
		return burnTime / (float) maxBurnTime;
	}

	@FunctionalInterface
	public interface FuelFinder
	{
		ItemStack nextFuel();
	}

	@FunctionalInterface
	public interface FuelExtractor
	{
		FuelInfo take();
	}

	@FunctionalInterface
	public interface HeatAcceptor
	{
		void heat(float temp, float joules);
	}
}
