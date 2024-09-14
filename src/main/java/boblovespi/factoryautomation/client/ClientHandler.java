package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.ChoppingBlockBER;
import boblovespi.factoryautomation.client.ber.MillstoneBER;
import boblovespi.factoryautomation.client.ber.PowerShaftBER;
import boblovespi.factoryautomation.client.ber.StoneCastingVesselBER;
import boblovespi.factoryautomation.client.gui.StoneCastingVesselScreen;
import boblovespi.factoryautomation.client.gui.StoneFoundryScreen;
import boblovespi.factoryautomation.client.gui.WorkbenchScreen;
import boblovespi.factoryautomation.client.model.PartialDynamicTextureGeometryLoader;
import boblovespi.factoryautomation.common.FAParticleTypes;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.processing.MillstoneBE;
import boblovespi.factoryautomation.common.menu.MenuTypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
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
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(FABETypes.CHOPPING_BLOCK_TYPE.get(), ChoppingBlockBER::new);
		event.registerBlockEntityRenderer(FABETypes.STONE_CASTING_VESSEL_TYPE.get(), StoneCastingVesselBER::new);
		// event.registerBlockEntityRenderer(FABETypes.BRICK_MAKER_FRAME_TYPE.get(), BrickMakerFrameBER::new);
		event.registerBlockEntityRenderer(FABETypes.POWER_SHAFT_TYPE.get(), PowerShaftBER::new);
		event.registerBlockEntityRenderer(FABETypes.MILLSTONE_TYPE.get(), MillstoneBER::new);
	}

	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event)
	{
		event.register(MenuTypes.STONE_FOUNDRY.get(), StoneFoundryScreen::new);
		event.register(MenuTypes.STONE_CASTING_VESSEL.get(), StoneCastingVesselScreen::new);
		event.register(MenuTypes.WORKBENCH_MENU.get(), WorkbenchScreen::new);
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
}
