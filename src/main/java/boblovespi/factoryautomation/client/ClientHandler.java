package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.*;
import boblovespi.factoryautomation.client.gui.*;
import boblovespi.factoryautomation.client.model.PartialDynamicTextureGeometryLoader;
import boblovespi.factoryautomation.common.FAParticleTypes;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.processing.MillstoneBE;
import boblovespi.factoryautomation.common.blockentity.processing.TripHammerBE;
import boblovespi.factoryautomation.common.blockentity.processing.TumblingBarrelBE;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.menu.MenuTypes;
import boblovespi.factoryautomation.common.util.ponder.FAPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import software.bernie.geckolib.loading.math.MolangQueries;

@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		// Some client setup code
		FactoryAutomation.LOGGER.info("Setting up client...");
		FactoryAutomation.LOGGER.info("Minecraft username is {}", Minecraft.getInstance().getUser().getName());
		// MathParser.registerVariable(new Variable("query.rot", 0));
		MolangQueries.<MillstoneBE>setActorVariable("query.rot", b -> b.animatable().getRenderRot(b.animationState().getPartialTick()));
		MolangQueries.<TumblingBarrelBE>setActorVariable("query.barrel_rot", b -> b.animatable().getRenderRot(b.animationState().getPartialTick()));
		MolangQueries.<TripHammerBE>setActorVariable("query.hammer_input", b -> b.animatable().getRenderInputRot(b.animationState().getPartialTick()));
		MolangQueries.<TripHammerBE>setActorVariable("query.hammer_tool", b -> b.animatable().getRenderToolRot(b.animationState().getPartialTick()));

		PonderIndex.addPlugin(new FAPonderPlugin());
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(FABETypes.CHOPPING_BLOCK_TYPE.get(), ChoppingBlockBER::new);
		event.registerBlockEntityRenderer(FABETypes.STONE_CASTING_VESSEL_TYPE.get(), StoneCastingVesselBER::new);
		event.registerBlockEntityRenderer(FABETypes.STONE_CRUCIBLE_TYPE.get(), StoneCrucibleBER::new);
		event.registerBlockEntityRenderer(FABETypes.BRICK_CRUCIBLE_TYPE.get(), BrickCrucibleBER::new);
		event.registerBlockEntityRenderer(FABETypes.BRICK_CASTING_VESSEL_TYPE.get(), BrickCastingVesselBER::new);
		// event.registerBlockEntityRenderer(FABETypes.BRICK_MAKER_FRAME_TYPE.get(), BrickMakerFrameBER::new);
		event.registerBlockEntityRenderer(FABETypes.POWER_SHAFT_TYPE.get(), PowerShaftBER::new);
		event.registerBlockEntityRenderer(FABETypes.GEARBOX_TYPE.get(), GearboxBER::new);
		event.registerBlockEntityRenderer(FABETypes.BEVEL_GEAR_TYPE.get(), BevelGearBER::new);
		event.registerBlockEntityRenderer(FABETypes.SPLITTER_TYPE.get(), SplitterBER::new);
		event.registerBlockEntityRenderer(FABETypes.JOINER_TYPE.get(), JoinerBER::new);
		event.registerBlockEntityRenderer(FABETypes.MILLSTONE_TYPE.get(), MillstoneBER::new);
		event.registerBlockEntityRenderer(FABETypes.HANDCRANK_TYPE.get(), HandCrankBER::new);
		event.registerBlockEntityRenderer(FABETypes.PAPER_BELLOWS_TYPE.get(), PaperBellowsBER::new);
		event.registerBlockEntityRenderer(FABETypes.LEATHER_BELLOWS_TYPE.get(), LeatherBellowsBER::new);
		event.registerBlockEntityRenderer(FABETypes.SMALL_WATERWHEEL_TYPE.get(), SmallWaterwheelBER::new);
		event.registerBlockEntityRenderer(FABETypes.TRIP_HAMMER_TYPE.get(), TripHammerBER::new);
		event.registerBlockEntityRenderer(FABETypes.TUMBLING_BARREL_TYPE.get(), TumblingBarrelBER::new);
		event.registerBlockEntityRenderer(FABETypes.PIPE_TYPE.get(), PipeDebugBER::new);
		event.registerBlockEntityRenderer(FABETypes.FRYING_PAN_TYPE.get(), FryingPanBER::new);
	}

	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event)
	{
		event.register(MenuTypes.STONE_FOUNDRY.get(), StoneFoundryScreen::new);
		event.register(MenuTypes.STONE_CASTING_VESSEL.get(), CircleMenuScreen::new);
		event.register(MenuTypes.WORKBENCH_MENU.get(), WorkbenchScreen::new);
		event.register(MenuTypes.BRICK_FOUNDRY.get(), BrickFoundryScreen::new);
		event.register(MenuTypes.TUMBLING_BARREL.get(), TumblingBarrelScreen::new);
		event.register(MenuTypes.BRICK_KILN.get(), BrickKilnScreen::new);
	}

	@SubscribeEvent
	public static void registerGeometryLoaders(ModelEvent.RegisterGeometryLoaders event)
	{
		event.register(PartialDynamicTextureGeometryLoader.ID, PartialDynamicTextureGeometryLoader.INSTANCE);
	}

	@SubscribeEvent
	public static void registerParticleProviders(RegisterParticleProvidersEvent event)
	{
		event.registerSpriteSet(FAParticleTypes.METAL_SPARK.get(), MetalSparkParticle.Provider::new);
	}

	@SubscribeEvent
	public static void registerClientExtensions(RegisterClientExtensionsEvent event)
	{
		registerFluidTextures(event);
	}

	private static void registerFluidTextures(RegisterClientExtensionsEvent event)
	{
		registerFluidTexture(event, "pancake_batter", FAFluids.PANCAKE_BATTER_TYPE);
		registerFluidTexture(event, "tannin", FAFluids.TANNIN_TYPE);
		registerFluidTexture(event, "limewater", FAFluids.LIMEWATER_TYPE);
		registerFluidTexture(event, "brine", FAFluids.BRINE_TYPE);
		registerFluidTexture(event, "soy_milk", FAFluids.SOY_MILK_TYPE);
		registerFluidTexture(event, "soy_sauce", FAFluids.SOY_SAUCE_TYPE);
		registerFluidTexture(event, "coffee", FAFluids.COFFEE_TYPE);
	}

	private static void registerFluidTexture(RegisterClientExtensionsEvent event, String name, DeferredHolder<FluidType, FluidType> type)
	{
		event.registerFluidType(new IClientFluidTypeExtensions()
		{
			private final ResourceLocation location = FactoryAutomation.name("block/" + name);

			@Override
			public ResourceLocation getStillTexture()
			{
				return location;
			}

			@Override
			public ResourceLocation getFlowingTexture()
			{
				return location;
			}
		}, type);
	}
}
