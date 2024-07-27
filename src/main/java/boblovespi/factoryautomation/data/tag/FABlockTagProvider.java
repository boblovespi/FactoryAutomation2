package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
		tag(FATags.MINEABLE_WITH_CHOPPING_BLADE).addTag(BlockTags.MINEABLE_WITH_AXE).add(Blocks.SHORT_GRASS);
	}
}
