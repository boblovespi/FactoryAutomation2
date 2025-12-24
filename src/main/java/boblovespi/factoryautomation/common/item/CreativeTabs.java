package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.StoneBlockForms;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;

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
						o.accept(FAItems.MINT_LEAVES);
						o.accept(FAItems.CASSITERITE_ORE);
						o.accept(FAItems.RAW_CASSITERITE);
						o.accept(FAItems.RAW_CASSITERITE_BLOCK);
						FAItems.LIMONITE_ORES.values().forEach(o::accept);
						o.accept(FAItems.RAW_LIMONITE);
						o.accept(FAItems.RAW_LIMONITE_BLOCK);
						o.accept(FAItems.HALITE);
						o.accept(FAItems.GYPSUM);
						o.accept(FAItems.IRON_SAND);
						o.accept(FAItems.IRON_SAND_CHARCOAL_MIX);
						o.accept(FAItems.PIG_TALLOW);
						o.accept(FAItems.GREEN_SAND);
						FAItems.PIG_TALLOW_FORMS.values().forEach(o::accept);
						FAItems.TALLOW_MOLDS.values().forEach(o::accept);
						FAItems.FIRED_TALLOW_MOLDS.values().forEach(o::accept);
						o.accept(FAItems.IRON_SHARD);
						o.accept(FAItems.ANCIENT_IRON_INGOT);
						o.accept(FAItems.ANCIENT_IRON_NUGGET);
						o.accept(FAItems.ANCIENT_IRON_BLOCK);
						o.accept(FAItems.WEAK_IRON_INGOT);
						o.accept(FAItems.WEAK_IRON_NUGGET);
						o.accept(FAItems.WEAK_IRON_BLOCK);
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
						FAItems.LEAD_THINGS.values().forEach(o::accept);
						FAItems.BRONZE_THINGS.values().forEach(o::accept);
						FAItems.NICKEL_THINGS.values().forEach(o::accept);
						FAItems.SILVER_THINGS.values().forEach(o::accept);
						FAItems.MAGMATIC_BRASS_THINGS.values().forEach(o::accept);
						FAItems.PIG_IRON_THINGS.values().forEach(o::accept);
						FAItems.STEEL_THINGS.values().forEach(o::accept);
						FAItems.ALUMINUM_THINGS.values().forEach(o::accept);
						FAItems.ALUMINUM_BRONZE_THINGS.values().forEach(o::accept);
						FAItems.CHROMIUM_THINGS.values().forEach(o::accept);
						o.accept(FAItems.WHEAT_FLOUR);
						o.accept(FAItems.CALCITE_DUST);
						o.accept(FAItems.SALT);
						o.accept(FAItems.GYPSUM_DUST);
						o.accept(FAItems.QUICKLIME);
						o.accept(FAItems.CALCIUM_SULFATE_HEMIHYDRATE_DUST);
						o.accept(FAItems.COAL_COKE);
						o.accept(FAItems.MUD_BRICK);
						o.accept(FAItems.DRIED_BRICK);
						o.accept(FAItems.DRIED_BRICKS);
						o.accept(FAItems.TANBARK_DUST);
						o.accept(FAItems.CLEANED_LEATHER);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PRODUCTS = CREATIVE_MODE_TABS.register("products",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "products"))).withTabsBefore(RESOURCES.getId())
								 .icon(() -> FAItems.SCREW.get().getDefaultInstance()).displayItems((p, o) ->
					{
						o.accept(FAItems.SCREW);
						o.accept(FAItems.BUSHING);
						o.accept(FAItems.IRON_RAIL);
						o.accept(FAItems.GOLD_RAIL);
						o.accept(FAItems.PROCESSED_LEATHER);
						o.accept(FAItems.LEATHER_PULLEY_BELT);
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
						o.accept(FAItems.BRICK_CASTING_VESSEL);
						o.accept(FAItems.MILLSTONE);
						o.accept(FAItems.PAPER_BELLOWS);
						o.accept(FAItems.LEATHER_BELLOWS);
						o.accept(FAItems.TRIP_HAMMER);
						o.accept(FAItems.TUMBLING_BARREL);
						o.accept(FAItems.FRYING_PAN);
						o.accept(FAItems.BAMBOO_BASKET);
						o.accept(FAItems.BRICK_KILN);
						o.accept(FAItems.STEAM_OVEN);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MECHANICAL = CREATIVE_MODE_TABS.register("mechanical",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "mechanical"))).withTabsBefore(PRODUCTS.getId())
								 .icon(() -> FAItems.CREATIVE_MECHANICAL_SOURCE.get().getDefaultInstance()).displayItems((p, o) ->
					{
						FAItems.GEARS.values().forEach(o::accept);
						o.accept(FAItems.CREATIVE_MECHANICAL_SOURCE);
						o.accept(FAItems.WOOD_POWER_SHAFT);
						o.accept(FAItems.WOOD_GEARBOX);
						o.accept(FAItems.WOOD_SPLITTER);
						o.accept(FAItems.WOOD_JOINER);
						o.accept(FAItems.WOOD_BEVEL_GEAR);
						o.accept(FAItems.IRON_POWER_SHAFT);
						o.accept(FAItems.IRON_GEARBOX);
						o.accept(FAItems.IRON_SPLITTER);
						o.accept(FAItems.IRON_JOINER);
						o.accept(FAItems.IRON_BEVEL_GEAR);
						o.accept(FAItems.HAND_CRANK);
						o.accept(FAItems.SMALL_WATERWHEEL);
						o.accept(FAItems.LARGE_WATERWHEEL);
						o.accept(FAItems.HORSE_ENGINE);
					}).build());

	public static final DeferredHolder<CreativeModeTab, CreativeModeTab> LOGISTICS = CREATIVE_MODE_TABS.register("logistics",
			() -> CreativeModeTab.builder().title(Component.translatable(FactoryAutomation.locString("itemGroup", "logistics"))).withTabsBefore(MECHANICAL.getId())
								 .icon(() -> FAItems.WOODEN_TANK.get().getDefaultInstance()).displayItems((p, o) ->
					{
						o.accept(FAItems.WOODEN_TANK);
						o.accept(FAItems.COPPER_PIPE);
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
			insertAfter(event, FAItems.IRON_WRENCH, FAItems.BRONZE_SHOVEL, FAItems.BRONZE_PICKAXE, FAItems.BRONZE_AXE, FAItems.BRONZE_HOE, FAItems.BRONZE_HAMMER, FAItems.BRONZE_WRENCH);
			event.insertBefore(Items.FLINT_AND_STEEL.getDefaultInstance(), FAItems.FIREBOW.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			event.insertBefore(Items.SHEARS.getDefaultInstance(), FAItems.COPPER_SHEARS.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		}

		if (event.getTabKey() == CreativeModeTabs.COMBAT)
		{
			insertAfter(event, Items.WOODEN_SWORD, FAItems.FLINT_SWORD);
			insertAfter(event, Items.STONE_SWORD, FAItems.COPPER_SWORD);
			insertAfter(event, Items.IRON_SWORD, FAItems.BRONZE_SWORD);
		}

		if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
		{
			insertAfter(event, Items.BRICK_WALL, FAItems.BRICK_TILES);
			insertForms(event, Items.POLISHED_ANDESITE_SLAB, FAItems.ANDESITE_BRICKS);
			insertForms(event, Items.POLISHED_GRANITE_SLAB, FAItems.GRANITE_BRICKS);
			insertForms(event, Items.POLISHED_GRANITE_SLAB, FAItems.DIORITE_BRICKS);
		}

		if (event.getTabKey() == CreativeModeTabs.NATURAL_BLOCKS)
		{
			insertAfter(event, Items.TUFF, FAItems.CHERT);
			insertAfter(event, Items.ALLIUM, FAItems.WILD_GREEN_ONION);
			insertAfter(event, Items.FLOWERING_AZALEA_LEAVES, FAItems.ARABICA_LEAVES);
		}

		if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS)
		{
			insertAfter(event, Items.BREAD, FAItems.TOASTED_BREAD, FAItems.SLICED_BREAD, FAItems.PANCAKE, FAItems.HONEY_PANCAKE);
			insertAfter(event, Items.MILK_BUCKET, FAItems.CHOCOLATE_ICE_CREAM, FAItems.COFFEE_ICE_CREAM, FAItems.COOKIES_N_CREAM_ICE_CREAM, FAItems.MINT_ICE_CREAM,
					FAItems.SWEETBERRY_ICE_CREAM, FAItems.VANILLA_ICE_CREAM, FAItems.MILK_ICE_CREAM);
			insertAfter(event, Items.RABBIT_STEW, FAItems.HAM_AND_EGGS, FAItems.STEAMED_FISH);
			insertAfter(event, Items.HONEY_BOTTLE, FAItems.PANCAKE_BATTER_BOTTLE, FAItems.SOY_SAUCE_BOTTLE, FAItems.SOY_MILK_BOTTLE, FAItems.COFFEE_BOTTLE);
			insertAfter(event, Items.MELON_SLICE, FAItems.SOYBEANS, FAItems.GROUND_SOYBEAN, FAItems.SOY_SAUCE_CULTURE, FAItems.TOFU, FAItems.DOUFUNAO, FAItems.SALTY_DOUFUNAO, FAItems.SWEET_DOUFUNAO);
			insertAfter(event, Items.CHICKEN, FAItems.YAKITORI);
			insertAfter(event, Items.CARROT, FAItems.GINGER, FAItems.GREEN_ONION, FAItems.COFFEE_CHERRY, FAItems.TEA_SEEDS, FAItems.TEA_LEAF, FAItems.PANNED_TEA_LEAF, FAItems.GREEN_TEA_LEAF);
			insertAfter(event, FAItems.SALTY_DOUFUNAO, FAItems.GREEN_COFFEE_BEANS, FAItems.ROASTED_COFFEE_BEANS, FAItems.GROUND_COFFEE);
			// insertAfter(event, Items.COOKED_COD, FAItems.STEAMED_FISH);
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

	private static void insertForms(BuildCreativeModeTabContentsEvent event, ItemLike target, Map<StoneBlockForms, DeferredItem<BlockItem>> items)
	{
		var before = target;
		for (var form : StoneBlockForms.values())
		{
			event.insertAfter(before.asItem().getDefaultInstance(), items.get(form).toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
			before = items.get(form);
		}
	}
}
