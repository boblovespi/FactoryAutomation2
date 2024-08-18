package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.tool.Tools;
import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Collection;
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
	public static final Map<Form, DeferredItem<? extends Item>> COPPER_THINGS = metal("copper", Form.copper(), null);
	public static final Map<Form, DeferredItem<? extends Item>> TIN_THINGS = metal("tin", Form.most(), FABlocks.TIN_BLOCK);

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

	public static final DeferredItem<ShovelItem> COPPER_SHOVEL = tieredTool("copper_shovel", ShovelItem::new, Tools.COPPER_TIER, new Item.Properties(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = tieredTool("copper_pickaxe", PickaxeItem::new, Tools.COPPER_TIER, new Item.Properties(), 1, -2.8f);
	public static final DeferredItem<AxeItem> COPPER_AXE = tieredTool("copper_axe", AxeItem::new, Tools.COPPER_TIER, new Item.Properties(), 6.5f, -3.15f);
	public static final DeferredItem<HoeItem> COPPER_HOE = tieredTool("copper_hoe", HoeItem::new, Tools.COPPER_TIER, new Item.Properties(), -1.5f, -1.5f);
	public static final DeferredItem<SwordItem> COPPER_SWORD = tieredTool("copper_sword", SwordItem::new, Tools.COPPER_TIER, new Item.Properties(), 3, -2.4f);

	private static <T extends TieredItem> DeferredItem<T> tieredTool(String name, BiFunction<Tier, Item.Properties, T> constructor, Tier tier, Item.Properties properties,
																	 float damage, float as)
	{
		return ITEMS.registerItem(name, p -> constructor.apply(tier, p), properties.attributes(SwordItem.createAttributes(tier, damage, as)));
	}

	private static Map<Form, DeferredItem<? extends Item>> metal(String name, Collection<Form> metals, @Nullable DeferredBlock<Block> block)
	{
		return metals.stream().collect(Collectors.toMap(k -> k, k -> {
			if (k == Form.BLOCK && block != null)
				return ITEMS.registerSimpleBlockItem(block);
			return ITEMS.registerItem(name + "_" + k.getName(), Item::new);
		}));
	}
}
