package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;

import javax.annotation.Nullable;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public abstract class CrucibleManager
{
	protected int maxCapacity;

	public CrucibleManager(String nbtId, int maxCapacity)
	{
		this.nbtId = nbtId;
		this.maxCapacity = maxCapacity;
	}

	protected String nbtId;

	/**
	 * Pour some metal out of the crucible. Usually used in conjunction with {@link ICastingVessel#cast(Function)}.
	 *
	 * @param amount The amount of metal to pour.
	 *
	 * @return The metal which was poured, if successful.
	 */
	public Optional<Metal> pour(int amount)
	{
		// Casting logic works as follows:
		//  1. crucible be looks for a suitable casting vessel
		//  2. if vessel exists, crucible calls ICastingVessel#cast, passing in its CrucibleManager#pour as the metal supplier
		//  3. casting vessel determines how much metal it needs, and requests it from crucible manager
		//  4. if casting vessel gets a metal, it forms the itemstack it desires, otherwise, it does nothing
		return Optional.empty();
	}


	/**
	 * Melt a metal into the crucible. Note: users should handle melt progress and heat elsewhere.
	 *
	 * @param metal  The metal that is being melted.
	 * @param amount The amount of metal to melt.
	 *
	 * @return The amount of metal was not successfully melted.
	 */
	public abstract int melt(Metal metal, int amount);

	public abstract void save(CompoundTag tag);

	public abstract void load(CompoundTag tag);

	public abstract Metal getCurrentMetal();

	public abstract int getAmount();

	public abstract float getHeatCapacity();

	public int getSpace()
	{
		return maxCapacity - getAmount();
	}

	public static class Single extends CrucibleManager
	{
		@Nullable
		private Metal metal;
		private int amount;

		public Single(String nbtId, int maxCapacity)
		{
			super(nbtId, maxCapacity);
		}

		@Override
		public int melt(Metal metal, int amount)
		{
			if (this.metal == null)
			{
				this.metal = metal;
				this.amount = Math.min(amount, maxCapacity);
				return amount - this.amount;
			}
			else if (this.metal == metal)
			{
				var moved = Math.min(amount, maxCapacity - this.amount);
				this.amount += moved;
				return moved;
			}
			else
				return amount;
		}

		@Override
		public void save(CompoundTag tag)
		{
			var nbt = new CompoundTag();
			nbt.putInt("amount", amount);
			nbt.putString("metal", metal == null ? "none" : metal.getName());
			tag.put(nbtId, nbt);
		}

		@Override
		public void load(CompoundTag tag)
		{
			var nbt = tag.getCompound(nbtId);
			amount = nbt.getInt("amount");
			var metalName = nbt.getString("metal");
			if (metalName.equals("none"))
				metal = null;
			else
				metal = Metal.fromName(metalName);
		}

		@Override
		public Metal getCurrentMetal()
		{
			return metal == null ? Metal.UNKNOWN : metal;
		}

		@Override
		public int getAmount()
		{
			return amount;
		}

		@Override
		public float getHeatCapacity()
		{
			if (amount == 0 || metal == null)
				return 0;
			return metal.massHeatCapacity() * metal.density() * amount / (Metal.UNITS_IN_INGOT * 9);
		}

		@Override
		public Optional<Metal> pour(int amount)
		{
			if (amount >= this.amount)
			{
				var ret = amount == this.amount ? metal : null;
				metal = null;
				this.amount = 0;
				return Optional.ofNullable(ret);
			}
			this.amount -= amount;
			return Optional.of(metal);
		}
	}

	public static class Multi extends CrucibleManager
	{
		private static final Codec<Map<Metal, Float>> RATIO_CODEC = Codec.unboundedMap(Codec.STRING.xmap(Metal::fromName, Metal::getName), Codec.FLOAT);
		private static final List<AlloyData> ALLOYS = List.of(AlloyData.BRONZE);
		private Map<Metal, Float> metalRatio; // must add up to 100, unless amount = 0
		private Metal currentMetal;
		private int amount;

		public Multi(String nbtId, int maxCapacity)
		{
			super(nbtId, maxCapacity);
			metalRatio = new IdentityHashMap<>(Metal.allMetals().size());
			currentMetal = Metal.UNKNOWN;
		}

		@Override
		public Optional<Metal> pour(int amount)
		{
			if (amount >= this.amount)
			{
				var ret = amount == this.amount ? currentMetal : null;
				this.amount = 0;
				metalRatio.entrySet().forEach(e -> e.setValue(0f));
				currentMetal = Metal.UNKNOWN;
				return Optional.ofNullable(ret);
			}
			this.amount -= amount;
			return Optional.of(currentMetal);
		}

		@Override
		public int melt(Metal metal, int amount)
		{
			var moved = Math.min(amount, maxCapacity - this.amount);
			metalRatio.putIfAbsent(metal, 0f);
			if (this.amount == 0)
			{
				metalRatio.put(metal, 100f);
				this.amount += moved;
				recomputeMetal();
				return moved;
			}
			var ratio = metalRatio.get(metal) / 100;
			var multFactor = ((float) this.amount) / (amount + this.amount);
			var newRatio = ratio * multFactor + 1 - multFactor;
			for (var entry : metalRatio.entrySet())
			{
				if (entry.getKey() == metal)
					entry.setValue(newRatio * 100f);
				else
					entry.setValue(entry.getValue() * multFactor);
			}

			recomputeMetal();

			this.amount += moved;
			return moved;
		}

		private void recomputeMetal()
		{
			// alloys of over 99.9% one metal are considered just that metal
			for (var entry : metalRatio.entrySet())
			{
				if (entry.getValue() > 99.9f)
				{
					currentMetal = entry.getKey();
					return;
				}
			}
			for (var alloy : ALLOYS)
			{
				var baseP = metalRatio.getOrDefault(alloy.base, 0f);
				// we don't have the base metal, so it can't be this alloy
				if (baseP == 0)
					continue;
				var success = true;
				for (var entry : metalRatio.entrySet())
				{
					// having the actual metal is always ok
					if (entry.getKey() == alloy.metal || entry.getKey() == alloy.base)
						continue;
					// extraneous metals are bad - if there is more than 1% of the extraneous metal we discard
					if (!alloy.lowerBound.containsKey(entry.getKey()) && entry.getValue() > 0 && entry.getValue() / baseP > 0.01f)
					{
						success = false;
						break;
					}
					// actually an alloy constituent
					if (alloy.lowerBound.containsKey(entry.getKey()))
					{
						var lb = alloy.lowerBound.get(entry.getKey());
						var ub = alloy.upperBound.get(entry.getKey());
						var ratio = entry.getValue() / baseP;
						// we exceed the alloy thresholds
						if (ratio < lb || ratio > ub)
						{
							success = false;
							break;
						}
					}
				}
				if (success)
				{
					currentMetal = alloy.metal;
					return;
				}
			}
			currentMetal = Metal.UNKNOWN;
		}

		@Override
		public void save(CompoundTag tag)
		{
			var nbt = new CompoundTag();
			nbt.putInt("amount", amount);
			nbt.put("metalRatios", RATIO_CODEC.encodeStart(NbtOps.INSTANCE, metalRatio).getOrThrow());
			tag.put(nbtId, nbt);
		}

		@Override
		public void load(CompoundTag tag)
		{
			var nbt = tag.getCompound(nbtId);
			amount = nbt.getInt("amount");
			RATIO_CODEC.decode(NbtOps.INSTANCE, nbt.get("metalRatios"))
					   .resultOrPartial(FactoryAutomation.LOGGER::warn)
					   .ifPresent(p -> metalRatio = new IdentityHashMap<>(p.getFirst()));
			recomputeMetal();
		}

		@Override
		public Metal getCurrentMetal()
		{
			return currentMetal;
		}

		@Override
		public int getAmount()
		{
			return amount;
		}

		@Override
		public float getHeatCapacity()
		{
			return 0;
		}

		private record AlloyData(Metal metal, Metal base, Map<Metal, Float> lowerBound, Map<Metal, Float> upperBound)
		{
			private static final AlloyData BRONZE = new AlloyData(Metal.BRONZE, Metal.TIN, Map.of(Metal.COPPER, 7f), Map.of(Metal.COPPER, 9f));
		}
	}
}
