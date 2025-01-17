package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
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

		public static final TagKey<Item> SHARDS = item("shards");

		public static final TagKey<Item> COPPER_NUGGET = cItem("nuggets/copper");
		public static final TagKey<Item> COPPER_SHEET = cItem("plates/copper");
		public static final TagKey<Item> COPPER_ROD = cItem("rods/copper");
		public static final TagKey<Item> COPPER_GEAR = cItem("gears/copper");
		public static final TagKey<Item> TIN_INGOT = cItem("ingots/tin");
		public static final TagKey<Item> TIN_NUGGET = cItem("nuggets/tin");
		public static final TagKey<Item> TIN_BLOCK = cItem("storage_blocks/tin");
		public static final TagKey<Item> TIN_SHEET = cItem("plates/tin");
		public static final TagKey<Item> TIN_ROD = cItem("rods/tin");
		public static final TagKey<Item> TIN_GEAR = cItem("gears/tin");
		public static final TagKey<Item> IRON_SHEET = cItem("plates/iron");
		public static final TagKey<Item> IRON_ROD = cItem("rods/iron");
		public static final TagKey<Item> IRON_GEAR = cItem("gears/iron");
		public static final TagKey<Item> BRONZE_INGOT = cItem("ingots/bronze");
		public static final TagKey<Item> BRONZE_NUGGET = cItem("nuggets/bronze");
		public static final TagKey<Item> BRONZE_BLOCK = cItem("storage_blocks/bronze");
		public static final TagKey<Item> BRONZE_SHEET = cItem("plates/bronze");
		public static final TagKey<Item> BRONZE_ROD = cItem("rods/bronze");
		public static final TagKey<Item> BRONZE_GEAR = cItem("gears/bronze");
		public static final TagKey<Item> STEEL_INGOT = cItem("ingots/steel");
		public static final TagKey<Item> STEEL_NUGGET = cItem("nuggets/steel");
		public static final TagKey<Item> STEEL_BLOCK = cItem("storage_blocks/steel");
		public static final TagKey<Item> STEEL_SHEET = cItem("plates/steel");
		public static final TagKey<Item> STEEL_ROD = cItem("rods/steel");
		public static final TagKey<Item> STEEL_GEAR = cItem("gears/steel");

		public static final TagKey<Item> WOOD_GEAR = cItem("gears/wooden");

		public static final TagKey<Item> RAW_TIN = cItem("raw_materials/tin");
		public static final TagKey<Item> SHEETS = cItem("plates");
		public static final TagKey<Item> GEARS = cItem("gears");

		public static final TagKey<Item> WHEAT_DUST = cItem("dusts/flour");
		public static final TagKey<Item> CALCIUM_CARBONATE_DUST = cItem("dusts/calcium_carbonate");
		public static final TagKey<Item> QUICKLIME_DUST = cItem("dusts/quicklime");
	}

	public static class Blocks
	{
		public static final TagKey<Block> MINEABLE_WITH_CHOPPING_BLADE = mcBlock("mineable/chopping_blade");
		public static final TagKey<Block> MINEABLE_WITH_HAMMER = mcBlock("mineable/hammer");
		public static final TagKey<Block> MINEABLE_WITH_WRENCH = mcBlock("mineable/wrench");

		public static final TagKey<Block> TIN_BLOCK = cBlock("storage_blocks/tin");
		public static final TagKey<Block> BRONZE_BLOCK = cBlock("storage_blocks/bronze");
		public static final TagKey<Block> STEEL_BLOCK = cBlock("storage_blocks/steel");

		public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = block("incorrect_for_copper_tool");
		public static final TagKey<Block> NEEDS_COPPER_TOOL = block("needs_copper_tool");
		public static final TagKey<Block> INCORRECT_FOR_BRONZE_TOOL = block("incorrect_for_bronze_tool");
		public static final TagKey<Block> NEEDS_BRONZE_TOOL = block("needs_bronze_tool");
		public static final TagKey<Block> INCORRECT_FOR_STEEL_TOOL = block("incorrect_for_steel_tool");
		public static final TagKey<Block> NEEDS_STEEL_TOOL = block("needs_steel_tool");

		public static final TagKey<Block> CHOPPING_BLOCK_LOGS = block("chopping_block_logs");
		public static final TagKey<Block> CHOPPING_BLOCKS = block("chopping_blocks");
	}

	public static class Biomes
	{
		public static final TagKey<Biome> IS_TYPICAL_OVERWORLD = biome("is_typical_overworld");
		public static final TagKey<Biome> IS_YELLOW_DESERT_OVERWORLD = biome("is_yellow_desert_overworld");
		public static final TagKey<Biome> IS_RED_DESERT_OVERWORLD = biome("is_red_desert_overworld");
		public static final TagKey<Biome> IS_WET_OVERWORLD = biome("is_wet_overworld");
		public static final TagKey<Biome> IS_SURFACE_OVERWORLD = biome("is_surface_overworld");
	}

	private static TagKey<Item> mcItem(String name)
	{
		return TagKey.create(Registries.ITEM, ResourceLocation.withDefaultNamespace(name));
	}

	private static TagKey<Item> item(String name)
	{
		return TagKey.create(Registries.ITEM, FactoryAutomation.name(name));
	}

	private static TagKey<Item> cItem(String name)
	{
		return TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Block> mcBlock(String name)
	{
		return TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace(name));
	}

	private static TagKey<Block> block(String name)
	{
		return TagKey.create(Registries.BLOCK, FactoryAutomation.name(name));
	}

	private static TagKey<Block> cBlock(String name)
	{
		return TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", name));
	}

	private static TagKey<Biome> biome(String name)
	{
		return TagKey.create(Registries.BIOME, FactoryAutomation.name(name));
	}
}
