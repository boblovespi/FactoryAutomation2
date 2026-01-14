package boblovespi.factoryautomation.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

public class MathHelper
{
	// primes from wolfram alpha
	private static final int PRIME1 = 201620423;
	private static final int PRIME2 = 503376191;
	private static final int PRIME3 = 448223089;

	public static double uniformZeroD(RandomSource random, double radius)
	{
		return (random.nextDouble() - 0.5) * 2 * radius;
	}

	public static double map(double x, double in_min, double in_max, double out_min, double out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

	public static double easeInOutBack(double x) {
		double c1 = 1.70158;
		double c2 = c1 * 1.525;

		if (x < 0.5) {
			return (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2;
		} else {
			return (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (2 * x - 2) + c2) + 2) / 2;
		}
	}

	public static double smoothInterpolate(double prevStep, double nextStep, double step) {
		//Clamp so step is always betwwen 0-1
		if (step < 0.0) {
			step = 0.0;
		} else if (step > 1.0) {
			step = 1.0;
		}

		var smoothT = step * step * (3 - 2 * step);
		return prevStep + (nextStep - prevStep) * smoothT;
	}

	public static int colorFromBlockPos(BlockPos pos)
	{
		// mix bits a little bit
		var x = pos.getX();
		x ^= x >>> 16;
		var y = pos.getY();
		y ^= y >>> 16;
		var z = pos.getZ();
		z ^= z >>> 16;
		var color = x * PRIME1 + y * PRIME2 + z * PRIME3;
		return color | 0xFF000000;
	}

	public static boolean crossesThreshold(float oldVal, float newVal, float mod)
	{
		while (oldVal >= 0)
		{
			if (oldVal < mod && newVal >= mod)
				return true;
			oldVal -= mod;
			newVal -= mod;
		}
		return false;
	}

	public static float normalizeAngle(float radians)
	{
		radians = radians % (2 * Mth.PI);
		if (radians < 0)
			radians += 2 * Mth.PI;
		return radians;
	}

	public static boolean isAngleBetween(float angle, float min, float max)
	{
		angle = normalizeAngle(angle);
		min = normalizeAngle(min);
		max = normalizeAngle(max);
		if (min > max)
			return min <= angle || angle <= max;
		else
			return min <= angle && angle <= max;
	}
}
