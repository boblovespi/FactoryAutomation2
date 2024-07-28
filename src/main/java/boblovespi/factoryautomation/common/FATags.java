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
	}

	public static class Blocks
	{
		public static final TagKey<Block> MINEABLE_WITH_CHOPPING_BLADE = mcBlock("mineable/chopping_blade");

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
