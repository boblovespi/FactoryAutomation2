package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
		{
			event.insertFirst(FAItems.CHOPPING_BLADE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			insertAfter(event, Items.WOODEN_HOE, FAItems.FLINT_SHOVEL, FAItems.FLINT_PICKAXE, FAItems.FLINT_AXE, FAItems.FLINT_HOE);
		}

		if (event.getTabKey() == CreativeModeTabs.COMBAT)
		{
			insertAfter(event, Items.WOODEN_SWORD, FAItems.FLINT_SWORD);
		}
	}

	private static void insertAfter(BuildCreativeModeTabContentsEvent event, ItemLike target, ItemLike... items)
	{
		var before = target;
		for (var item : items)
		{
			event.insertAfter(before.asItem().getDefaultInstance(), item.asItem().getDefaultInstance(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			before = item;
		}
	}
}
