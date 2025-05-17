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
}
