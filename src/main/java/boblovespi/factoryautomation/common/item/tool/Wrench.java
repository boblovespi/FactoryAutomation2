package boblovespi.factoryautomation.common.item.tool;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.world.item.Tier;

public class Wrench extends FastTool
{
	public Wrench(Tier pTier, Properties properties)
	{
		super(pTier, FATags.Blocks.MINEABLE_WITH_WRENCH, 3, Tools.WRENCH, properties);
	}
}
