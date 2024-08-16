package boblovespi.factoryautomation.common.util;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.Nullable;
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
}
