package boblovespi.factoryautomation;

import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.common.menu.MenuTypes;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.item.CreativeTabs;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.Workbench;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.FuelInfo;
import boblovespi.factoryautomation.common.worldgen.FAWorldgen;
import boblovespi.factoryautomation.data.loot.AlternateDropsLootModifier;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import java.util.List;

@Mod(FactoryAutomation.MODID)
public class FactoryAutomation
{
	public static final String MODID = "factoryautomation";
	public static final Logger LOGGER = LogUtils.getLogger();

	private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(
			NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AlternateDropsLootModifier>> ADD_TABLE_LOOT_MODIFIER_TYPE = GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(
			"alternate_drops", () -> AlternateDropsLootModifier.CODEC);

	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public FactoryAutomation(IEventBus modEventBus, ModContainer modContainer)
	{
		// Register the commonSetup method for mod loading
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::onRegisterCapabilities);
		modEventBus.addListener(this::onRegisterDataMapTypes);

		// Add all registrars to the event bus
		FABlocks.BLOCKS.register(modEventBus);
		FAItems.ITEMS.register(modEventBus);
		CreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		FASounds.SOUND_EVENTS.register(modEventBus);
		FAWorldgen.FEATURES.register(modEventBus);
		GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
		FABETypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		RecipeThings.RECIPE_TYPES.register(modEventBus);
		RecipeThings.RECIPE_SERIALIZERS.register(modEventBus);
		MenuTypes.TYPES.register(modEventBus);
		Multiblocks.register();

		// Register ourselves for server and other game events we are interested in.
		// Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
		// Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
		NeoForge.EVENT_BUS.register(this);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
	}

	public static ResourceLocation name(String location)
	{
		return ResourceLocation.fromNamespaceAndPath(MODID, location);
	}

	public static String locString(String prefix, String postfix)
	{
		return prefix + "." + MODID + "." + postfix;
	}

	private void commonSetup(final FMLCommonSetupEvent event)
	{
		//		// Some common setup code
		//		LOGGER.info("HELLO FROM COMMON SETUP");
		//
		//		if (Config.logDirtBlock)
		//			LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

		LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

		//		Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event)
	{
		// Do something when the server starts
		LOGGER.info("HELLO from server starting");

		var tags = List.of(FATags.Items.IRON_MELTABLE, FATags.Items.GOLD_MELTABLE);
		for (TagKey<Item> tag : tags)
		{
			LOGGER.info("Tag {}: ", tag.location());
			BuiltInRegistries.ITEM.getOrCreateTag(tag).stream().forEach(t -> LOGGER.info("\t- {}", t.getRegisteredName()));
		}
	}

	public void onRegisterCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(CastingCapability.BLOCK, FABETypes.STONE_CASTING_VESSEL_TYPE.get(), (b, d) -> b);
	}

	public void onRegisterDataMapTypes(RegisterDataMapTypesEvent event)
	{
		event.register(Workbench.PART_DATA);
		event.register(Workbench.TOOL_DATA);
		event.register(FuelInfo.FUEL_DATA);
	}
}
