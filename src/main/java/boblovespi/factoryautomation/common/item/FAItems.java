package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.tool.Tools;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FAItems
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FactoryAutomation.MODID);

	// Resources

	public static final DeferredItem<Item> PLANT_FIBER = ITEMS.registerItem("plant_fiber", Item::new);
	public static final DeferredItem<BlockItem> ROCK = ITEMS.registerItem("rock", p -> new Rock.Item(FABlocks.COBBLESTONE_ROCK.get(), p));

	// Refined materials

	public static final DeferredItem<BlockItem> GREEN_SAND = ITEMS.registerSimpleBlockItem(FABlocks.GREEN_SAND);

	// Food

	public static final DeferredItem<Item> TOASTED_BREAD = ITEMS.registerSimpleItem("toasted_bread",
			new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(4 / 5f).build()));

	// Processing

	public static final Map<WoodTypes, DeferredItem<BlockItem>> CHOPPING_BLOCKS = FABlocks.CHOPPING_BLOCKS.entrySet().stream().collect(
			Collectors.toMap(Map.Entry::getKey, e -> ITEMS.registerSimpleBlockItem(e.getValue())));
	public static final DeferredItem<BlockItem> LOG_PILE = ITEMS.registerSimpleBlockItem(FABlocks.LOG_PILE);
	public static final DeferredItem<BlockItem> STONE_CRUCIBLE = ITEMS.registerSimpleBlockItem(FABlocks.STONE_CRUCIBLE);
	public static final DeferredItem<BlockItem> STONE_CASTING_VESSEL = ITEMS.registerSimpleBlockItem(FABlocks.STONE_CASTING_VESSEL);

	// Tools

	public static final DeferredItem<DiggerItem> CHOPPING_BLADE = tieredTool("chopping_blade", (t, p) -> new DiggerItem(t, FATags.Blocks.MINEABLE_WITH_CHOPPING_BLADE, p),
			Tools.BAD_FLINT_TIER, new Item.Properties(), 2, -3.2f);

	public static final DeferredItem<ShovelItem> FLINT_SHOVEL = tieredTool("flint_shovel", ShovelItem::new, Tools.FLINT_TIER, new Item.Properties(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = tieredTool("flint_pickaxe", PickaxeItem::new, Tools.FLINT_TIER, new Item.Properties(), 1, -2.8f);
	public static final DeferredItem<AxeItem> FLINT_AXE = tieredTool("flint_axe", AxeItem::new, Tools.FLINT_TIER, new Item.Properties(), 6, -3.2f);
	public static final DeferredItem<HoeItem> FLINT_HOE = tieredTool("flint_hoe", HoeItem::new, Tools.FLINT_TIER, new Item.Properties(), 1, -3f);
	public static final DeferredItem<SwordItem> FLINT_SWORD = tieredTool("flint_sword", SwordItem::new, Tools.FLINT_TIER, new Item.Properties(), 3, -2.4f);

	private static <T extends TieredItem> DeferredItem<T> tieredTool(String name, BiFunction<Tier, Item.Properties, T> constructor, Tier tier, Item.Properties properties,
																	 float damage, float as)
	{
		return ITEMS.registerItem(name, p -> constructor.apply(tier, p), properties.attributes(SwordItem.createAttributes(tier, damage, as)));
	}
}
