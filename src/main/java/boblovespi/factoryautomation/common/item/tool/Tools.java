package boblovespi.factoryautomation.common.item.tool;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public class Tools
{
	public static final Tier BAD_FLINT_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 20, 4.5f, 1.5f,
			2, () -> Ingredient.of(Items.FLINT));
	public static final Tier FLINT_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_WOODEN_TOOL, Tiers.WOOD.getUses(), Tiers.WOOD.getSpeed(), Tiers.WOOD.getAttackDamageBonus(),
			Tiers.WOOD.getEnchantmentValue(), () -> Ingredient.of(Items.FLINT));
}
