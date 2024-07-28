package boblovespi.factoryautomation.common.block.types;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.MapColor;

public enum WoodTypes
{
	OAK("oak", MapColor.WOOD),
	SPRUCE("spruce", MapColor.PODZOL),
	BIRCH("birch", MapColor.SAND),
	JUNGLE("jungle", MapColor.DIRT),
	ACACIA("acacia", MapColor.COLOR_ORANGE),
	DARK_OAK("dark_oak", MapColor.COLOR_BROWN),
	MANGROVE("mangrove", MapColor.COLOR_RED),
	CHERRY("cherry", MapColor.TERRACOTTA_WHITE);

	private final String name;
	private final MapColor color;

	WoodTypes(String name, MapColor color)
	{
		this.name = name;
		this.color = color;
	}

	public String getName()
	{
		return name;
	}

	public MapColor getColor()
	{
		return color;
	}

	public static WoodTypes fromLog(Block log)
	{
		return switch (log)
		{
			case Block ignored when log.defaultBlockState().is(BlockTags.OAK_LOGS) -> OAK;
			case Block ignored when log.defaultBlockState().is(BlockTags.SPRUCE_LOGS) -> SPRUCE;
			case Block ignored when log.defaultBlockState().is(BlockTags.BIRCH_LOGS) -> BIRCH;
			case Block ignored when log.defaultBlockState().is(BlockTags.JUNGLE_LOGS) -> JUNGLE;
			case Block ignored when log.defaultBlockState().is(BlockTags.ACACIA_LOGS) -> ACACIA;
			case Block ignored when log.defaultBlockState().is(BlockTags.DARK_OAK_LOGS) -> DARK_OAK;
			case Block ignored when log.defaultBlockState().is(BlockTags.MANGROVE_LOGS) -> MANGROVE;
			case Block ignored when log.defaultBlockState().is(BlockTags.CHERRY_LOGS) -> CHERRY;
			default -> OAK;
		};
	}

	public Block getLog()
	{
		return switch (this)
		{
			case OAK -> Blocks.OAK_LOG;
			case SPRUCE -> Blocks.SPRUCE_LOG;
			case BIRCH -> Blocks.BIRCH_LOG;
			case JUNGLE -> Blocks.JUNGLE_LOG;
			case ACACIA -> Blocks.ACACIA_LOG;
			case DARK_OAK -> Blocks.DARK_OAK_LOG;
			case MANGROVE -> Blocks.MANGROVE_LOG;
			case CHERRY -> Blocks.CHERRY_LOG;
		};
	}

	public Block getPlanks()
	{
		return switch (this)
		{
			case OAK -> Blocks.OAK_PLANKS;
			case SPRUCE -> Blocks.SPRUCE_PLANKS;
			case BIRCH -> Blocks.BIRCH_PLANKS;
			case JUNGLE -> Blocks.JUNGLE_PLANKS;
			case ACACIA -> Blocks.ACACIA_PLANKS;
			case DARK_OAK -> Blocks.DARK_OAK_PLANKS;
			case MANGROVE -> Blocks.MANGROVE_PLANKS;
			case CHERRY -> Blocks.CHERRY_PLANKS;
		};
	}

	public TagKey<Item> getLogsTag()
	{
		return switch (this)
		{
			case OAK -> ItemTags.OAK_LOGS;
			case SPRUCE -> ItemTags.SPRUCE_LOGS;
			case BIRCH -> ItemTags.BIRCH_LOGS;
			case JUNGLE -> ItemTags.JUNGLE_LOGS;
			case ACACIA -> ItemTags.ACACIA_LOGS;
			case DARK_OAK -> ItemTags.DARK_OAK_LOGS;
			case MANGROVE -> ItemTags.MANGROVE_LOGS;
			case CHERRY -> ItemTags.CHERRY_LOGS;
		};
	}
}
