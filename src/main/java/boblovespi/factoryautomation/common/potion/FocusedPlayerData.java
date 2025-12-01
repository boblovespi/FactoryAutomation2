package boblovespi.factoryautomation.common.potion;

public class FocusedPlayerData
{
	private static final int MAX_TICKS = 20 * 5 / 4;
	private static final int MAX_BLOCKS_BROKEN = 5;
	private int blocksBroken;
	private int lastBlockBrokenCounter;

	public void breakBlock()
	{
		lastBlockBrokenCounter = MAX_TICKS;
		blocksBroken++;
	}

	public void tick()
	{
		if (lastBlockBrokenCounter > 0)
			lastBlockBrokenCounter--;
		if (lastBlockBrokenCounter == 0)
			blocksBroken = 0;
	}

	public void reset()
	{
		blocksBroken = 0;
		lastBlockBrokenCounter = 0;
	}

	public float getBlockBrokenCount(int amplifier)
	{
		return Math.min(blocksBroken, (amplifier + 1) * MAX_BLOCKS_BROKEN);
	}
}
