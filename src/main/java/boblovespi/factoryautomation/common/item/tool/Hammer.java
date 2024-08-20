package boblovespi.factoryautomation.common.item.tool;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class Hammer extends DiggerItem
{
	public Hammer(Tier t, Properties p)
	{
		super(t, FATags.Blocks.MINEABLE_WITH_HAMMER, p);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return itemAbility == ItemAbilities.PICKAXE_DIG;
	}
}
