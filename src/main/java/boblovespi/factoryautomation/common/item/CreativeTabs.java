package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.util.Form;
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

@SuppressWarnings("unused")
@EventBusSubscriber(modid = FactoryAutomation.MODID, bus = EventBusSubscriber.Bus.MOD)
public class CreativeTabs
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, FactoryAutomation.MODID);

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> RESOURCES = CREATIVE_MODE_TABS.register("resources",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "resources"))).withTabsBefore(CreativeModeTabs.COMBAT)
								 .icon(() -> FAItems.ROCK.get().getDefaultInstance()).displayItems((p, o) ->
					{
						o.accept(FAItems.ROCK);
						o.accept(FAItems.PLANT_FIBER);
						o.accept(FAItems.CASSITERITE_ORE);
						o.accept(FAItems.RAW_CASSITERITE);
						o.accept(FAItems.RAW_CASSITERITE_BLOCK);
						FAItems.LIMONITE_ORES.values().forEach(o::accept);
						o.accept(FAItems.RAW_LIMONITE);
						o.accept(FAItems.RAW_LIMONITE_BLOCK);
						o.accept(FAItems.PIG_TALLOW);
						o.accept(FAItems.GREEN_SAND);
						FAItems.PIG_TALLOW_FORMS.values().forEach(o::accept);
						FAItems.TALLOW_MOLDS.values().forEach(o::accept);
						FAItems.FIRED_TALLOW_MOLDS.values().forEach(o::accept);
						o.accept(FAItems.IRON_SHARD);
						o.accept(FAItems.SLAG);
						o.accept(Items.COPPER_INGOT);
						for (var deferredItem : FAItems.COPPER_THINGS.values())
						{
							if (deferredItem == FAItems.COPPER_THINGS.get(Form.SHEET))
								o.accept(Items.COPPER_BLOCK);
							o.accept(deferredItem);
						}
						FAItems.TIN_THINGS.values().forEach(o::accept);
						o.accept(Items.IRON_INGOT);
						o.accept(Items.IRON_NUGGET);
						for (var deferredItem : FAItems.IRON_THINGS.values())
						{
							if (deferredItem == FAItems.IRON_THINGS.get(Form.SHEET))
								o.accept(Items.IRON_BLOCK);
							o.accept(deferredItem);
						}
						FAItems.BRONZE_THINGS.values().forEach(o::accept);
						FAItems.STEEL_THINGS.values().forEach(o::accept);
						o.accept(FAItems.WHEAT_FLOUR);
						o.accept(FAItems.CALCITE_DUST);
						o.accept(FAItems.QUICKLIME);
						o.accept(FAItems.MUD_BRICK);
						o.accept(FAItems.DRIED_BRICK);
						o.accept(FAItems.DRIED_BRICKS);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PRODUCTS = CREATIVE_MODE_TABS.register("products",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "products"))).withTabsBefore(RESOURCES.getId())
								 .icon(() -> FAItems.SCREW.get().getDefaultInstance()).displayItems((p, o) ->
					{
						o.accept(FAItems.SCREW);
						o.accept(FAItems.BUSHING);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PROCESSING = CREATIVE_MODE_TABS.register("processing",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "processing"))).withTabsBefore(PRODUCTS.getId())
								 .icon(() -> FAItems.CHOPPING_BLOCKS.get(WoodTypes.OAK).get().getDefaultInstance()).displayItems((p, o) ->
					{
						FAItems.CHOPPING_BLOCKS.values().forEach(o::accept);
						o.accept(FAItems.LOG_PILE);
						o.accept(FAItems.LIMONITE_CHARCOAL_MIX);
						o.accept(FAItems.STONE_CRUCIBLE);
						o.accept(FAItems.STONE_CASTING_VESSEL);
						o.accept(FAItems.STONE_WORKBENCH);
						o.accept(FAItems.BRICK_MAKER_FRAME);
						o.accept(FAItems.BRICK_CRUCIBLE);
						o.accept(FAItems.BRICK_FIREBOX);
						o.accept(FAItems.MILLSTONE);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MECHANICAL = CREATIVE_MODE_TABS.register("mechanical",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "mechanical"))).withTabsBefore(PRODUCTS.getId())
								 .icon(() -> FAItems.CREATIVE_MECHANICAL_SOURCE.get().getDefaultInstance()).displayItems((p, o) ->
					{
						FAItems.GEARS.values().forEach(o::accept);
						o.accept(FAItems.CREATIVE_MECHANICAL_SOURCE);
						o.accept(FAItems.WOOD_POWER_SHAFT);
						o.accept(FAItems.WOOD_GEARBOX);
					}).build());


	@SubscribeEvent
	public static void addCreative(BuildCreativeModeTabContentsEvent event)
	{
		if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES)
		{
			event.insertFirst(FAItems.CHOPPING_BLADE.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			insertAfter(event, Items.WOODEN_HOE, FAItems.FLINT_SHOVEL, FAItems.FLINT_PICKAXE, FAItems.FLINT_AXE, FAItems.FLINT_HOE);
			insertAfter(event, Items.STONE_HOE, FAItems.COPPER_SHOVEL, FAItems.COPPER_PICKAXE, FAItems.COPPER_AXE, FAItems.COPPER_HOE, FAItems.COPPER_HAMMER);
			insertAfter(event, Items.IRON_HOE, FAItems.IRON_HAMMER, FAItems.IRON_WRENCH);
			event.insertBefore(Items.FLINT_AND_STEEL.getDefaultInstance(), FAItems.FIREBOW.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertBefore(Items.SHEARS.getDefaultInstance(), FAItems.COPPER_SHEARS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}

		if (event.getTabKey() == CreativeModeTabs.COMBAT)
		{
			insertAfter(event, Items.WOODEN_SWORD, FAItems.FLINT_SWORD);
			insertAfter(event, Items.STONE_SWORD, FAItems.COPPER_SWORD);
		}

		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
		{
			insertAfter(event, Items.BRICK_WALL, FABlocks.BRICK_TILES);
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
