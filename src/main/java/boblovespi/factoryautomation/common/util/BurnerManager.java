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
				maxBurnTime = burnTime = 1600; // TODO: replace with registry lookup
				burnEnergy = 4_300_000_000f;
				burnTemp = 2172;
				fuelExtractor.take();
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
		nbt.putInt("burnTime", burnTime);
		nbt.putInt("maxBurnTime", maxBurnTime);
		nbt.putFloat("burnEnergy", burnEnergy);
		nbt.putFloat("burnTemp", burnTemp);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		burnTime = nbt.getInt("burnTime");
		maxBurnTime = nbt.getInt("maxBurnTime");
		burnEnergy = nbt.getFloat("burnEnergy");
		burnTemp = nbt.getFloat("burnTemp");
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
		void take();
	}

	@FunctionalInterface
	public interface HeatAcceptor
	{
		void heat(float temp, float joules);
	}
}
