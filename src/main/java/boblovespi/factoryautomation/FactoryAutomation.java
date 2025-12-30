package boblovespi.factoryautomation;

import boblovespi.factoryautomation.api.capability.BellowsCapability;
import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.api.capability.HeatCapability;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import boblovespi.factoryautomation.common.FAAttachmentTypes;
import boblovespi.factoryautomation.common.FAParticleTypes;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.logistics.SmallTankBE;
import boblovespi.factoryautomation.common.blockentity.mechanical.*;
import boblovespi.factoryautomation.common.blockentity.processing.*;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.item.CreativeTabs;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.item.FluidBottle;
import boblovespi.factoryautomation.common.menu.MenuTypes;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import boblovespi.factoryautomation.common.potion.FAMobEffects;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.Workbench;
import boblovespi.factoryautomation.common.sound.FASounds;
import boblovespi.factoryautomation.common.util.FuelInfo;
import boblovespi.factoryautomation.common.worldgen.FAWorldgen;
import boblovespi.factoryautomation.data.loot.AlternateDropsLootModifier;
import boblovespi.factoryautomation.data.loot.LootTablePrefixCondition;
import boblovespi.factoryautomation.data.loot.ReplaceDropsLootModifier;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.Supplier;

@Mod(FactoryAutomation.MODID)
public class FactoryAutomation
{
	public static final String MODID = "factoryautomation";
	public static final Logger LOGGER = LogUtils.getLogger();

	private static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(
			NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

	private static final DeferredRegister<LootItemConditionType> LOOT_ITEM_CONDITION_TYPES = DeferredRegister.create(Registries.LOOT_CONDITION_TYPE, MODID);

	public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, MODID);

	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<AlternateDropsLootModifier>> ADD_TABLE_LOOT_MODIFIER_TYPE = GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(
			"alternate_drops", () -> AlternateDropsLootModifier.CODEC);
	public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<ReplaceDropsLootModifier>> REPLACE_LOOT_MODIFIER_TYPE = GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(
			"replace_drops", () -> ReplaceDropsLootModifier.CODEC);

	public static final Supplier<LootItemConditionType> LOOT_TABLE_PREFIX_CONDITION = LOOT_ITEM_CONDITION_TYPES.register("loot_table_prefix", () -> new LootItemConditionType(
			LootTablePrefixCondition.CODEC));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_CONTENT_DC = DATA_COMPONENTS.registerComponentType("fluid_contents",
			i -> i.networkSynchronized(SimpleFluidContent.STREAM_CODEC));

	public static final ModelProperty<Block[]> PARTIAL_DYNAMIC_TEXTURE_PROPERTY = new ModelProperty<>();

