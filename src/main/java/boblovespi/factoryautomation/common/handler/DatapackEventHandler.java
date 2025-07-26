package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.Config;
import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.BasicItemListing;
import net.neoforged.neoforge.event.village.VillagerTradesEvent;
import net.neoforged.neoforge.event.village.WandererTradesEvent;

import java.util.ArrayList;

@EventBusSubscriber(modid = FactoryAutomation.MODID)
public class DatapackEventHandler
{
	@SubscribeEvent
	public static void onWandererTradesEvent(WandererTradesEvent event)
	{
		if (Config.wanderingTraderSellsGinger)
		{
			event.getGenericTrades().add(new BasicItemListing(1, FAItems.GINGER.toStack(), 6, 1));
		}
	}

	@SubscribeEvent
	public static void onVillagerTradesEvent(VillagerTradesEvent event)
	{
		if (Config.farmerVillagerSellsHerbs)
		{
			var profession = event.getType();
			var trades = event.getTrades();
			if (profession == VillagerProfession.FARMER)
			{
				trades.compute(3, (i, l) -> {
					l = l == null ? new ArrayList<>() : l;
					l.add(new BasicItemListing(4, FAItems.GREEN_ONION.toStack(), 12, 10));
					l.add(new BasicItemListing(4, FAItems.GINGER.toStack(), 12, 10));
					l.add(new BasicItemListing(4, FAItems.MINT_LEAVES.toStack(), 12, 10));
					return l;
				});
			}
		}
	}
}
