package boblovespi.factoryautomation.common.item.tool;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.List;

public class FastTool extends TieredItem
{
	private final ItemAbility ability;

	public FastTool(Tier tier, TagKey<Block> mineables, float mult, ItemAbility ability, Properties properties)
	{
		super(tier, properties.component(DataComponents.TOOL,
				new Tool(List.of(Tool.Rule.deniesDrops(tier.getIncorrectBlocksForDrops()), Tool.Rule.minesAndDrops(mineables, mult * tier.getSpeed())), 1, 1)));

		this.ability = ability;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return itemAbility == ability;
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		return true;
	}

	@Override
	public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
		stack.hurtAndBreak(2, attacker, EquipmentSlot.MAINHAND);
	}
}