	// The constructor for the mod class is the first code that is run when your mod is loaded.
	// FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
	public FactoryAutomation(IEventBus modEventBus, ModContainer modContainer)
	{
		LOGGER.debug("Constructing FactoryAutomation class");
		NeoForgeMod.enableMilkFluid();

		// Register the commonSetup method for mod loading
		modEventBus.addListener(this::commonSetup);
		modEventBus.addListener(this::onRegisterCapabilities);
		modEventBus.addListener(this::onRegisterDataMapTypes);

		// Add all registrars to the event bus
		FABlocks.BLOCKS.register(modEventBus);
		FAItems.ITEMS.register(modEventBus);
		FAFluids.FLUID_TYPES.register(modEventBus);
		FAFluids.FLUIDS.register(modEventBus);
		CreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
		FASounds.SOUND_EVENTS.register(modEventBus);
		FAWorldgen.FEATURES.register(modEventBus);
		GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
		LOOT_ITEM_CONDITION_TYPES.register(modEventBus);
		DATA_COMPONENTS.register(modEventBus);
		FABETypes.BLOCK_ENTITY_TYPES.register(modEventBus);
		RecipeThings.RECIPE_TYPES.register(modEventBus);
		RecipeThings.RECIPE_SERIALIZERS.register(modEventBus);
		MenuTypes.TYPES.register(modEventBus);
		FAParticleTypes.TYPES.register(modEventBus);
		FAAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
		FAMobEffects.MOB_EFFECTS.register(modEventBus);
		Multiblocks.register();

		// Register ourselves for server and other game events we are interested in.
		// Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
		// Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
		NeoForge.EVENT_BUS.register(this);

		// Register our mod's ModConfigSpec so that FML can create and load the config file for us
		modContainer.registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
		modContainer.registerConfig(ModConfig.Type.SERVER, Config.SERVER_SPEC);
		modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
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

		// LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

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
			LOGGER.debug("Tag {}: ", tag.location());
			BuiltInRegistries.ITEM.getOrCreateTag(tag).stream().forEach(t -> LOGGER.debug("\t- {}", t.getRegisteredName()));
		}
	}

	public void onRegisterCapabilities(RegisterCapabilitiesEvent event)
	{
		event.registerBlockEntity(CastingCapability.BLOCK, FABETypes.STONE_CASTING_VESSEL_TYPE.get(), (b, d) -> b);
		event.registerBlockEntity(CastingCapability.BLOCK, FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), (b, d) -> b);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.CREATIVE_MECHANICAL_SOURCE_TYPE.get(), (b, d) -> b);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.POWER_SHAFT_TYPE.get(), PowerShaftBE::output);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.POWER_SHAFT_TYPE.get(), PowerShaftBE::input);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.GEARBOX_TYPE.get(), GearboxBE::output);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.GEARBOX_TYPE.get(), GearboxBE::input);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.SPLITTER_TYPE.get(), SplitterBE::output);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.SPLITTER_TYPE.get(), SplitterBE::input);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.JOINER_TYPE.get(), JoinerBE::output);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.JOINER_TYPE.get(), JoinerBE::input);
		event.registerBlockEntity(MechanicalCapability.OUTPUT, FABETypes.BEVEL_GEAR_TYPE.get(), BevelGearBE::output);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.BEVEL_GEAR_TYPE.get(), BevelGearBE::input);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.MILLSTONE_TYPE.get(), MillstoneBE::input);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.TUMBLING_BARREL_TYPE.get(), TumblingBarrelBE::input);
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.LEATHER_BELLOWS_TYPE.get(), LeatherBellowsBE::input);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FABETypes.MILLSTONE_TYPE.get(), MillstoneBE::itemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FABETypes.TUMBLING_BARREL_TYPE.get(), TumblingBarrelBE::itemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FABETypes.BRICK_FIREBOX_TYPE.get(), BrickFireboxBE::itemHandler);
		event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, FABETypes.STEAM_OVEN_TYPE.get(), SteamOvenBE::itemHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FABETypes.TUMBLING_BARREL_TYPE.get(), TumblingBarrelBE::fluidHandler);
		event.registerBlockEntity(BellowsCapability.BLOCK, FABETypes.MULTIBLOCK_PART_TYPE.get(), (b, d) -> b.getCapability(BellowsCapability.BLOCK, d));
		event.registerBlockEntity(MechanicalCapability.INPUT, FABETypes.MULTIBLOCK_PART_TYPE.get(), (b, d) -> b.getCapability(MechanicalCapability.INPUT, d));
		event.registerBlockEntity(HeatCapability.BLOCK, FABETypes.MULTIBLOCK_PART_TYPE.get(), (b, d) -> b.getCapability(HeatCapability.BLOCK, d));
		event.registerBlockEntity(HeatCapability.BLOCK, FABETypes.BRICK_FIREBOX_TYPE.get(), BrickFireboxBE::heatManager);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FABETypes.SMALL_TANK_TYPE.get(), SmallTankBE::fluidHandler);
		event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), BrickCastingVesselBE::fluidHandler);

		for (var item : BuiltInRegistries.ITEM)
			if (item instanceof FluidBottle fb)
				event.registerItem(Capabilities.FluidHandler.ITEM, (s, c) -> fb.makeHandler(s), fb);
	}

	public void onRegisterDataMapTypes(RegisterDataMapTypesEvent event)
	{
		event.register(Workbench.PART_DATA);
		event.register(Workbench.TOOL_DATA);
		event.register(FuelInfo.FUEL_DATA);
	}
}
