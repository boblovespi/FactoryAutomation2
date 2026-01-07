package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = FactoryAutomation.MODID)
public class ServerHandler
{
	@SubscribeEvent
	public static void onServerAboutToStart(ServerAboutToStartEvent event)
	{
		FactoryAutomation.LOGGER.debug("server is about to start");
		var processorListRegistry = event.getServer().registryAccess().registry(Registries.PROCESSOR_LIST).orElseThrow();
		addRuleToList(ProcessorLists.FARM_SAVANNA.location(), makeCropRule(0.8f, FABlocks.SOYBEAN), processorListRegistry);
		addRuleToList(ProcessorLists.FARM_PLAINS.location(), makeCropRule(0.5f, FABlocks.SOYBEAN), processorListRegistry);
	}

	private static RuleProcessor makeCropRule(float probability, DeferredBlock<?> crop)
	{
		return new RuleProcessor(
				List.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.WHEAT, probability), AlwaysTrueTest.INSTANCE, crop.get().defaultBlockState())));
	}

	private static void addRuleToList(ResourceLocation list, StructureProcessor rule, Registry<StructureProcessorList> registry)
	{
		registry.getOptional(list).ifPresentOrElse(
				l -> {
					var mut = new ArrayList<>(l.list());
					mut.add(rule);
					l.list = mut;
				},
				() -> FactoryAutomation.LOGGER.warn("couldn't modify unknown processor list {}", list)
												  );
	}
}
