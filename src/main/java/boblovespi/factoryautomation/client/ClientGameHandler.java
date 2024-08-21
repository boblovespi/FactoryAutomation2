package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = FactoryAutomation.MODID, value = Dist.CLIENT)
public class ClientGameHandler
{
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event)
	{
		PartialTickHelper.tick();
	}
}
