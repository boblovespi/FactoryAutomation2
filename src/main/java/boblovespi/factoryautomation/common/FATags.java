package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class FATags
{
	public static class Items
	{
		public static final TagKey<Item> SILKS_GRASS = item("tools/silks_grass");
		public static final TagKey<Item> GOOD_AXES = item("tools/good_axes");

		public static final TagKey<Item> CHOPPING_BLOCKS = item("chopping_blocks");

		public static final TagKey<Item> IRON_MELTABLE = item("meltables/iron");
		public static final TagKey<Item> GOLD_MELTABLE = item("meltables/gold");
		public static final TagKey<Item> COPPER_MELTABLE = item("meltables/copper");
		public static final TagKey<Item> TIN_MELTABLE = item("meltables/tin");
		public static final TagKey<Item> BRONZE_MELTABLE = item("meltables/bronze");
		public static final TagKey<Item> STEEL_MELTABLE = item("meltables/steel");
	}

	public static class Blocks
	{
		public static final TagKey<Block> MINEABLE_WITH_CHOPPING_BLADE = mcBlock("mineable/chopping_blade");

		public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = block("incorrect_for_copper_tool");
		public static final TagKey<Block> NEEDS_COPPER_TOOL = block("needs_copper_tool");
		public static final TagKey<Block> INCORRECT_FOR_BRONZE_TOOL = block("incorrect_for_bronze_tool");
		public static final TagKey<Block> NEEDS_BRONZE_TOOL = block("needs_bronze_tool");
		public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = block("incorrect_for_steel_tool");
		public static final TagKey<Block> NEEDS_STEEL_TOOL = block("needs_steel_tool");

		public static final TagKey<Block> CHOPPING_BLOCK_LOGS = block("chopping_block_logs");
		public static final TagKey<Block> CHOPPING_BLOCKS = block("chopping_blocks");
	}

	private static TagKey<Item> mcItem(String name)
	{
		return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name));
	}

	private static TagKey<Item> item(String name)
	{
		return TagKey.create(Registries.ITEM, FactoryAutomation.name(name));
	}

	private static TagKey<Block> mcBlock(String name)
	{
		return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name));
	}

	private static TagKey<Block> block(String name)
	{
		return TagKey.create(Registries.BLOCK, FactoryAutomation.name(name));
	}
}
