package boblovespi.factoryautomation.common.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class BurnerManager
{
	private final String nbtId;
	private final FuelFinder fuelFinder;
	private final FuelExtractor fuelExtractor;
	private int burnTime;

	public BurnerManager(String nbtId, FuelFinder fuelFinder, FuelExtractor fuelExtractor)
	{
		this.nbtId = nbtId;
		this.fuelFinder = fuelFinder;
		this.fuelExtractor = fuelExtractor;
	}

	public void progress()
	{
		if (burnTime > 0)
			burnTime--;
		if (burnTime == 0)
		{
			var fuel = fuelFinder.nextFuel();
			if (!fuel.isEmpty())
			{
				burnTime = 20; // TODO: replace with registry lookup
				fuelExtractor.take();
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
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		burnTime = nbt.getInt("burnTime");
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
}
