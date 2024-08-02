package boblovespi.factoryautomation.common.util;

import java.util.Optional;
import java.util.function.Function;

public abstract class CrucibleManager
{
	private int maxCapacity;

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
	 * @param metal The metal that is being melted.
	 * @param amount The amount of metal to melt.
	 *
	 * @return {@code true} if and only if the metal was successfully melted.
	 */
	public abstract boolean melt(Metal metal, int amount);
}
