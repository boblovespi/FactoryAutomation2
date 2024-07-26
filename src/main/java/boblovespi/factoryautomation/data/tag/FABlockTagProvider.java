package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
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

	}
}
