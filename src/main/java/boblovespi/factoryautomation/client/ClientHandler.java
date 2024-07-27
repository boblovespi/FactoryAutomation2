package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.ChoppingBlockBER;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientHandler
{
	@SubscribeEvent
	public static void onClientSetup(FMLClientSetupEvent event)
	{
		// Some client setup code
		FactoryAutomation.LOGGER.info("Setting up client...");
		FactoryAutomation.LOGGER.info("Minecraft username is {}", Minecraft.getInstance().getUser().getName());
	}

	@SubscribeEvent
	public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event)
	{
		event.registerBlockEntityRenderer(FABETypes.CHOPPING_BLOCK_TYPE.get(), ChoppingBlockBER::new);
	}
}
