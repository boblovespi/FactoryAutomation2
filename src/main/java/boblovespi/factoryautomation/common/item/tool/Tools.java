package boblovespi.factoryautomation.common.item.tool;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.ItemAbility;
import net.neoforged.neoforge.common.SimpleTier;
import net.neoforged.neoforge.common.Tags;

public class Tools
{
	public static final Tier BAD_FLINT_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_WOODEN_TOOL, 20, 4.5f, 1.5f,
			2, () -> Ingredient.of(Items.FLINT));
	public static final Tier FLINT_TIER = new SimpleTier(BlockTags.INCORRECT_FOR_WOODEN_TOOL, Tiers.WOOD.getUses(), Tiers.WOOD.getSpeed(), Tiers.WOOD.getAttackDamageBonus(),
			Tiers.WOOD.getEnchantmentValue(), () -> Ingredient.of(Items.FLINT));
	public static final Tier COPPER_TIER = new SimpleTier(FATags.Blocks.INCORRECT_FOR_COPPER_TOOL, 240, 3.5f, 1.5f, 5, () -> Ingredient.of(Tags.Items.INGOTS_COPPER));

	public static final ItemAbility MAKE_CHOPPING_BLOCK = ItemAbility.get("make_chopping_block");
}
