package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.Workbench;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.concurrent.CompletableFuture;

public class FADataMapProvider extends DataMapProvider
{
	public FADataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather()
	{
		builder(Workbench.PART_DATA).add(FAItems.SCREW, new Workbench.Part(FactoryAutomation.name("screw"), 1), false);
		builder(Workbench.TOOL_DATA).add(FAItems.COPPER_HAMMER, new Workbench.Tool(FactoryAutomation.name("hammer"), 1), false);
	}
}
