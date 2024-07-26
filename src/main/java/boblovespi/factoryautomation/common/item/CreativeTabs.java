package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabs
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FactoryAutomation.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES = CREATIVE_MODE_TABS.register("resources",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "resources"))).withTabsBefore(CreativeModeTabs.COMBAT)
								 .icon(() -> FAItems.ROCK.get().getDefaultInstance()).displayItems((p, o) ->
					{
						o.accept(FAItems.ROCK.get());
					}).build());


	@SubscribeEvent
	public static void addCreative(BuildCreativeModeTabContentsEvent event)
	{

	}
}
