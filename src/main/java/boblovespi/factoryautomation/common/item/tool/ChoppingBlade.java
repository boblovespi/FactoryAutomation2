package boblovespi.factoryautomation.common.item.tool;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class ChoppingBlade extends DiggerItem
{
	public ChoppingBlade(Tier t, Properties p)
	{
		super(t, FATags.Blocks.MINEABLE_WITH_CHOPPING_BLADE, p);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return ItemAbilities.DEFAULT_AXE_ACTIONS.contains(itemAbility);
	}
}
