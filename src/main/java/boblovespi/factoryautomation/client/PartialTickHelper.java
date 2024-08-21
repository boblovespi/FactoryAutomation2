package boblovespi.factoryautomation.client;

import net.minecraft.client.Minecraft;

public class PartialTickHelper
{
	// Thanks to create for this idea
	private static long tick = 0;

	public static void tick()
	{
		tick = (tick + 1) % 864_000;
	}

	public static float getPartialFor(int max)
	{
		return tick % max + Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
	}
}
