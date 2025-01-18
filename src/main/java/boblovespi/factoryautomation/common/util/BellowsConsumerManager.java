package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.IBellowsConsumer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

public class BellowsConsumerManager implements IBellowsConsumer
{
	private final String nbtId;
	private final float baseEfficiency;
	private float efficiency;
	private int blowTime;
	private int maxBlowTime;

	public BellowsConsumerManager(String nbtId, float baseEfficiency)
	{
		this.nbtId = nbtId;
		this.baseEfficiency = baseEfficiency;
	}

	@Override
	public void blow(float efficiency, int time)
	{
		if (efficiency > this.efficiency)
		{
			this.efficiency = efficiency;
			this.maxBlowTime = this.blowTime = time;
		}
		else if (efficiency == this.efficiency && time > this.blowTime)
			this.maxBlowTime = this.blowTime = time;
	}

	public void progress()
	{
		if (blowTime > 0)
		{
			blowTime--;
		}
		if (blowTime == 0)
		{
			efficiency = baseEfficiency;
			maxBlowTime = 1;
		}
	}

	public void save(CompoundTag tag)
	{
		var nbt = new CompoundTag();
		nbt.putInt("time", blowTime);
		nbt.putInt("maxBlowTime", maxBlowTime);
		nbt.putFloat("efficiency", efficiency);
		tag.put(nbtId, nbt);
	}

	public void load(CompoundTag tag)
	{
		var nbt = tag.getCompound(nbtId);
		blowTime = nbt.getInt("time");
		maxBlowTime = nbt.getInt("maxBlowTime");
		efficiency = nbt.getFloat("efficiency");
	}

	public float getTempEfficiency()
	{
		return Mth.clamp(efficiency, 0, 1);
	}

	public float getEnergyEfficiency()
	{
		return efficiency / 2;
	}

	public float getTime()
	{
		return (float) blowTime / maxBlowTime;
	}
}
