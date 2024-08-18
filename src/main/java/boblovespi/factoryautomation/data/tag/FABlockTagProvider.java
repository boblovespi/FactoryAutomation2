package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.ChoppingBlock;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class FABlockTagProvider extends BlockTagsProvider
{
	public FABlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh)
	{
		super(output, lookupProvider, FactoryAutomation.MODID, efh);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		var choppingBlocks = FABlocks.CHOPPING_BLOCKS.values().stream().map(DeferredHolder::get).toArray(ChoppingBlock[]::new);

		// vanilla overrides
		tag(FATags.Blocks.NEEDS_STEEL_TOOL).add(Blocks.ANCIENT_DEBRIS, Blocks.NETHERITE_BLOCK);
		tag(FATags.Blocks.INCORRECT_FOR_STEEL_TOOL).addTag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL).remove(FATags.Blocks.NEEDS_STEEL_TOOL);
		tag(BlockTags.NEEDS_DIAMOND_TOOL).addTag(BlockTags.GOLD_ORES).remove(FATags.Blocks.NEEDS_STEEL_TOOL);
		tag(BlockTags.INCORRECT_FOR_DIAMOND_TOOL).addTag(FATags.Blocks.INCORRECT_FOR_BRONZE_TOOL).remove(BlockTags.NEEDS_DIAMOND_TOOL);
		tag(FATags.Blocks.NEEDS_BRONZE_TOOL).addTags(BlockTags.DIAMOND_ORES, BlockTags.EMERALD_ORES);
		tag(FATags.Blocks.INCORRECT_FOR_BRONZE_TOOL).addTag(BlockTags.INCORRECT_FOR_IRON_TOOL).remove(FATags.Blocks.NEEDS_BRONZE_TOOL);
		tag(BlockTags.NEEDS_IRON_TOOL).add(Blocks.DEEPSLATE, Blocks.DEEPSLATE_BRICKS, Blocks.DEEPSLATE_BRICK_SLAB, Blocks.DEEPSLATE_BRICK_STAIRS, Blocks.DEEPSLATE_BRICK_WALL)
									  .add(Blocks.CRACKED_DEEPSLATE_BRICKS, Blocks.DEEPSLATE_TILES, Blocks.DEEPSLATE_TILE_SLAB, Blocks.DEEPSLATE_TILE_STAIRS)
									  .add(Blocks.DEEPSLATE_TILE_WALL, Blocks.POLISHED_DEEPSLATE, Blocks.POLISHED_DEEPSLATE_SLAB, Blocks.POLISHED_DEEPSLATE_STAIRS)
									  .add(Blocks.POLISHED_DEEPSLATE_WALL, Blocks.CHISELED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE, Blocks.COBBLED_DEEPSLATE_SLAB)
									  .add(Blocks.COBBLED_DEEPSLATE_STAIRS, Blocks.COBBLED_DEEPSLATE_WALL, Blocks.INFESTED_DEEPSLATE, Blocks.CRACKED_DEEPSLATE_TILES)
									  .add(Blocks.DEEPSLATE_COAL_ORE, Blocks.DEEPSLATE_COPPER_ORE)
									  .addTags(BlockTags.IRON_ORES, BlockTags.GOLD_ORES)
									  .remove(FATags.Blocks.NEEDS_BRONZE_TOOL).remove(BlockTags.NEEDS_DIAMOND_TOOL);
		tag(BlockTags.INCORRECT_FOR_IRON_TOOL).addTag(FATags.Blocks.INCORRECT_FOR_COPPER_TOOL).remove(BlockTags.NEEDS_IRON_TOOL);
		tag(FATags.Blocks.NEEDS_COPPER_TOOL).add(FABlocks.TIN_BLOCK.get());
		tag(FATags.Blocks.INCORRECT_FOR_COPPER_TOOL).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL).remove(FATags.Blocks.NEEDS_COPPER_TOOL);
		tag(BlockTags.NEEDS_STONE_TOOL);
		tag(BlockTags.INCORRECT_FOR_STONE_TOOL).addTag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).remove(BlockTags.NEEDS_STONE_TOOL);
		tag(BlockTags.INCORRECT_FOR_WOODEN_TOOL).addTags(FATags.Blocks.NEEDS_STEEL_TOOL, BlockTags.NEEDS_DIAMOND_TOOL, FATags.Blocks.NEEDS_BRONZE_TOOL, BlockTags.NEEDS_IRON_TOOL,
				FATags.Blocks.NEEDS_COPPER_TOOL, BlockTags.NEEDS_STONE_TOOL);
		tag(BlockTags.INCORRECT_FOR_GOLD_TOOL).addTags(BlockTags.INCORRECT_FOR_WOODEN_TOOL);


		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(FABlocks.GREEN_SAND.get(), FABlocks.CHARCOAL_PILE.get());
		tag(BlockTags.MINEABLE_WITH_PICKAXE).add(FABlocks.STONE_CRUCIBLE.get(), FABlocks.STONE_CASTING_VESSEL.get(), FABlocks.TIN_BLOCK.get());
		tag(BlockTags.MINEABLE_WITH_AXE).add(choppingBlocks).add(FABlocks.LOG_PILE.get());

		tag(Tags.Blocks.STORAGE_BLOCKS).addTags(FATags.Blocks.TIN_BLOCK);
		tag(FATags.Blocks.TIN_BLOCK).add(FABlocks.TIN_BLOCK.get());

		tag(FATags.Blocks.MINEABLE_WITH_CHOPPING_BLADE).addTag(BlockTags.MINEABLE_WITH_AXE).add(Blocks.SHORT_GRASS);
		tag(FATags.Blocks.CHOPPING_BLOCK_LOGS).add(Arrays.stream(WoodTypes.values()).map(WoodTypes::getLog).toArray(Block[]::new));
		tag(FATags.Blocks.CHOPPING_BLOCKS).add(choppingBlocks);
	}
}
