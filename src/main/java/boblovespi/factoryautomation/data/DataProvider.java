package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FADamageTypes;
import boblovespi.factoryautomation.data.loot.FAGlobalLootModifierProvider;
import boblovespi.factoryautomation.data.loot.FALootTableProvider;
import boblovespi.factoryautomation.data.tag.FABiomeTagProvider;
import boblovespi.factoryautomation.data.tag.FABlockTagProvider;
import boblovespi.factoryautomation.data.tag.FADamageTypeTagProvider;
import boblovespi.factoryautomation.data.tag.FAItemTagProvider;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Set;

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
		gen.addProvider(event.includeClient(), new FAParticleDescriptionProvider(output, efh));

		gen.addProvider(event.includeServer(), new FALootTableProvider(output, lookupProvider));
		gen.addProvider(event.includeServer(), new FAGlobalLootModifierProvider(output, lookupProvider));
		gen.addProvider(event.includeServer(), new FARecipeProvider(output, lookupProvider));
		gen.addProvider(event.includeServer(), new FAAdvancementProvider(output, lookupProvider, efh));
		// gen.addProvider(event.includeServer(), new FAWorldgenProvider(output, lookupProvider));
		gen.addProvider(event.includeServer(), new FADataMapProvider(output, lookupProvider));

		var rsb = new RegistrySetBuilder();
		FAWorldgenProvider.registrySet(rsb);
		rsb.add(Registries.DAMAGE_TYPE, DataProvider::addDamageTypes);
		var builtinDatapack = new DatapackBuiltinEntriesProvider(output, lookupProvider, rsb, Set.of(FactoryAutomation.MODID));
		gen.addProvider(event.includeServer(), builtinDatapack);

		var blockTags = new FABlockTagProvider(output, lookupProvider, efh);
		gen.addProvider(event.includeServer(), blockTags);
		gen.addProvider(event.includeServer(), new FAItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), efh));
		gen.addProvider(event.includeServer(), new FABiomeTagProvider(output, lookupProvider, efh));
		gen.addProvider(event.includeServer(), new FADamageTypeTagProvider(output, builtinDatapack.getRegistryProvider(), efh));
	}

	private static void addDamageTypes(BootstrapContext<DamageType> bootstrap)
	{
		bootstrap.register(FADamageTypes.METAL_TOO_HOT,
				new DamageType(FADamageTypes.METAL_TOO_HOT.location().getPath(), DamageScaling.NEVER, 0.1f, DamageEffects.BURNING, DeathMessageType.DEFAULT)
						  );
		bootstrap.register(FADamageTypes.PUNCHING_WOOD,
				new DamageType(FADamageTypes.PUNCHING_WOOD.location().getPath(), DamageScaling.NEVER, 0.1f, DamageEffects.HURT, DeathMessageType.DEFAULT)
						  );
	}
}
