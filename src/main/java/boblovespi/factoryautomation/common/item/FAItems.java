package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.OreQualities;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.tool.*;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.GearMaterial;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WaterloggedTransparentBlock;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class FAItems
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FactoryAutomation.MODID);

	// Resources

	public static final DeferredItem<Item> PLANT_FIBER = ITEMS.registerItem("plant_fiber", Item::new);
	public static final DeferredItem<BlockItem> ROCK = ITEMS.registerItem("rock", p -> new Rock.Item(FABlocks.COBBLESTONE_ROCK.get(), p));
	public static final DeferredItem<BlockItem> CASSITERITE_ORE = ITEMS.registerSimpleBlockItem(FABlocks.CASSITERITE_ORE);
	public static final DeferredItem<Item> RAW_CASSITERITE = ITEMS.registerItem("raw_cassiterite", Item::new);
	public static final DeferredItem<BlockItem> RAW_CASSITERITE_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.RAW_CASSITERITE_BLOCK);
	public static final Map<OreQualities, DeferredItem<BlockItem>> LIMONITE_ORES = FABlocks.LIMONITE_ORES.entrySet().stream().collect(
			Collectors.toMap(Map.Entry::getKey, e -> ITEMS.registerSimpleBlockItem(e.getValue())));
	public static final DeferredItem<Item> RAW_LIMONITE = ITEMS.registerItem("raw_limonite", Item::new);
	public static final DeferredItem<BlockItem> RAW_LIMONITE_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.RAW_LIMONITE_BLOCK);
	public static final DeferredItem<Item> PIG_TALLOW = ITEMS.registerSimpleItem("pig_tallow");

	// Refined materials

	public static final DeferredItem<BlockItem> GREEN_SAND = ITEMS.registerSimpleBlockItem(FABlocks.GREEN_SAND);
	public static final Map<Form, DeferredItem<? extends Item>> PIG_TALLOW_FORMS = metal("pig_tallow", Form.tallow(), null, null, null);
	public static final Map<Form, DeferredItem<? extends Item>> TALLOW_MOLDS = metal("tallow_mold", Form.tallow(), null, null, null);
	public static final Map<Form, DeferredItem<? extends Item>> FIRED_TALLOW_MOLDS = tallowMold("fired_mold", Form.tallow());
	public static final DeferredItem<Item> IRON_SHARD = ITEMS.registerSimpleItem("iron_shard");
	public static final DeferredItem<Item> SLAG = ITEMS.registerSimpleItem("slag");
	public static final Map<Form, DeferredItem<? extends Item>> COPPER_THINGS = metal("copper", Form.copper(), null, FABlocks.COPPER_PLATE_BLOCK, FABlocks.COPPER_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> TIN_THINGS = metal("tin", Form.most(), FABlocks.TIN_BLOCK, FABlocks.TIN_PLATE_BLOCK, FABlocks.TIN_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> IRON_THINGS = metal("iron", Form.iron(), null, FABlocks.IRON_PLATE_BLOCK, FABlocks.IRON_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> BRONZE_THINGS = metal("bronze", Form.most(), FABlocks.BRONZE_BLOCK, FABlocks.BRONZE_PLATE_BLOCK, FABlocks.BRONZE_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> STEEL_THINGS = metal("steel", Form.most(), FABlocks.STEEL_BLOCK, FABlocks.STEEL_PLATE_BLOCK, FABlocks.STEEL_SPACE_FRAME);
	public static final DeferredItem<Item> WHEAT_FLOUR = ITEMS.registerSimpleItem("wheat_flour");
	public static final DeferredItem<Item> CALCITE_DUST = ITEMS.registerSimpleItem("calcite_dust");
	public static final DeferredItem<Item> QUICKLIME = ITEMS.registerSimpleItem("quicklime");
	public static final DeferredItem<Item> MUD_BRICK = ITEMS.registerSimpleItem("mud_brick");
	public static final DeferredItem<Item> DRIED_BRICK = ITEMS.registerSimpleItem("dried_brick");
	public static final DeferredItem<BlockItem> DRIED_BRICKS = ITEMS.registerSimpleBlockItem(FABlocks.DRIED_BRICKS);

	// Building blocks

	public static final DeferredItem<BlockItem> BRICK_TILES = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_TILES);

	// Intermediate products

	public static final DeferredItem<Item> SCREW = ITEMS.registerSimpleItem("screw");
	public static final DeferredItem<Item> BUSHING = ITEMS.registerSimpleItem("bushing");

	// Food

	public static final DeferredItem<Item> TOASTED_BREAD = ITEMS.registerSimpleItem("toasted_bread",
			new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(4 / 5f).build()));

	// Processing

	public static final Map<WoodTypes, DeferredItem<BlockItem>> CHOPPING_BLOCKS = FABlocks.CHOPPING_BLOCKS.entrySet().stream().collect(
			Collectors.toMap(Map.Entry::getKey, e -> ITEMS.registerSimpleBlockItem(e.getValue())));
	public static final DeferredItem<BlockItem> LOG_PILE = ITEMS.registerSimpleBlockItem(FABlocks.LOG_PILE);
	public static final DeferredItem<BlockItem> LIMONITE_CHARCOAL_MIX = ITEMS.registerSimpleBlockItem(FABlocks.LIMONITE_CHARCOAL_MIX);
	public static final DeferredItem<BlockItem> STONE_CRUCIBLE = ITEMS.registerSimpleBlockItem(FABlocks.STONE_CRUCIBLE);
	public static final DeferredItem<BlockItem> STONE_CASTING_VESSEL = ITEMS.registerSimpleBlockItem(FABlocks.STONE_CASTING_VESSEL);
	public static final DeferredItem<BlockItem> STONE_WORKBENCH = ITEMS.registerSimpleBlockItem(FABlocks.STONE_WORKBENCH);
	public static final DeferredItem<BlockItem> BRICK_MAKER_FRAME = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_MAKER_FRAME);
	public static final DeferredItem<BlockItem> BRICK_CRUCIBLE = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_CRUCIBLE);
	public static final DeferredItem<BlockItem> MILLSTONE = ITEMS.registerSimpleBlockItem(FABlocks.MILLSTONE);
	public static final DeferredItem<BlockItem> BRICK_FIREBOX = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_FIREBOX);

	// Tools

	public static final DeferredItem<DiggerItem> CHOPPING_BLADE = tieredTool("chopping_blade", ChoppingBlade::new, Tools.BAD_FLINT_TIER, new Item.Properties(), 2, -3.2f);

	public static final DeferredItem<ShovelItem> FLINT_SHOVEL = tieredTool("flint_shovel", ShovelItem::new, Tools.FLINT_TIER, new Item.Properties(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = tieredTool("flint_pickaxe", PickaxeItem::new, Tools.FLINT_TIER, new Item.Properties(), 1, -2.8f);
	public static final DeferredItem<AxeItem> FLINT_AXE = tieredTool("flint_axe", AxeItem::new, Tools.FLINT_TIER, new Item.Properties(), 6, -3.2f);
	public static final DeferredItem<HoeItem> FLINT_HOE = tieredTool("flint_hoe", HoeItem::new, Tools.FLINT_TIER, new Item.Properties(), 1, -3f);
	public static final DeferredItem<SwordItem> FLINT_SWORD = tieredTool("flint_sword", SwordItem::new, Tools.FLINT_TIER, new Item.Properties(), 3, -2.4f);

	public static final DeferredItem<Firebow> FIREBOW = ITEMS.registerItem("firebow", Firebow::new);

	public static final DeferredItem<ShovelItem> COPPER_SHOVEL = tieredTool("copper_shovel", ShovelItem::new, Tools.COPPER_TIER, new Item.Properties(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = tieredTool("copper_pickaxe", PickaxeItem::new, Tools.COPPER_TIER, new Item.Properties(), 1, -2.8f);
	public static final DeferredItem<AxeItem> COPPER_AXE = tieredTool("copper_axe", AxeItem::new, Tools.COPPER_TIER, new Item.Properties(), 6.5f, -3.15f);
	public static final DeferredItem<HoeItem> COPPER_HOE = tieredTool("copper_hoe", HoeItem::new, Tools.COPPER_TIER, new Item.Properties(), -1.5f, -1.5f);
	public static final DeferredItem<SwordItem> COPPER_SWORD = tieredTool("copper_sword", SwordItem::new, Tools.COPPER_TIER, new Item.Properties(), 3, -2.4f);
	public static final DeferredItem<Hammer> COPPER_HAMMER = tieredTool("copper_hammer", Hammer::new, Tools.COPPER_TIER, new Item.Properties(), 8, -3.5f);
	public static final DeferredItem<ShearsItem> COPPER_SHEARS = ITEMS.registerItem("copper_shears", ShearsItem::new,
			new Item.Properties().durability(176).component(DataComponents.TOOL, ShearsItem.createToolProperties()));

	public static final DeferredItem<Hammer> IRON_HAMMER = tieredTool("iron_hammer", Hammer::new, Tiers.IRON, new Item.Properties(), 8, -3.5f);
	public static final DeferredItem<Wrench> IRON_WRENCH = tieredTool("iron_wrench", Wrench::new, Tiers.IRON, new Item.Properties(), 1.5f, -3f);

	// Mechanical

	public static final Map<GearMaterial, DeferredItem<Item>> GEARS = gear();

	public static final DeferredItem<BlockItem> WOOD_POWER_SHAFT = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_POWER_SHAFT);
	public static final DeferredItem<BlockItem> WOOD_GEARBOX = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_GEARBOX);
	public static final DeferredItem<BlockItem> HAND_CRANK = ITEMS.registerSimpleBlockItem(FABlocks.HAND_CRANK);

	// Misc

	public static final DeferredItem<BlockItem> CREATIVE_MECHANICAL_SOURCE = ITEMS.registerSimpleBlockItem(FABlocks.CREATIVE_MECHANICAL_SOURCE);

	private static <T extends TieredItem> DeferredItem<T> tieredTool(String name, BiFunction<Tier, Item.Properties, T> constructor, Tier tier, Item.Properties properties,
																	 float damage, float as)
	{
		return ITEMS.registerItem(name, p -> constructor.apply(tier, p), properties.attributes(SwordItem.createAttributes(tier, damage, as)));
	}

	private static Map<Form, DeferredItem<? extends Item>> metal(String name, Collection<Form> metals, @Nullable DeferredBlock<Block> block,
																 @Nullable DeferredBlock<Block> plateBlock, @Nullable DeferredBlock<WaterloggedTransparentBlock> spaceFrame)
	{
		return metals.stream().collect(Collectors.toMap(k -> k, k -> {
			if (k == Form.BLOCK && block != null)
				return ITEMS.registerSimpleBlockItem(block);
			else if (k == Form.PLATE_BLOCK && plateBlock != null)
				return ITEMS.registerSimpleBlockItem(plateBlock);
			else if (k == Form.SPACE_FRAME && spaceFrame != null)
				return ITEMS.registerSimpleBlockItem(spaceFrame);
			return ITEMS.registerItem(name + "_" + k.getName(), Item::new);
		}, (a, b) -> a, LinkedHashMap::new));
	}

	private static Map<Form, DeferredItem<? extends Item>> tallowMold(String name, Collection<Form> metals)
	{
		return metals.stream().collect(Collectors.toMap(k -> k, k -> ITEMS.registerItem(name + "_" + k.getName(),
				Item::new, new Item.Properties().durability(20)), (a, b) -> a, LinkedHashMap::new));
	}

	public static Map<GearMaterial, DeferredItem<Item>> gear()
	{
		return GearMaterial.all().stream().collect(Collectors.toMap(k -> k, k -> ITEMS.registerItem(k.getName() + "_gear",
				Item::new, new Item.Properties().durability(k.getDurability())), (a, b) -> a, LinkedHashMap::new));
	}
}
