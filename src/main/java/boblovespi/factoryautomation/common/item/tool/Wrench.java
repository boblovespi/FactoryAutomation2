package boblovespi.factoryautomation.common.item.tool;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbility;

public class Wrench extends DiggerItem
{
	public Wrench(Tier pTier, Properties pProperties)
	{
		super(pTier, FATags.Blocks.MINEABLE_WITH_WRENCH, pProperties);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return itemAbility == Tools.WRENCH;
	}
}
