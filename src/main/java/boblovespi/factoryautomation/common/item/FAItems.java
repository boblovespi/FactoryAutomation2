package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.SpaceFrameBlock;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.OreQualities;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.item.tool.*;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.GearMaterial;
import boblovespi.factoryautomation.common.util.StoneBlockForms;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
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
	public static final DeferredItem<BlockItem> CHERT = ITEMS.registerSimpleBlockItem(FABlocks.CHERT);
	public static final DeferredItem<BlockItem> CASSITERITE_ORE = ITEMS.registerSimpleBlockItem(FABlocks.CASSITERITE_ORE);
	public static final DeferredItem<Item> RAW_CASSITERITE = ITEMS.registerItem("raw_cassiterite", Item::new);
	public static final DeferredItem<BlockItem> RAW_CASSITERITE_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.RAW_CASSITERITE_BLOCK);
	public static final Map<OreQualities, DeferredItem<BlockItem>> LIMONITE_ORES = FABlocks.LIMONITE_ORES.entrySet().stream().collect(
			Collectors.toMap(Map.Entry::getKey, e -> ITEMS.registerSimpleBlockItem(e.getValue())));
	public static final DeferredItem<Item> RAW_LIMONITE = ITEMS.registerItem("raw_limonite", Item::new);
	public static final DeferredItem<BlockItem> RAW_LIMONITE_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.RAW_LIMONITE_BLOCK);
	public static final DeferredItem<BlockItem> ANCIENT_IRON_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.ANCIENT_IRON_BLOCK);
	public static final DeferredItem<BlockItem> WEAK_IRON_BLOCK = ITEMS.registerSimpleBlockItem(FABlocks.WEAK_IRON_BLOCK);
	public static final DeferredItem<Item> PIG_TALLOW = ITEMS.registerSimpleItem("pig_tallow");
	public static final DeferredItem<Item> ANCIENT_IRON_INGOT = ITEMS.registerSimpleItem("ancient_iron_ingot");
	public static final DeferredItem<Item> ANCIENT_IRON_NUGGET = ITEMS.registerSimpleItem("ancient_iron_nugget");
	public static final DeferredItem<Item> WEAK_IRON_INGOT = ITEMS.registerSimpleItem("weak_iron_ingot");
	public static final DeferredItem<Item> WEAK_IRON_NUGGET = ITEMS.registerSimpleItem("weak_iron_nugget");
	public static final DeferredItem<BlockItem> IRON_SAND = ITEMS.registerSimpleBlockItem(FABlocks.IRON_SAND);
	public static final DeferredItem<BlockItem> IRON_SAND_CHARCOAL_MIX = ITEMS.registerSimpleBlockItem(FABlocks.IRON_SAND_CHARCOAL_MIX);
	public static final DeferredItem<BlockItem> MINT_LEAVES = ITEMS.registerSimpleBlockItem(FABlocks.MINT_BUSH);
	public static final DeferredItem<Item> SOYBEANS = ITEMS.registerSimpleItem("soybeans");
	public static final DeferredItem<Item> GINGER = ITEMS.registerSimpleItem("ginger");
	public static final DeferredItem<BlockItem> WILD_GREEN_ONION = ITEMS.registerSimpleBlockItem(FABlocks.WILD_GREEN_ONION);

	// Refined materials

	public static final DeferredItem<BlockItem> GREEN_SAND = ITEMS.registerSimpleBlockItem(FABlocks.GREEN_SAND);
	public static final Map<Form, DeferredItem<? extends Item>> PIG_TALLOW_FORMS = metal("pig_tallow", Form.tallow(), null, null, null);
	public static final Map<Form, DeferredItem<? extends Item>> TALLOW_MOLDS = metal("tallow_mold", Form.tallow(), null, null, null);
	public static final Map<Form, DeferredItem<? extends Item>> FIRED_TALLOW_MOLDS = tallowMold("fired_mold", Form.tallow());
	public static final DeferredItem<Item> IRON_SHARD = ITEMS.registerSimpleItem("iron_shard");
	public static final DeferredItem<Item> SLAG = ITEMS.registerSimpleItem("slag");
	public static final Map<Form, DeferredItem<? extends Item>> COPPER_THINGS = metal("copper", Form.copper(), null, FABlocks.COPPER_PLATE_BLOCK, FABlocks.COPPER_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> TIN_THINGS = metal("tin", Form.most(), FABlocks.TIN_BLOCK, FABlocks.TIN_PLATE_BLOCK, FABlocks.TIN_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> LEAD_THINGS = metal("lead", Form.most(), FABlocks.LEAD_BLOCK, FABlocks.LEAD_PLATE_BLOCK, FABlocks.LEAD_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> IRON_THINGS = metal("iron", Form.iron(), null, FABlocks.IRON_PLATE_BLOCK, FABlocks.IRON_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> BRONZE_THINGS = metal("bronze", Form.most(), FABlocks.BRONZE_BLOCK, FABlocks.BRONZE_PLATE_BLOCK, FABlocks.BRONZE_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> NICKEL_THINGS = metal("nickel", Form.most(), FABlocks.NICKEL_BLOCK, FABlocks.NICKEL_PLATE_BLOCK, FABlocks.NICKEL_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> SILVER_THINGS = metal("silver", Form.most(), FABlocks.SILVER_BLOCK, FABlocks.SILVER_PLATE_BLOCK, FABlocks.SILVER_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> MAGMATIC_BRASS_THINGS = metal("magmatic_brass", Form.most(), FABlocks.MAGMATIC_BRASS_BLOCK, FABlocks.MAGMATIC_BRASS_PLATE_BLOCK, FABlocks.MAGMATIC_BRASS_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> PIG_IRON_THINGS = metal("pig_iron", Form.most(), FABlocks.PIG_IRON_BLOCK, FABlocks.PIG_IRON_PLATE_BLOCK, FABlocks.PIG_IRON_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> STEEL_THINGS = metal("steel", Form.most(), FABlocks.STEEL_BLOCK, FABlocks.STEEL_PLATE_BLOCK, FABlocks.STEEL_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> ALUMINUM_THINGS = metal("aluminum", Form.most(), FABlocks.ALUMINUM_BLOCK, FABlocks.ALUMINUM_PLATE_BLOCK, FABlocks.ALUMINUM_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> ALUMINUM_BRONZE_THINGS = metal("aluminum_bronze", Form.most(), FABlocks.ALUMINUM_BRONZE_BLOCK, FABlocks.ALUMINUM_BRONZE_PLATE_BLOCK, FABlocks.ALUMINUM_BRONZE_SPACE_FRAME);
	public static final Map<Form, DeferredItem<? extends Item>> CHROMIUM_THINGS = metal("chromium", Form.most(), FABlocks.CHROMIUM_BLOCK, FABlocks.CHROMIUM_PLATE_BLOCK, FABlocks.CHROMIUM_SPACE_FRAME);
	public static final DeferredItem<Item> WHEAT_FLOUR = ITEMS.registerSimpleItem("wheat_flour");
	public static final DeferredItem<Item> CALCITE_DUST = ITEMS.registerSimpleItem("calcite_dust");
	public static final DeferredItem<Item> QUICKLIME = ITEMS.registerSimpleItem("quicklime");
	public static final DeferredItem<Item> MUD_BRICK = ITEMS.registerSimpleItem("mud_brick");
	public static final DeferredItem<Item> DRIED_BRICK = ITEMS.registerSimpleItem("dried_brick");
	public static final DeferredItem<BlockItem> DRIED_BRICKS = ITEMS.registerSimpleBlockItem(FABlocks.DRIED_BRICKS);
	public static final DeferredItem<Item> TANBARK_DUST = ITEMS.registerSimpleItem("tanbark_dust");
	public static final DeferredItem<Item> CLEANED_LEATHER = ITEMS.registerSimpleItem("cleaned_leather");
	public static final DeferredItem<FluidBottle> PANCAKE_BATTER_BOTTLE = ITEMS.registerItem("pancake_batter_bottle", p -> new FluidBottle(p, FAFluids.PANCAKE_BATTER_SOURCE.get()));
	public static final DeferredItem<Item> GROUND_SOYBEAN = ITEMS.registerSimpleItem("ground_soybean");
	public static final DeferredItem<Item> SOY_SAUCE_CULTURE = ITEMS.registerSimpleItem("soy_sauce_culture");
	public static final DeferredItem<FluidBottle> SOY_SAUCE_BOTTLE = ITEMS.registerItem("soy_sauce_bottle", p -> new FluidBottle(p, FAFluids.SOY_SAUCE_SOURCE.get()));

	// Building blocks

	public static final DeferredItem<BlockItem> BRICK_TILES = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_TILES);
	public static final Map<StoneBlockForms, DeferredItem<BlockItem>> ANDESITE_BRICKS = stoneBlockForms(FABlocks.ANDESITE_BRICKS);
	public static final Map<StoneBlockForms, DeferredItem<BlockItem>> GRANITE_BRICKS = stoneBlockForms(FABlocks.GRANITE_BRICKS);
	public static final Map<StoneBlockForms, DeferredItem<BlockItem>> DIORITE_BRICKS = stoneBlockForms(FABlocks.DIORITE_BRICKS);

	private static Map<StoneBlockForms, DeferredItem<BlockItem>> stoneBlockForms(Map<StoneBlockForms, DeferredBlock<? extends Block>> blocks)
	{
		return blocks.entrySet()
					 .stream()
					 .map(e -> Map.entry(e.getKey(), ITEMS.registerSimpleBlockItem(e.getValue())))
					 .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	// Intermediate products

	public static final DeferredItem<Item> SCREW = ITEMS.registerSimpleItem("screw");
	public static final DeferredItem<Item> BUSHING = ITEMS.registerSimpleItem("bushing");
	public static final DeferredItem<Item> IRON_RAIL = ITEMS.registerSimpleItem("iron_rail");
	public static final DeferredItem<Item> GOLD_RAIL = ITEMS.registerSimpleItem("gold_rail");
	public static final DeferredItem<Item> PROCESSED_LEATHER = ITEMS.registerSimpleItem("processed_leather");

	// Food

	public static final DeferredItem<Item> TOASTED_BREAD = ITEMS.registerSimpleItem("toasted_bread", p().food(makeFood(5, 4 / 5f)));
	public static final DeferredItem<Item> SLICED_BREAD = ITEMS.registerSimpleItem("sliced_bread", p().food(new FoodProperties.Builder().nutrition(1).saturationModifier(3 / 5f).fast().build()));
	public static final DeferredItem<Item> CHOCOLATE_ICE_CREAM = ITEMS.registerSimpleItem("chocolate_ice_cream", p().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1 / 2f).effect(new MobEffectInstance(MobEffects.SATURATION, 300, 0), 1.0F).build()));
	public static final DeferredItem<Item> COFFEE_ICE_CREAM = ITEMS.registerSimpleItem("coffee_ice_cream", p().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1 / 2f).effect(new MobEffectInstance(MobEffects.DIG_SPEED, 300, 0), 1.0F).build()));
	public static final DeferredItem<Item> COOKIES_N_CREAM_ICE_CREAM = ITEMS.registerSimpleItem("cookies_n_cream_ice_cream", p().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1 / 2f).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 100, 2), 1.0F).build()));
	public static final DeferredItem<Item> MINT_ICE_CREAM = ITEMS.registerSimpleItem("mint_ice_cream", p().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1 / 2f).effect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 300, 0), 1.0F).build()));
	public static final DeferredItem<Item> SWEETBERRY_ICE_CREAM = ITEMS.registerSimpleItem("sweetberry_ice_cream", p().food(new FoodProperties.Builder().nutrition(4).saturationModifier(1 / 2f).effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).build()));
	public static final DeferredItem<Item> VANILLA_ICE_CREAM = ITEMS.registerSimpleItem("vanilla_ice_cream", p().food(makeFood(3, 1 / 2f)));
	public static final DeferredItem<Item> HAM_AND_EGGS = ITEMS.registerSimpleItem("ham_n_eggs", p().stacksTo(4).food(makeFood(8, 5 / 5f, Items.BOWL)));
	public static final DeferredItem<Item> PANCAKE = ITEMS.registerSimpleItem("pancake", p().food(makeFood(5, 3 / 5f)));
	public static final DeferredItem<Item> HONEY_PANCAKE = ITEMS.registerSimpleItem("honey_pancake", p().food(makeFood(10, 3 / 5f)));
	public static final DeferredItem<Item> TOFU = ITEMS.registerSimpleItem("tofu", p().food(makeFood(2, 1 / 2f)));
	public static final DeferredItem<Item> DOUFUNAO = ITEMS.registerSimpleItem("doufunao", p().stacksTo(4).food(makeFood(6, 3 / 5f, Items.BOWL)));
	public static final DeferredItem<Item> SWEET_DOUFUNAO = ITEMS.registerSimpleItem("sweet_doufunao", p().stacksTo(4).food(makeFood(7, 4 / 5f, Items.BOWL)));
	public static final DeferredItem<Item> SALTY_DOUFUNAO = ITEMS.registerSimpleItem("salty_doufunao", p().stacksTo(4).food(makeFood(7, 4 / 5f, Items.BOWL)));
	public static final DeferredItem<FluidBottle> SOY_MILK_BOTTLE = ITEMS.registerItem("soy_milk_bottle", p -> new FluidBottle(p, FAFluids.SOY_MILK_SOURCE.get()), p().stacksTo(16).food(makeFood(3, 3 / 5f, Items.GLASS_BOTTLE)));
	public static final DeferredItem<Item> GREEN_ONION = ITEMS.registerItem("green_onion", p -> new ItemNameBlockItem(FABlocks.GREEN_ONIONS.get(), p), p().food(makeFood(1, 1 / 10f)));
	public static final DeferredItem<Item> YAKITORI = ITEMS.registerSimpleItem("yakitori", p().food(makeFood(7, 4 / 5f, Items.STICK)));

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
	public static final DeferredItem<BlockItem> BRICK_CASTING_VESSEL = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_CASTING_VESSEL);
	public static final DeferredItem<BlockItem> MILLSTONE = ITEMS.registerSimpleBlockItem(FABlocks.MILLSTONE);
	public static final DeferredItem<BlockItem> BRICK_FIREBOX = ITEMS.registerSimpleBlockItem(FABlocks.BRICK_FIREBOX);
	public static final DeferredItem<BlockItem> PAPER_BELLOWS = ITEMS.registerSimpleBlockItem(FABlocks.PAPER_BELLOWS);
	public static final DeferredItem<BlockItem> LEATHER_BELLOWS = ITEMS.registerSimpleBlockItem(FABlocks.LEATHER_BELLOWS);
	public static final DeferredItem<BlockItem> TRIP_HAMMER = ITEMS.registerSimpleBlockItem(FABlocks.TRIP_HAMMER);
	public static final DeferredItem<BlockItem> TUMBLING_BARREL = ITEMS.registerSimpleBlockItem(FABlocks.TUMBLING_BARREL);
	public static final DeferredItem<BlockItem> FRYING_PAN = ITEMS.registerSimpleBlockItem(FABlocks.FRYING_PAN);

	// Tools

	public static final DeferredItem<DiggerItem> CHOPPING_BLADE = tieredTool("chopping_blade", ChoppingBlade::new, Tools.BAD_FLINT_TIER, p(), 2, -3.2f);

	public static final DeferredItem<ShovelItem> FLINT_SHOVEL = tieredTool("flint_shovel", ShovelItem::new, Tools.FLINT_TIER, p(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = tieredTool("flint_pickaxe", PickaxeItem::new, Tools.FLINT_TIER, p(), 1, -2.8f);
	public static final DeferredItem<AxeItem> FLINT_AXE = tieredTool("flint_axe", AxeItem::new, Tools.FLINT_TIER, p(), 6, -3.2f);
	public static final DeferredItem<HoeItem> FLINT_HOE = tieredTool("flint_hoe", HoeItem::new, Tools.FLINT_TIER, p(), 1, -3f);
	public static final DeferredItem<SwordItem> FLINT_SWORD = tieredTool("flint_sword", SwordItem::new, Tools.FLINT_TIER, p(), 3, -2.4f);

	public static final DeferredItem<Firebow> FIREBOW = ITEMS.registerItem("firebow", Firebow::new);

	public static final DeferredItem<ShovelItem> COPPER_SHOVEL = tieredTool("copper_shovel", ShovelItem::new, Tools.COPPER_TIER, p(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> COPPER_PICKAXE = tieredTool("copper_pickaxe", PickaxeItem::new, Tools.COPPER_TIER, p(), 1, -2.8f);
	public static final DeferredItem<AxeItem> COPPER_AXE = tieredTool("copper_axe", AxeItem::new, Tools.COPPER_TIER, p(), 6.5f, -3.15f);
	public static final DeferredItem<HoeItem> COPPER_HOE = tieredTool("copper_hoe", HoeItem::new, Tools.COPPER_TIER, p(), -1.5f, -1.5f);
	public static final DeferredItem<SwordItem> COPPER_SWORD = tieredTool("copper_sword", SwordItem::new, Tools.COPPER_TIER, p(), 3, -2.4f);
	public static final DeferredItem<Hammer> COPPER_HAMMER = tieredTool("copper_hammer", Hammer::new, Tools.COPPER_TIER, p(), 8, -3.5f);
	public static final DeferredItem<ShearsItem> COPPER_SHEARS = ITEMS.registerItem("copper_shears", ShearsItem::new,
			p().durability(176).component(DataComponents.TOOL, ShearsItem.createToolProperties()));

	public static final DeferredItem<Hammer> IRON_HAMMER = tieredTool("iron_hammer", Hammer::new, Tiers.IRON, p(), 9, -3.5f);
	public static final DeferredItem<Wrench> IRON_WRENCH = tieredTool("iron_wrench", Wrench::new, Tiers.IRON, p(), 2f, -3f);

	public static final DeferredItem<ShovelItem> BRONZE_SHOVEL = tieredTool("bronze_shovel", ShovelItem::new, Tools.BRONZE_TIER, p(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> BRONZE_PICKAXE = tieredTool("bronze_pickaxe", PickaxeItem::new, Tools.BRONZE_TIER, p(), 1, -2.8f);
	public static final DeferredItem<AxeItem> BRONZE_AXE = tieredTool("bronze_axe", AxeItem::new, Tools.BRONZE_TIER, p(), 6.5f, -3.1f);
	public static final DeferredItem<HoeItem> BRONZE_HOE = tieredTool("bronze_hoe", HoeItem::new, Tools.BRONZE_TIER, p(), -1.5f, -0.8f);
	public static final DeferredItem<SwordItem> BRONZE_SWORD = tieredTool("bronze_sword", SwordItem::new, Tools.BRONZE_TIER, p(), 3, -2.4f);
	public static final DeferredItem<Hammer> BRONZE_HAMMER = tieredTool("bronze_hammer", Hammer::new, Tools.BRONZE_TIER, p(), 10, -3.5f);
	public static final DeferredItem<Wrench> BRONZE_WRENCH = tieredTool("bronze_wrench", Wrench::new, Tools.BRONZE_TIER, p(), 2.5f, -3f);

	// Mechanical

	public static final Map<GearMaterial, DeferredItem<Item>> GEARS = gear();

	public static final DeferredItem<BlockItem> WOOD_POWER_SHAFT = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_POWER_SHAFT);
	public static final DeferredItem<BlockItem> WOOD_GEARBOX = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_GEARBOX);
	public static final DeferredItem<BlockItem> WOOD_SPLITTER = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_SPLITTER);
	public static final DeferredItem<BlockItem> WOOD_JOINER = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_JOINER);
	public static final DeferredItem<BlockItem> WOOD_BEVEL_GEAR = ITEMS.registerSimpleBlockItem(FABlocks.WOOD_BEVEL_GEAR);
	public static final DeferredItem<BlockItem> IRON_POWER_SHAFT = ITEMS.registerSimpleBlockItem(FABlocks.IRON_POWER_SHAFT);
	public static final DeferredItem<BlockItem> IRON_GEARBOX = ITEMS.registerSimpleBlockItem(FABlocks.IRON_GEARBOX);
	public static final DeferredItem<BlockItem> IRON_SPLITTER = ITEMS.registerSimpleBlockItem(FABlocks.IRON_SPLITTER);
	public static final DeferredItem<BlockItem> IRON_JOINER = ITEMS.registerSimpleBlockItem(FABlocks.IRON_JOINER);
	public static final DeferredItem<BlockItem> IRON_BEVEL_GEAR = ITEMS.registerSimpleBlockItem(FABlocks.IRON_BEVEL_GEAR);
	public static final DeferredItem<BlockItem> HAND_CRANK = ITEMS.registerSimpleBlockItem(FABlocks.HAND_CRANK);
	public static final DeferredItem<BlockItem> SMALL_WATERHWHEEL = ITEMS.registerSimpleBlockItem(FABlocks.SMALL_WATERWHEEL);
	public static final DeferredItem<BlockItem> LARGE_WATERWHEEL = ITEMS.registerSimpleBlockItem(FABlocks.LARGE_WATERWHEEL);
	public static final DeferredItem<BlockItem> HORSE_ENGINE = ITEMS.registerSimpleBlockItem(FABlocks.HORSE_ENGINE);

	// Logistics

	public static final DeferredItem<BlockItem> WOODEN_TANK = ITEMS.registerSimpleBlockItem(FABlocks.WOODEN_TANK);
	public static final DeferredItem<BlockItem> COPPER_PIPE = ITEMS.registerSimpleBlockItem(FABlocks.COPPER_PIPE);

	// Misc

	public static final DeferredItem<BlockItem> CREATIVE_MECHANICAL_SOURCE = ITEMS.registerSimpleBlockItem(FABlocks.CREATIVE_MECHANICAL_SOURCE);

	private static Item.Properties p()
	{
		return new Item.Properties();
	}

	private static <T extends TieredItem> DeferredItem<T> tieredTool(String name, BiFunction<Tier, Item.Properties, T> constructor, Tier tier, Item.Properties properties,
																	 float damage, float as)
	{
		return ITEMS.registerItem(name, p -> constructor.apply(tier, p), properties.attributes(SwordItem.createAttributes(tier, damage, as)));
	}

	private static Map<Form, DeferredItem<? extends Item>> metal(String name, Collection<Form> metals, @Nullable DeferredBlock<Block> block,
																 @Nullable DeferredBlock<Block> plateBlock, @Nullable DeferredBlock<SpaceFrameBlock> spaceFrame)
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
				Item::new, p().durability(20)), (a, b) -> a, LinkedHashMap::new));
	}

	public static Map<GearMaterial, DeferredItem<Item>> gear()
	{
		return GearMaterial.all().stream().collect(Collectors.toMap(k -> k, k -> ITEMS.registerItem(k.getName() + "_gear",
				Item::new, p().durability(k.getDurability())), (a, b) -> a, LinkedHashMap::new));
	}

	private static FoodProperties makeFood(int nutrition, float saturation)
	{
		return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).build();
	}

	private static FoodProperties makeFood(int nutrition, float saturation, ItemLike remainder)
	{
		return new FoodProperties.Builder().nutrition(nutrition).saturationModifier(saturation).usingConvertsTo(remainder).build();
	}
}
