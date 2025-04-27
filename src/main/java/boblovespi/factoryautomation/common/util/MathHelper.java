package boblovespi.factoryautomation.common.util;

import net.minecraft.util.RandomSource;

public class MathHelper
{
	public static double uniformZeroD(RandomSource random, double radius)
	{
		return (random.nextDouble() - 0.5) * 2 * radius;
	}
}
