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

	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		var choppingBlocks = FABlocks.CHOPPING_BLOCKS.values().stream().map(DeferredHolder::get).toArray(ChoppingBlock[]::new);

		tag(BlockTags.MINEABLE_WITH_SHOVEL).add(FABlocks.GREEN_SAND.get(), FABlocks.CHARCOAL_PILE.get());
		tag(BlockTags.MINEABLE_WITH_AXE).add(choppingBlocks).add(FABlocks.LOG_PILE.get());

		tag(FATags.Blocks.MINEABLE_WITH_CHOPPING_BLADE).addTag(BlockTags.MINEABLE_WITH_AXE).add(Blocks.SHORT_GRASS);
		tag(FATags.Blocks.CHOPPING_BLOCK_LOGS).add(Arrays.stream(WoodTypes.values()).map(WoodTypes::getLog).toArray(Block[]::new));
		tag(FATags.Blocks.CHOPPING_BLOCKS).add(choppingBlocks);
	}
}
