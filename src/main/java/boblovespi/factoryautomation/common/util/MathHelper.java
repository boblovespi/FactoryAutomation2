package boblovespi.factoryautomation.common.util;

import net.minecraft.util.RandomSource;

public class MathHelper
{
	public static double uniformZeroD(RandomSource random, double radius)
	{
		return (random.nextDouble() - 0.5) * 2 * radius;
	}

	public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

}
