package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.data.tag.FABlockTagProvider;
import boblovespi.factoryautomation.data.tag.FAItemTagProvider;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static boblovespi.factoryautomation.FactoryAutomation.LOGGER;

@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD)
public class DataProvider
{
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event)
	{
		LOGGER.debug("Generating data...");
		var gen = event.getGenerator();
		var output = gen.getPackOutput();
		var efh = event.getExistingFileHelper();
		var lookupProvider = event.getLookupProvider();
		gen.addProvider(event.includeClient(), new FABlockStateProvider(output, efh));
		gen.addProvider(event.includeClient(), new FAItemModelProvider(output, efh));
		gen.addProvider(event.includeClient(), new FASoundDefinitionProvider(output, efh));

		gen.addProvider(event.includeServer(), new FALootTableProvider(output, lookupProvider));
		gen.addProvider(event.includeServer(), new FARecipeProvider(output, lookupProvider));

		var blockTags = new FABlockTagProvider(output, lookupProvider, efh);
		gen.addProvider(event.includeServer(), blockTags);
		gen.addProvider(event.includeServer(), new FAItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), efh));
	}
}
