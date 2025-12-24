package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.*;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.GearMaterial;
import boblovespi.factoryautomation.common.util.StoneBlockForms;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.registries.DeferredItem;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("SameParameterValue")
public class FARecipeProvider extends RecipeProvider
{
	public FARecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		// common ingredients
		var cobbleSlabs = Ingredient.of(Items.COBBLESTONE_SLAB, Items.BLACKSTONE_SLAB, Items.COBBLED_DEEPSLATE_SLAB);

		// shaped

		stoneBricks(output, Items.ANDESITE, Items.POLISHED_ANDESITE, FAItems.ANDESITE_BRICKS);
		stoneBricks(output, Items.GRANITE, Items.POLISHED_GRANITE, FAItems.GRANITE_BRICKS);
		stoneBricks(output, Items.DIORITE, Items.POLISHED_DIORITE, FAItems.DIORITE_BRICKS);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, PatchouliAPI.get().getBookStack(FactoryAutomation.name("guidebook")))
						   .pattern("rd")
						   .define('r', FAItems.ROCK)
						   .define('d', Blocks.DIRT)
						   .unlockedBy("has_rock", has(FAItems.ROCK))
						   .save(output, FactoryAutomation.name("guidebook"));

		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, Blocks.GRAVEL, FAItems.ROCK);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, FABlocks.GREEN_SAND, 4)
						   .pattern("sc")
						   .pattern("cs")
						   .define('c', Blocks.CLAY)
						   .define('s', Tags.Items.SANDS_COLORLESS)
						   .unlockedBy("has_clay", has(Blocks.CLAY))
						   .save(output);
		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, Blocks.MUD_BRICKS, FAItems.MUD_BRICK);
		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, FABlocks.DRIED_BRICKS, FAItems.DRIED_BRICK);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.BRICK, 4)
						   .pattern("b")
						   .define('b', Items.BRICKS)
						   .group("brick")
						   .unlockedBy("has_bricks", has(Items.BRICKS))
						   .save(output, FactoryAutomation.name("bricks_to_brick"));

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.INGOT))
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_ingot")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.NUGGET))
						   .pattern("n")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_nugget")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.BLOCK))
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_block")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.SHEET))
						   .pattern("nnn")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_sheet")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.ROD))
						   .pattern("n")
						   .pattern("n")
						   .pattern("n")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_rod")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.PIG_TALLOW_FORMS.get(Form.GEAR))
						   .pattern(" n ")
						   .pattern("n n")
						   .pattern(" n ")
						   .define('n', FAItems.PIG_TALLOW)
						   .group("pig_tallow_gear")
						   .unlockedBy("has_pig_tallow", has(FAItems.PIG_TALLOW))
						   .save(output);

		Form.tallow().forEach(f -> tallowMold(output, f, FAItems.PIG_TALLOW_FORMS, FAItems.TALLOW_MOLDS));
		Form.tallow().forEach(f -> firedTallowMold(output, f, FAItems.TALLOW_MOLDS, FAItems.FIRED_TALLOW_MOLDS));

		rawOre(FAItems.RAW_CASSITERITE, FAItems.RAW_CASSITERITE_BLOCK, "raw_cassiterite", output);
		rawOre(FAItems.RAW_LIMONITE, FAItems.RAW_LIMONITE_BLOCK, "raw_limonite", output);

		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, FAItems.HALITE, FAItems.SALT);

		ingot(FAItems.ANCIENT_IRON_INGOT, FAItems.ANCIENT_IRON_NUGGET, "ancient_iron", output);
		block(FAItems.ANCIENT_IRON_BLOCK, FAItems.ANCIENT_IRON_INGOT, "ancient_iron", output);
		ingot(FAItems.WEAK_IRON_INGOT, FAItems.WEAK_IRON_NUGGET, "weak_iron", output);
		block(FAItems.WEAK_IRON_BLOCK, FAItems.WEAK_IRON_INGOT, "weak_iron", output);

		ingot(Items.COPPER_INGOT, FAItems.COPPER_THINGS.get(Form.NUGGET), Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, "copper", output);
		metal(FAItems.COPPER_THINGS, Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, Tags.Items.STORAGE_BLOCKS_COPPER, FATags.Items.COPPER_SHEET, "copper", output);
		metal(FAItems.TIN_THINGS, FATags.Items.TIN_INGOT, FATags.Items.TIN_NUGGET, FATags.Items.TIN_BLOCK, FATags.Items.TIN_SHEET, "tin", output);
		metal(FAItems.IRON_THINGS, Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON, Tags.Items.STORAGE_BLOCKS_IRON, FATags.Items.IRON_SHEET, "iron", output);
		metal(FAItems.LEAD_THINGS, FATags.Items.LEAD_INGOT, FATags.Items.LEAD_NUGGET, FATags.Items.LEAD_BLOCK, FATags.Items.LEAD_SHEET, "lead", output);
		metal(FAItems.BRONZE_THINGS, FATags.Items.BRONZE_INGOT, FATags.Items.BRONZE_NUGGET, FATags.Items.BRONZE_BLOCK, FATags.Items.BRONZE_SHEET, "bronze", output);
		metal(FAItems.NICKEL_THINGS, FATags.Items.NICKEL_INGOT, FATags.Items.NICKEL_NUGGET, FATags.Items.NICKEL_BLOCK, FATags.Items.NICKEL_SHEET, "nickel", output);
		metal(FAItems.SILVER_THINGS, FATags.Items.SILVER_INGOT, FATags.Items.SILVER_NUGGET, FATags.Items.SILVER_BLOCK, FATags.Items.SILVER_SHEET, "silver", output);
		metal(FAItems.MAGMATIC_BRASS_THINGS, FATags.Items.MAGMATIC_BRASS_INGOT, FATags.Items.MAGMATIC_BRASS_NUGGET, FATags.Items.MAGMATIC_BRASS_BLOCK,
				FATags.Items.MAGMATIC_BRASS_SHEET, "magmatic_brass", output);
		metal(FAItems.PIG_IRON_THINGS, FATags.Items.PIG_IRON_INGOT, FATags.Items.PIG_IRON_NUGGET, FATags.Items.PIG_IRON_BLOCK, FATags.Items.PIG_IRON_SHEET, "pig_iron", output);
		metal(FAItems.STEEL_THINGS, FATags.Items.STEEL_INGOT, FATags.Items.STEEL_NUGGET, FATags.Items.STEEL_BLOCK, FATags.Items.STEEL_SHEET, "steel", output);
		metal(FAItems.ALUMINUM_THINGS, FATags.Items.ALUMINUM_INGOT, FATags.Items.ALUMINUM_NUGGET, FATags.Items.ALUMINUM_BLOCK, FATags.Items.ALUMINUM_SHEET, "aluminum", output);
		metal(FAItems.ALUMINUM_BRONZE_THINGS, FATags.Items.ALUMINUM_BRONZE_INGOT, FATags.Items.ALUMINUM_BRONZE_NUGGET, FATags.Items.ALUMINUM_BRONZE_BLOCK,
				FATags.Items.ALUMINUM_BRONZE_SHEET, "aluminum_bronze", output);
		metal(FAItems.CHROMIUM_THINGS, FATags.Items.CHROMIUM_INGOT, FATags.Items.CHROMIUM_NUGGET, FATags.Items.CHROMIUM_BLOCK, FATags.Items.CHROMIUM_SHEET, "chromium", output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.CHOPPING_BLADE)
						   .pattern("rf")
						   .pattern("r ")
						   .define('r', FAItems.ROCK)
						   .define('f', Items.FLINT)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.FLINT_SHOVEL)
						   .pattern(" f ")
						   .pattern("psp")
						   .pattern(" s ")
						   .define('f', Items.FLINT)
						   .define('p', FAItems.PLANT_FIBER)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.FLINT_PICKAXE)
						   .pattern("fff")
						   .pattern("psp")
						   .pattern(" s ")
						   .define('f', Items.FLINT)
						   .define('p', FAItems.PLANT_FIBER)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.FLINT_AXE)
						   .pattern("ffp")
						   .pattern("fsp")
						   .pattern(" s ")
						   .define('f', Items.FLINT)
						   .define('p', FAItems.PLANT_FIBER)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.FLINT_HOE)
						   .pattern("ff ")
						   .pattern("psp")
						   .pattern(" s ")
						   .define('f', Items.FLINT)
						   .define('p', FAItems.PLANT_FIBER)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.FLINT_SWORD)
						   .pattern(" f ")
						   .pattern("pfp")
						   .pattern(" s ")
						   .define('f', Items.FLINT)
						   .define('p', FAItems.PLANT_FIBER)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_flint", has(Items.FLINT))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, FAItems.GREEN_ONION)
						   .pattern("w")
						   .define('w', FAItems.WILD_GREEN_ONION)
						   .unlockedBy("has_wild_green_onion", has(FAItems.WILD_GREEN_ONION))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.BAMBOO_BASKET)
						   .pattern("bbb")
						   .pattern("bbb")
						   .define('b', Items.BAMBOO)
						   .unlockedBy("has_bamboo", has(Items.BAMBOO))
						   .save(output);

		// shapeless

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, FAItems.FIREBOW)
							  .requires(Items.BOW)
							  .requires(Tags.Items.RODS_WOODEN)
							  .unlockedBy("has_bow", has(Items.BOW))
							  .save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.VANILLA_ICE_CREAM)
							  .requires(FAItems.MILK_ICE_CREAM)
							  .requires(Items.EGG)
							  .requires(Items.SUGAR)
							  .unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
							  .save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.CHOCOLATE_ICE_CREAM)
				.requires(FAItems.MILK_ICE_CREAM)
				.requires(Items.COCOA_BEANS)
				.requires(Items.SUGAR)
				.unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.MINT_ICE_CREAM)
				.requires(FAItems.MILK_ICE_CREAM)
				.requires(FAItems.MINT_LEAVES)
				.requires(Items.SUGAR)
				.unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.COFFEE_ICE_CREAM)
				.requires(FAItems.MILK_ICE_CREAM)
				.requires(FATags.Items.GROUND_COFFEE)
				.requires(Items.HONEY_BOTTLE)
				.unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.COOKIES_N_CREAM_ICE_CREAM)
				.requires(FAItems.MILK_ICE_CREAM)
				.requires(Items.COOKIE)
				.requires(Items.SUGAR)
				.unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.SWEETBERRY_ICE_CREAM)
				.requires(FAItems.MILK_ICE_CREAM)
				.requires(Items.SWEET_BERRIES)
				.requires(Items.SUGAR)
				.unlockedBy("has_ice_cream", has(FAItems.MILK_ICE_CREAM))
				.save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.HONEY_PANCAKE)
							  .requires(FAItems.PANCAKE)
							  .requires(Items.HONEY_BOTTLE)
							  .unlockedBy("has_pancake", has(FAItems.PANCAKE))
							  .save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.DOUFUNAO)
							  .requires(FATags.Items.TOFU)
							  .requires(Items.BOWL)
							  .unlockedBy("has_tofu", has(FATags.Items.TOFU))
							  .save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.SWEET_DOUFUNAO)
							  .requires(FAItems.DOUFUNAO)
							  .requires(FATags.Items.GINGER_CROP)
							  .requires(Ingredient.of(Items.SUGAR, Items.HONEY_BOTTLE))
							  .unlockedBy("has_doufunao", has(FAItems.DOUFUNAO))
							  .save(output, "sweet_doufunao_from_doufunao");

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.SWEET_DOUFUNAO)
							  .requires(FATags.Items.TOFU)
							  .requires(Items.BOWL)
							  .requires(FATags.Items.GINGER_CROP)
							  .requires(Ingredient.of(Items.SUGAR, Items.HONEY_BOTTLE))
							  .unlockedBy("has_tofu", has(FATags.Items.TOFU))
							  .save(output);

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.SALTY_DOUFUNAO)
							  .requires(FAItems.DOUFUNAO)
							  .requires(FATags.Items.SOY_SAUCE)
							  .unlockedBy("has_doufunao", has(FAItems.DOUFUNAO))
							  .save(output, "salty_doufunao_from_doufunao");

		ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, FAItems.SALTY_DOUFUNAO)
							  .requires(FATags.Items.TOFU)
							  .requires(Items.BOWL)
							  .requires(FATags.Items.SOY_SAUCE)
							  .unlockedBy("has_tofu", has(FATags.Items.TOFU))
							  .save(output);

		tool(FAItems.COPPER_SHOVEL, FAItems.COPPER_PICKAXE, FAItems.COPPER_AXE, FAItems.COPPER_HOE, FAItems.COPPER_SWORD, FAItems.COPPER_HAMMER, null, "copper",
				Tags.Items.INGOTS_COPPER, output);
		tool(null, null, null, null, null, FAItems.IRON_HAMMER, FAItems.IRON_WRENCH, "iron", Tags.Items.INGOTS_IRON, output);
		tool(FAItems.BRONZE_SHOVEL, FAItems.BRONZE_PICKAXE, FAItems.BRONZE_AXE, FAItems.BRONZE_HOE, FAItems.BRONZE_SWORD, FAItems.BRONZE_HAMMER, FAItems.BRONZE_WRENCH,
				"bronze", FATags.Items.BRONZE_INGOT, output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.COPPER_SHEARS)
						   .pattern(" i")
						   .pattern("i ")
						   .define('i', Tags.Items.INGOTS_COPPER)
						   .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.STONE_CRUCIBLE)
						   .pattern("c c")
						   .pattern("c c")
						   .pattern("ccc")
						   .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
						   .unlockedBy("has_cobblestone", has(ItemTags.STONE_CRAFTING_MATERIALS)).save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.STONE_CASTING_VESSEL)
						   .pattern("c c")
						   .pattern("sss")
						   .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
						   .define('s', cobbleSlabs)
						   .unlockedBy("has_cobblestone", has(ItemTags.STONE_CRAFTING_MATERIALS)).save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.STONE_WORKBENCH)
						   .pattern("sss")
						   .pattern("cwc")
						   .pattern("c c")
						   .define('c', Tags.Items.INGOTS_COPPER)
						   .define('s', Items.SMOOTH_STONE_SLAB)
						   .define('w', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
						   .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.BRICK_MAKER_FRAME)
						   .pattern("s s")
						   .pattern("ppp")
						   .define('p', ItemTags.PLANKS)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_planks", has(ItemTags.PLANKS)).save(output);

		// Workbench recipes
		WorkbenchRecipeBuilder.of(FAItems.LOG_PILE)
							  .pattern("lll")
							  .pattern("lll")
							  .pattern("lll")
							  .define('l', ItemTags.LOGS_THAT_BURN)
							  .unlockedBy("has_log", has(ItemTags.LOGS_THAT_BURN))
							  .save(output);

		screw(output, 4, FATags.Items.COPPER_NUGGET, FATags.Items.COPPER_ROD, "copper");
		screw(output, 6, Tags.Items.NUGGETS_IRON, FATags.Items.IRON_ROD, "iron");

		bushing(output, 4, FATags.Items.TIN_SHEET, "tin");

		WorkbenchRecipeBuilder.of(FAItems.GEARS.get(GearMaterial.WOOD))
							  .pattern(" s ")
							  .pattern("s s")
							  .pattern(" s ")
							  .define('s', Tags.Items.RODS_WOODEN)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_stick", has(Tags.Items.RODS_WOODEN))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.GEARS.get(GearMaterial.STONE))
							  .pattern(" s ")
							  .pattern("sgs")
							  .pattern(" s ")
							  .define('g', FATags.Items.WOOD_GEAR)
							  .define('s', ItemTags.STONE_CRAFTING_MATERIALS)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_wood_gear", has(FATags.Items.WOOD_GEAR))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.LEATHER_PULLEY_BELT)
							  .pattern(" ll")
							  .pattern("l l")
							  .pattern("ll ")
							  .define('l', FAItems.PROCESSED_LEATHER)
							  .tool("hammer", 1, 2)
							  .unlockedBy("has_processed_leather", has(FAItems.PROCESSED_LEATHER))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.LIMONITE_CHARCOAL_MIX)
							  .pattern("lc")
							  .pattern("cc")
							  .define('l', FAItems.RAW_LIMONITE)
							  .define('c', Items.CHARCOAL)
							  .tool("hammer", 1, 10)
							  .unlockedBy("has_limonite", has(FAItems.RAW_LIMONITE))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.IRON_SAND_CHARCOAL_MIX)
							.pattern("xox")
							.pattern("oxo")
							.pattern("xox")
							.define('x', FAItems.IRON_SAND)
							.define('o', Items.CHARCOAL)
							.tool("hammer", 1, 10)
							.unlockedBy("has_iron_sand", has(FAItems.IRON_SAND))
							.save(output);

		WorkbenchRecipeBuilder.of(FAItems.FIREBOW)
							  .pattern(" sr")
							  .pattern("ssr")
							  .pattern(" sr")
							  .define('s', Tags.Items.RODS_WOODEN)
							  .define('r', Tags.Items.STRINGS)
							  .unlockedBy("has_string", has(Tags.Items.STRINGS))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.BRICK_CRUCIBLE)
							  .pattern("i i")
							  .pattern("b b")
							  .pattern("bbb")
							  .define('b', Items.BRICKS)
							  .define('i', FATags.Items.IRON_ROD)
							  .tool("hammer", 1, 15)
							  .unlockedBy("has_bricks", has(Items.BRICKS))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.BRICK_FIREBOX)
							  .pattern("bbb")
							  .pattern("i i")
							  .pattern("bbb")
							  .define('b', Items.BRICKS)
							  .define('i', FATags.Items.IRON_SHEET)
							  .tool("hammer", 1, 15)
							  .unlockedBy("has_bricks", has(Items.BRICKS))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.BRICK_CASTING_VESSEL)
							  .pattern("i i")
							  .pattern("b b")
							  .pattern("sss")
							  .define('b', Items.BRICKS)
							  .define('s', Items.BRICK_SLAB)
							  .define('i', FATags.Items.IRON_SHEET)
							  .tool("hammer", 1, 15)
							  .unlockedBy("has_bricks", has(Items.BRICKS))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.WOOD_POWER_SHAFT)
							  .pattern("srs")
							  .define('s', ItemTags.PLANKS)
							  .define('r', FATags.Items.COPPER_ROD)
							  .tool("hammer", 2, 5)
							  .part("screw", 1, 2)
							  .part("bearing", 1, 1)
							  .unlockedBy("has_copper_rod", has(FATags.Items.COPPER_ROD))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.IRON_POWER_SHAFT, 3)
							  .pattern("sss")
							  .define('s', FATags.Items.IRON_ROD)
							  .tool("hammer", 2, 5)
							  .unlockedBy("has_iron_rod", has(FATags.Items.IRON_ROD))
							  .save(output);

		gearbox(output, FAItems.WOOD_GEARBOX, FATags.Items.IRON_ROD, FATags.Items.COPPER_SHEET, ItemTags.PLANKS);
		gearbox(output, FAItems.IRON_GEARBOX, FATags.Items.IRON_ROD, FATags.Items.IRON_SHEET, Tags.Items.INGOTS_IRON);

		splitter(output, FAItems.WOOD_SPLITTER, FATags.Items.IRON_ROD, FATags.Items.COPPER_GEAR, ItemTags.PLANKS);
		splitter(output, FAItems.IRON_SPLITTER, FATags.Items.IRON_ROD, FATags.Items.IRON_GEAR, Tags.Items.INGOTS_IRON);

		joiner(output, FAItems.WOOD_JOINER, FATags.Items.IRON_ROD, FATags.Items.COPPER_GEAR, ItemTags.PLANKS);
		joiner(output, FAItems.IRON_JOINER, FATags.Items.IRON_ROD, FATags.Items.IRON_GEAR, Tags.Items.INGOTS_IRON);

		bevelGear(output, FAItems.WOOD_BEVEL_GEAR, FATags.Items.IRON_ROD, FATags.Items.COPPER_GEAR, ItemTags.PLANKS);
		bevelGear(output, FAItems.IRON_BEVEL_GEAR, FATags.Items.IRON_ROD, FATags.Items.IRON_GEAR, Tags.Items.INGOTS_IRON);

		WorkbenchRecipeBuilder.of(FAItems.HAND_CRANK)
							  .pattern("ss")
							  .pattern(" s")
							  .pattern(" r")
							  .define('s', Tags.Items.RODS_WOODEN)
							  .define('r', FATags.Items.IRON_ROD)
							  .tool("hammer", 2, 5)
							  .part("screw", 1, 2)
							  .part("bearing", 1, 1)
							  .unlockedBy("has_iron_rod", has(FATags.Items.IRON_ROD))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.SMALL_WATERWHEEL)
							  .pattern("wcw")
							  .pattern("cgc")
							  .pattern("wcw")
							  .define('w', ItemTags.PLANKS)
							  .define('c', FATags.Items.COPPER_SHEET)
							  .define('g', FATags.Items.IRON_GEAR)
							  .tool("hammer", 2, 20)
							  .part("screw", 1, 8)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_iron_gear", has(FATags.Items.IRON_GEAR))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.LARGE_WATERWHEEL)
							  .pattern("iwi")
							  .pattern("wgw")
							  .pattern("iwi")
							  .define('w', ItemTags.LOGS_THAT_BURN)
							  .define('i', FATags.Items.IRON_SHEET)
							  .define('g', Tags.Items.STORAGE_BLOCKS_IRON)
							  .tool("hammer", 2, 20)
							  .part("screw", 1, 8)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_iron_block", has(Tags.Items.STORAGE_BLOCKS_IRON))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.MILLSTONE)
							  .pattern("ccc")
							  .pattern("srs")
							  .pattern("crc")
							  .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
							  .define('s', cobbleSlabs)
							  .define('r', FAItems.IRON_POWER_SHAFT)
							  .tool("hammer", 2, 25)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_power_shaft", has(FAItems.IRON_POWER_SHAFT))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.PAPER_BELLOWS)
							  .pattern("pww")
							  .pattern("p c")
							  .pattern("pww")
							  .define('p', Items.PAPER)
							  .define('w', ItemTags.PLANKS)
							  .define('c', FATags.Items.COPPER_SHEET)
							  .tool("hammer", 1, 5)
							  .part("screw", 1, 4)
							  .unlockedBy("has_copper_sheet", has(FATags.Items.COPPER_SHEET))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.LEATHER_BELLOWS)
							  .pattern("pww")
							  .pattern("p c")
							  .pattern("pww")
							  .define('p', FAItems.PROCESSED_LEATHER)
							  .define('w', ItemTags.PLANKS)
							  .define('c', FATags.Items.COPPER_SHEET)
							  .tool("hammer", 1, 5)
							  .part("screw", 1, 4)
							  .unlockedBy("has_copper_sheet", has(FATags.Items.COPPER_SHEET))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.TRIP_HAMMER)
							  .pattern("sas")
							  .pattern("sps")
							  .pattern("lll")
							  .define('s', FATags.Items.IRON_SHEET)
							  .define('a', Items.ANVIL)
							  .define('p', FAItems.IRON_THINGS.get(Form.PLATE_BLOCK))
							  .define('l', Items.SMOOTH_STONE_SLAB)
							  .tool("hammer", 2, 10)
							  .part("screw", 1, 4)
							  .unlockedBy("has_anvil", has(Items.ANVIL))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.WOODEN_TANK)
							  .pattern("ppp")
							  .pattern("pip")
							  .pattern("ppp")
							  .define('p', ItemTags.PLANKS)
							  .define('i', FATags.Items.IRON_ROD)
							  .tool("hammer", 2, 5)
							  .part("screw", 1, 8)
							  .unlockedBy("has_iron_rod", has(FATags.Items.IRON_ROD))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.TUMBLING_BARREL)
							  .pattern("i i")
							  .pattern("rbr")
							  .pattern("i i")
							  .define('r', FATags.Items.IRON_ROD)
							  .define('i', Tags.Items.INGOTS_IRON)
							  .define('b', FAItems.WOODEN_TANK)
							  .tool("hammer", 1, 5)
							  .tool("wrench", 1, 5)
							  .part("bushing", 1, 2)
							  .part("screw", 1, 4)
							  .unlockedBy("has_wooden_tank", has(FAItems.WOODEN_TANK))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.FRYING_PAN)
							  .pattern(" i ")
							  .pattern("isi")
							  .pattern("wi ")
							  .define('s', FATags.Items.IRON_SHEET)
							  .define('i', Tags.Items.INGOTS_IRON)
							  .define('w', Tags.Items.RODS_WOODEN)
							  .tool("hammer", 1, 5)
							  .part("screw", 1, 2)
							  .unlockedBy("has_iron_sheet", has(FATags.Items.IRON_SHEET))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.HORSE_ENGINE)
							  .pattern("ss ")
							  .pattern("psp")
							  .pattern("psp")
							  .define('s', FATags.Items.IRON_ROD)
							  .define('p', ItemTags.PLANKS)
							  .tool("hammer", 2, 5)
							  .part("screw", 1, 8)
							  .unlockedBy("has_iron_rod", has(FATags.Items.IRON_ROD))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.BRICK_KILN)
							  .pattern("bbb")
							  .pattern("s s")
							  .pattern("bbb")
							  .define('b', Items.BRICKS)
							  .define('s', FATags.Items.BRONZE_SHEET)
							  .tool("hammer", 2, 10)
							  .unlockedBy("has_bronze_sheet", has(FATags.Items.BRONZE_SHEET))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.STEAM_OVEN)
							  .pattern("bbb")
							  .pattern("bSb")
							  .pattern("bbb")
							  .define('b', FATags.Items.BRONZE_SHEET)
							  .define('S', Items.SMOKER)
							  .tool("hammer", 2, 10)
							  .part("screw", 1, 8)
							  .unlockedBy("has_bronze_sheet", has(FATags.Items.BRONZE_SHEET))
							  .save(output);

		pipeRecipe(output, FAItems.COPPER_PIPE, FATags.Items.COPPER_SHEET);

		// Vanilla furnace recipes
		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.BREAD), RecipeCategory.FOOD, FAItems.TOASTED_BREAD, 0.35f, 300)
								  .unlockedBy("has_bread", has(Items.BREAD))
								  .save(output, FactoryAutomation.name("campfire/toasted_bread"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(FATags.Items.CALCIUM_CARBONATE_DUST), RecipeCategory.MISC, FAItems.QUICKLIME, 1, 200)
								  .unlockedBy("has_calcium_carbonate", has(FATags.Items.CALCIUM_CARBONATE_DUST))
								  .save(output, FactoryAutomation.name("campfire/quicklime"));

		// Chopping block recipes
		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(Blocks.SHORT_GRASS), FAItems.PLANT_FIBER, 1).unlockedBy("has_short_grass", has(Blocks.SHORT_GRASS))
						   .save(output, FactoryAutomation.name("chopping_block/plant_fiber"));

		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(Items.BREAD), FAItems.SLICED_BREAD, 3).unlockedBy("has_bread", has(Items.BREAD))
				.save(output, FactoryAutomation.name("chopping_block/sliced_bread"));

		for (var wood : WoodTypes.values())
			ChoppingBlockRecipe.builder(RecipeCategory.BUILDING_BLOCKS, Ingredient.of(wood.getLogsTag()), wood.getPlanks(), 2).unlockedBy("has_log", has(wood.getLogsTag()))
							   .save(output, FactoryAutomation.name("chopping_block/" + wood.getName() + "_planks"));
		ChoppingBlockRecipe.builder(RecipeCategory.BUILDING_BLOCKS, Ingredient.of(ItemTags.BAMBOO_BLOCKS), Blocks.BAMBOO_PLANKS, 1)
						   .unlockedBy("has_bamboo_block", has(ItemTags.BAMBOO_BLOCKS))
						   .save(output, FactoryAutomation.name("chopping_block/bamboo_planks"));
		ChoppingBlockRecipe.builder(RecipeCategory.BUILDING_BLOCKS, Ingredient.of(ItemTags.CRIMSON_STEMS), Blocks.CRIMSON_PLANKS, 2)
						   .unlockedBy("has_crimson_stem", has(ItemTags.CRIMSON_STEMS))
						   .save(output, FactoryAutomation.name("chopping_block/crimson_planks"));
		ChoppingBlockRecipe.builder(RecipeCategory.BUILDING_BLOCKS, Ingredient.of(ItemTags.WARPED_STEMS), Blocks.WARPED_PLANKS, 2)
						   .unlockedBy("has_warped_stem", has(ItemTags.WARPED_STEMS))
						   .save(output, FactoryAutomation.name("chopping_block/warped_planks"));
		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(ItemTags.PLANKS), Items.STICK, 2).unlockedBy("has_planks", has(ItemTags.PLANKS))
						   .save(output, FactoryAutomation.name("chopping_block/sticks"));

		BrickDryingRecipe.of(Blocks.CLAY).input(Blocks.MUD).time(20 * 60 * 5).blocks(Blocks.MUD, Blocks.CLAY).unlockedBy("has_mud", has(Blocks.MUD)).save(output);
		BrickDryingRecipe.of(FAItems.MUD_BRICK).input(Blocks.PACKED_MUD).time(20 * 60 * 8).blocks(Blocks.PACKED_MUD, Blocks.MUD_BRICKS)
						 .unlockedBy("has_packed_mud", has(Blocks.PACKED_MUD)).save(output);
		BrickDryingRecipe.of(FAItems.DRIED_BRICK).input(Items.CLAY_BALL).time(20 * 60 * 3).blocks(Blocks.CLAY, FABlocks.DRIED_BRICKS.get())
						 .unlockedBy("has_clay_ball", has(Items.CLAY_BALL)).save(output);

		// Log pile firing

		LogPileFiringRecipe.of(Blocks.BRICKS).input(FABlocks.DRIED_BRICKS).beginData().logPileLike(FABlocks.LOG_PILE.get()).endData()
						   .unlockedBy("has_dried_bricks", has(FAItems.DRIED_BRICKS)).save(output);

		// Millstone

		MillstoneRecipe.of(new ItemStack(Items.BONE_MEAL, 4)).input(Tags.Items.BONES).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_bones", has(Tags.Items.BONES)).save(output);
		MillstoneRecipe.of(new ItemStack(Items.BLAZE_POWDER, 4)).input(Tags.Items.RODS_BLAZE).progress(80).beginData().speed(2).torque(100).endData()
					   .unlockedBy("has_blaze_rods", has(Tags.Items.RODS_BLAZE)).save(output);
		MillstoneRecipe.of(new ItemStack(Items.GLOWSTONE_DUST, 4)).input(Items.GLOWSTONE).progress(100).beginData().speed(1).torque(800).endData()
					   .unlockedBy("has_glowstone", has(Items.GLOWSTONE)).save(output);
		MillstoneRecipe.of(FAItems.WHEAT_FLOUR.toStack()).input(Tags.Items.CROPS_WHEAT).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT)).save(output);
		MillstoneRecipe.of(FAItems.CALCITE_DUST.toStack()).input(Items.CALCITE).progress(100).beginData().speed(1).torque(800).endData()
					   .unlockedBy("has_calcite", has(Items.CALCITE)).save(output);
		MillstoneRecipe.of(FAItems.GYPSUM_DUST.toStack()).input(FAItems.GYPSUM).progress(100).beginData().speed(1).torque(800).endData()
					   .unlockedBy("has_gypsum", has(FAItems.GYPSUM)).save(output);
		MillstoneRecipe.of(FAItems.TANBARK_DUST.toStack()).input(ItemTags.OAK_LOGS).progress(100).beginData().speed(1).torque(800).endData()
					   .unlockedBy("has_oak_logs", has(ItemTags.OAK_LOGS)).save(output);

		// dyes
		MillstoneRecipe.of(new ItemStack(Items.MAGENTA_DYE, 2)).input(Items.ALLIUM).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_allium", has(Items.ALLIUM)).saveNoteFrom(output, "allium");
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 2)).input(Items.BEETROOT).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_beetroot", has(Items.BEETROOT)).saveNoteFrom(output, "beetroot");
		MillstoneRecipe.of(new ItemStack(Items.LIGHT_BLUE_DYE, 2)).input(Items.BLUE_ORCHID).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_blue_orchid", has(Items.BLUE_ORCHID)).saveNoteFrom(output, "blue_orchid");
		MillstoneRecipe.of(new ItemStack(Items.BROWN_DYE, 2)).input(Items.COCOA_BEANS).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS)).saveNoteFrom(output, "cocoa_beans");
		MillstoneRecipe.of(new ItemStack(Items.BLUE_DYE, 2)).input(Items.CORNFLOWER).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_cornflower", has(Items.CORNFLOWER)).saveNoteFrom(output, "cornflower");
		MillstoneRecipe.of(new ItemStack(Items.YELLOW_DYE, 2)).input(Items.DANDELION).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_dandelion", has(Items.DANDELION)).saveNoteFrom(output, "dandelion");
		MillstoneRecipe.of(new ItemStack(Items.BLUE_DYE, 3)).input(Items.LAPIS_LAZULI).progress(100).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_lapis_lazuli", has(Items.LAPIS_LAZULI)).saveNoteFrom(output, "lapis_lazuli");
		var lightGrayFlower = Ingredient.of(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP);
		MillstoneRecipe.of(new ItemStack(Items.LIGHT_GRAY_DYE, 2)).input(lightGrayFlower).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_light_gray_flower", inventoryTrigger(ItemPredicate.Builder.item().of(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP)))
					   .saveNoteFrom(output, "light_gray_flower");
		MillstoneRecipe.of(new ItemStack(Items.MAGENTA_DYE, 4)).input(Items.LILAC).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_lilac", has(Items.LILAC)).saveNoteFrom(output, "lilac");
		MillstoneRecipe.of(new ItemStack(Items.WHITE_DYE, 2)).input(Items.LILY_OF_THE_VALLEY).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_lily_of_the_valley", has(Items.LILY_OF_THE_VALLEY)).saveNoteFrom(output, "lily_of_the_valley");
		MillstoneRecipe.of(new ItemStack(Items.ORANGE_DYE, 2)).input(Items.ORANGE_TULIP).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_orange_tulip", has(Items.ORANGE_TULIP)).saveNoteFrom(output, "orange_tulip");
		MillstoneRecipe.of(new ItemStack(Items.PINK_DYE, 2)).input(Items.PEONY).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_peony", has(Items.PEONY)).saveNoteFrom(output, "peony");
		MillstoneRecipe.of(new ItemStack(Items.PINK_DYE, 2)).input(Items.PINK_TULIP).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_pink_tulip", has(Items.PINK_TULIP)).saveNoteFrom(output, "pink_tulip");
		var redFlower = Ingredient.of(Items.POPPY, Items.RED_TULIP);
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 2)).input(redFlower).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_red_flower", inventoryTrigger(ItemPredicate.Builder.item().of(Items.POPPY, Items.RED_TULIP))).saveNoteFrom(output, "red_flower");
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 4)).input(Items.ROSE_BUSH).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_rose_bush", has(Items.ROSE_BUSH)).saveNoteFrom(output, "rose_bush");
		MillstoneRecipe.of(new ItemStack(Items.YELLOW_DYE, 4)).input(Items.SUNFLOWER).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_sunflower", has(Items.SUNFLOWER)).saveNoteFrom(output, "sunflower");
		MillstoneRecipe.of(new ItemStack(Items.BLACK_DYE, 2)).input(Items.WITHER_ROSE).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_wither_rose", has(Items.WITHER_ROSE)).saveNoteFrom(output, "wither_rose");

		MillstoneRecipe.of(FAItems.GROUND_SOYBEAN.toStack()).input(FAItems.SOYBEANS).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_soybeans", has(FAItems.SOYBEANS)).save(output);
		MillstoneRecipe.of(FAItems.GROUND_COFFEE.toStack()).input(FAItems.ROASTED_COFFEE_BEANS).progress(50).beginData().speed(1).torque(40).endData()
					   .unlockedBy("has_roasted_coffee_beans", has(FAItems.ROASTED_COFFEE_BEANS)).save(output);

		// Trip hammer
		TripHammerRecipe.of(new ItemStack(FAItems.IRON_RAIL.get())).input(FATags.Items.IRON_ROD).progress(100).beginData().endData()
						.unlockedBy("has_iron_rod", has(FATags.Items.IRON_ROD)).save(output);
		TripHammerRecipe.of(new ItemStack(FAItems.GOLD_RAIL.get())).input(FATags.Items.GOLD_ROD).progress(100).beginData().endData()
						.unlockedBy("has_gold_rod", has(FATags.Items.GOLD_ROD)).save(output);

		// Tumbling barrel
		TumblingBarrelRecipe.of(FAItems.MILK_ICE_CREAM.get())
							.fluidInput(NeoForgeMod.MILK.get(), 500)
							.input(Items.ICE)
							.time(50)
							.minSpeed(1)
							.maxSpeed(8)
							.unlockedBy("has_ice", has(Items.ICE))
							.unlockedBy("has_milk", has(Items.MILK_BUCKET))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.PANCAKE_BATTER_SOURCE.get(), 500)
							.fluidInput(NeoForgeMod.MILK.get(), 500)
							.input(FATags.Items.WHEAT_DUST)
							.time(40)
							.minSpeed(1)
							.maxSpeed(10)
							.unlockedBy("has_wheat_flour", has(FATags.Items.WHEAT_DUST))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.LIMEWATER_SOURCE.get(), 250)
							.fluidInput(Fluids.WATER, 250)
							.input(FATags.Items.QUICKLIME_DUST)
							.time(250)
							.minSpeed(1)
							.maxSpeed(10)
							.unlockedBy("has_quicklime", has(FATags.Items.QUICKLIME_DUST))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.TANNIN_SOURCE.get(), 100)
							.fluidInput(Fluids.WATER, 100)
							.input(FAItems.TANBARK_DUST)
							.time(200)
							.minSpeed(1)
							.maxSpeed(10)
							.unlockedBy("has_tanbark_dust", has(FAItems.TANBARK_DUST))
							.save(output);

		TumblingBarrelRecipe.of(FAItems.CLEANED_LEATHER)
							.fluidInput(FAFluids.LIMEWATER_SOURCE.get(), 500)
							.input(Items.LEATHER)
							.time(1000)
							.minSpeed(0.2f)
							.maxSpeed(0.5f)
							.unlockedBy("has_leather", has(Items.LEATHER))
							.save(output);

		TumblingBarrelRecipe.of(FAItems.PROCESSED_LEATHER)
							.fluidInput(FAFluids.TANNIN_SOURCE.get(), 500)
							.input(FAItems.CLEANED_LEATHER)
							.time(1000)
							.minSpeed(0.5f)
							.maxSpeed(1.5f)
							.unlockedBy("has_cleaned_leather", has(FAItems.CLEANED_LEATHER))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.BRINE_SOURCE.get(), 100)
							.fluidInput(Fluids.WATER, 100)
							.input(FATags.Items.SALT_DUST)
							.time(20 * 5)
							.minSpeed(2)
							.maxSpeed(10)
							.unlockedBy("has_salt", has(Items.SUGAR))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.SOY_MILK_SOURCE.get(), 250)
							.fluidInput(Fluids.WATER, 250)
							.input(FATags.Items.GROUND_SOYBEAN)
							.time(20 * 5)
							.minSpeed(2)
							.maxSpeed(10)
							.unlockedBy("has_ground_soybeans", has(FAItems.GROUND_SOYBEAN))
							.save(output);

		TumblingBarrelRecipe.of(FAItems.TOFU)
							.fluidInput(FAFluids.SOY_MILK_SOURCE.get(), 250)
							.input(FATags.Items.GYPSUM_DUST)
							.time(20 * 60)
							.minSpeed(0.2f)
							.maxSpeed(0.5f)
							.unlockedBy("has_gypsum", has(FATags.Items.GYPSUM_DUST))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.SOY_SAUCE_SOURCE.get(), 250)
							.fluidInput(FAFluids.BRINE_SOURCE.get(), 250)
							.input(FAItems.SOY_SAUCE_CULTURE)
							.time(20 * 60 * 2)
							.minSpeed(0.2f)
							.maxSpeed(0.5f)
							.unlockedBy("has_soy_sauce_culture", has(FAItems.SOY_SAUCE_CULTURE))
							.save(output);

		TumblingBarrelRecipe.of(FAFluids.COFFEE_SOURCE.get(), 250)
							.fluidInput(Fluids.WATER, 250)
							.input(FATags.Items.GROUND_COFFEE)
							.time(20 * 10)
							.minSpeed(0.5f)
							.maxSpeed(1)
							.unlockedBy("has_ground_coffee", has(FATags.Items.GROUND_COFFEE))
							.save(output);

		// Frying pan

		FryingPanRecipe.of(new ItemStack(Items.RABBIT_STEW))
					   .input(Items.RABBIT)
					   .input(Items.CARROT)
					   .input(Items.POTATO)
					   .input(Tags.Items.MUSHROOMS)
					   .progress(20 * 20)
					   .beginData().plate(Ingredient.of(Items.BOWL)).endData()
					   .unlockedBy("has_rabbit", has(Items.RABBIT))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.HAM_AND_EGGS.get()))
					   .input(Items.EGG)
					   .input(Items.EGG)
					   .input(FATags.Items.RAW_PORKS)
					   .progress(20 * 10)
					   .beginData().plate(Ingredient.of(Items.BOWL)).endData()
					   .unlockedBy("has_pork", has(Items.PORKCHOP))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.PANCAKE.get()))
					   .progress(20 * 10)
					   .beginData().liquid(FluidIngredient.single(FAFluids.PANCAKE_BATTER_SOURCE.get())).endData()
					   .unlockedBy("has_pancake_batter", has(FAItems.PANCAKE_BATTER_BOTTLE))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.SOY_SAUCE_CULTURE.get()))
					   .progress(20 * 10)
					   .input(FATags.Items.SOYBEAN_CROP)
					   .input(FATags.Items.WHEAT_DUST)
					   .beginData().liquid(FluidIngredient.single(Fluids.WATER)).endData()
					   .unlockedBy("has_soybeans", has(FAItems.SOYBEANS))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.ROASTED_COFFEE_BEANS.get()))
					   .progress(20 * 10)
					   .input(FAItems.GREEN_COFFEE_BEANS)
					   .beginData().endData()
					   .unlockedBy("has_green_coffee_beans", has(FAItems.GREEN_COFFEE_BEANS))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.YAKITORI.get()))
					   .progress(20 * 10)
					   .input(FATags.Items.RAW_CHICKEN)
					   .input(Items.SUGAR)
					   .input(FATags.Items.GREEN_ONION_FOOD)
					   .beginData().liquid(FluidIngredient.single(FAFluids.SOY_SAUCE_SOURCE)).plate(Ingredient.of(Items.STICK)).endData()
					   .unlockedBy("has_chicken", has(Items.CHICKEN))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.STEAMED_FISH.get()))
					   .input(Items.COD)
					   .input(FATags.Items.GREEN_ONION_FOOD)
					   .input(FATags.Items.GREEN_ONION_FOOD)
					   .input(FATags.Items.GINGER_CROP)
					   .progress(20 * 10)
					   .beginData().liquid(FluidIngredient.single(FAFluids.SOY_SAUCE_SOURCE)).plate(Ingredient.of(Items.BOWL)).endData()
					   .unlockedBy("has_cod", has(Items.COD))
					   .save(output);

		FryingPanRecipe.of(new ItemStack(FAItems.PANNED_TEA_LEAF.get()))
					   .input(FAItems.TEA_LEAF)
					   .progress(20 * 10)
					   .beginData().endData()
					   .unlockedBy("has_tea_leaf", has(FAItems.PANNED_TEA_LEAF))
					   .save(output);

		// Bamboo basket
		BasketDryingRecipe.of(new ItemStack(FAItems.GREEN_TEA_LEAF.get()))
						  .input(FAItems.PANNED_TEA_LEAF)
						  .progress(20 * 10)
						  .beginData().endData()
						  .unlockedBy("has_panned_tea_leaf", has(FAItems.PANNED_TEA_LEAF))
						  .save(output);

		BasketDryingRecipe.of(new ItemStack(FAItems.GREEN_COFFEE_BEANS.get()))
						  .input(FAItems.COFFEE_CHERRY)
						  .progress(20 * 10)
						  .beginData().endData()
						  .unlockedBy("has_coffee_cherry", has(FAItems.COFFEE_CHERRY))
						  .save(output);

		// Kiln recipes
		KilnRecipe.of(new ItemStack(Items.BRICK))
				  .progress(20 * 10)
				  .input(Items.CLAY_BALL)
				  .beginData().temperature(200).power(1000).endData() // TODO: fix temps
				  .unlockedBy("has_clay_ball", has(Items.CLAY_BALL))
				  .save(output);

		KilnRecipe.of(new ItemStack(Items.TERRACOTTA))
				  .progress(20 * 10 * 4)
				  .input(Items.CLAY)
				  .beginData().temperature(200).power(1000).endData() // TODO: fix temps
				  .unlockedBy("has_clay", has(Items.CLAY))
				  .save(output);

		KilnRecipe.of(new ItemStack(Items.CHARCOAL))
				  .progress(20 * 10)
				  .input(ItemTags.LOGS_THAT_BURN)
				  .beginData().temperature(330 + 273).power(4_300_000_300f / (8 * 10)).endData() // TODO: fix temps
				  .unlockedBy("has_logs", has(ItemTags.LOGS_THAT_BURN))
				  .save(output);

		KilnRecipe.of(new ItemStack(FAItems.COAL_COKE.get()))
				  .progress(20 * 10)
				  .input(Items.COAL)
				  .beginData().temperature(200).power(1000).endData() // TODO: fix temps
				  .unlockedBy("has_coal", has(Items.COAL))
				  .save(output);

		KilnRecipe.of(new ItemStack(FAItems.QUICKLIME.get()))
				  .progress(20 * 10)
				  .input(FATags.Items.CALCIUM_CARBONATE_DUST)
				  .beginData().temperature(200).power(1000).endData() // TODO: fix temps
				  .unlockedBy("has_calcium_carbonate", has(FATags.Items.CALCIUM_CARBONATE_DUST))
				  .save(output);

		KilnRecipe.of(new ItemStack(FAItems.CALCIUM_SULFATE_HEMIHYDRATE_DUST.get()))
				  .progress(20 * 10)
				  .input(FATags.Items.GYPSUM_DUST)
				  .beginData().temperature(130 + 273).power(1000).endData()
				  .unlockedBy("has_gypsum_dust", has(FATags.Items.GYPSUM_DUST))
				  .save(output);

		// Steam oven
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_BEEF)).input(Items.BEEF).progress(80).beginData().endData().unlockedBy("has_beef", has(Items.BEEF)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_CHICKEN)).input(Items.CHICKEN).progress(80).beginData().endData().unlockedBy("has_chicken", has(Items.CHICKEN)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_COD)).input(Items.COD).progress(80).beginData().endData().unlockedBy("has_cod", has(Items.COD)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.DRIED_KELP)).input(Items.KELP).progress(80).beginData().endData().unlockedBy("has_kelp", has(Items.KELP)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_SALMON)).input(Items.SALMON).progress(80).beginData().endData().unlockedBy("has_salmon", has(Items.SALMON)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_MUTTON)).input(Items.MUTTON).progress(80).beginData().endData().unlockedBy("has_mutton", has(Items.MUTTON)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_PORKCHOP)).input(Items.PORKCHOP).progress(80).beginData().endData().unlockedBy("has_porkchop", has(Items.PORKCHOP)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.BAKED_POTATO)).input(Items.POTATO).progress(80).beginData().endData().unlockedBy("has_potato", has(Items.POTATO)).save(output);
		SteamOvenRecipe.of(new ItemStack(Items.COOKED_RABBIT)).input(Items.RABBIT).progress(80).beginData().endData().unlockedBy("has_rabbit", has(Items.RABBIT)).save(output);
		SteamOvenRecipe.of(new ItemStack(FAItems.PANNED_TEA_LEAF.get())).input(FAItems.TEA_LEAF).progress(80).beginData().endData().unlockedBy("has_tea_leaf", has(FAItems.TEA_LEAF)).save(output);

		// Vanilla overrides
		for (var wood : WoodTypes.values())
			RemovalRecipe.unitFor(wood.getPlanks()).save(output);
		RemovalRecipe.unitFor(Blocks.BAMBOO_PLANKS).save(output);
		RemovalRecipe.unitFor(Blocks.CRIMSON_PLANKS).save(output);
		RemovalRecipe.unitFor(Blocks.WARPED_PLANKS).save(output);
		RemovalRecipe.unitFor(Items.STICK).save(output);
		RemovalRecipe.unitFor(Items.WOODEN_SHOVEL).save(output);
		RemovalRecipe.unitFor(Items.WOODEN_PICKAXE).save(output);
		RemovalRecipe.unitFor(Items.WOODEN_AXE).save(output);
		RemovalRecipe.unitFor(Items.WOODEN_HOE).save(output);
		RemovalRecipe.unitFor(Items.WOODEN_SWORD).save(output);
		// RemovalRecipe.unitFor(Blocks.MUD_BRICKS).save(output);

		// Smelting
		VanillaRecipeProvider.COAL_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.COAL).forSmelting(output, i));
		VanillaRecipeProvider.IRON_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.IRON_INGOT).forSmelting(output, i));
		VanillaRecipeProvider.COPPER_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.COPPER_INGOT).forSmelting(output, i));
		VanillaRecipeProvider.GOLD_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.GOLD_INGOT).forSmelting(output, i));
		VanillaRecipeProvider.DIAMOND_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.DIAMOND).forSmelting(output, i));
		VanillaRecipeProvider.LAPIS_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.LAPIS_LAZULI).forSmelting(output, i));
		VanillaRecipeProvider.REDSTONE_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.REDSTONE).forSmelting(output, i));
		VanillaRecipeProvider.EMERALD_SMELTABLES.forEach(i -> RemovalRecipe.unitFor(Items.EMERALD).forSmelting(output, i));
		RemovalRecipe.unitFor(Items.CHARCOAL).save(output);
		RemovalRecipe.unitFor(Items.BRICK).save(output);
	}

	private void stoneBricks(RecipeOutput output, Item stone, Item polishedStone, Map<StoneBlockForms, DeferredItem<BlockItem>> bricks)
	{
		var brick = bricks.get(StoneBlockForms.BLOCK);
		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, brick, polishedStone);
		slab(output, RecipeCategory.BUILDING_BLOCKS, bricks.get(StoneBlockForms.SLAB), brick);
		stairBuilder(bricks.get(StoneBlockForms.STAIRS), Ingredient.of(brick)).unlockedBy(getHasName(brick), has(brick)).save(output);
		wall(output, RecipeCategory.BUILDING_BLOCKS, bricks.get(StoneBlockForms.WALL), brick);
		bricks.forEach((form, b) ->
		{
			stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, b, stone, form.getCountWhenCut());
			stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, b, polishedStone, form.getCountWhenCut());
			if (b != brick)
				stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, b, brick, form.getCountWhenCut());
		});
	}

	private void tool(@Nullable ItemLike shovel, @Nullable ItemLike pickaxe, @Nullable ItemLike axe, @Nullable ItemLike hoe, @Nullable ItemLike sword, @Nullable ItemLike hammer,
					  @Nullable ItemLike wrench, String matName, TagKey<Item> canonicalMat, RecipeOutput output)
	{
		var mat = Ingredient.of(canonicalMat);
		if (shovel != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
							   .pattern("m")
							   .pattern("s")
							   .pattern("s")
							   .define('m', mat)
							   .define('s', Tags.Items.RODS_WOODEN)
							   .unlockedBy("has_" + matName, has(canonicalMat))
							   .save(output);

		if (pickaxe != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
							   .pattern("mmm")
							   .pattern(" s ")
							   .pattern(" s ")
							   .define('m', mat)
							   .define('s', Tags.Items.RODS_WOODEN)
							   .unlockedBy("has_" + matName, has(canonicalMat))
							   .save(output);

		if (axe != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
							   .pattern("mm")
							   .pattern("ms")
							   .pattern(" s")
							   .define('m', mat)
							   .define('s', Tags.Items.RODS_WOODEN)
							   .unlockedBy("has_" + matName, has(canonicalMat))
							   .save(output);

		if (hoe != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
							   .pattern("mm")
							   .pattern(" s")
							   .pattern(" s")
							   .define('m', mat)
							   .define('s', Tags.Items.RODS_WOODEN)
							   .unlockedBy("has_" + matName, has(canonicalMat))
							   .save(output);

		if (sword != null)
			ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, sword)
							   .pattern("m")
							   .pattern("m")
							   .pattern("s")
							   .define('m', mat)
							   .define('s', Tags.Items.RODS_WOODEN)
							   .unlockedBy("has_" + matName, has(canonicalMat))
							   .save(output);

		if (hammer != null)
			WorkbenchRecipeBuilder.of(hammer)
								  .pattern("mmm")
								  .pattern("msm")
								  .pattern(" s ")
								  .define('m', mat)
								  .define('s', Tags.Items.RODS_WOODEN)
								  .unlockedBy("has_" + matName, has(canonicalMat))
								  .save(output);

		if (wrench != null)
			WorkbenchRecipeBuilder.of(wrench)
								  .pattern("m m")
								  .pattern(" m ")
								  .pattern(" m ")
								  .define('m', mat)
								  .unlockedBy("has_" + matName, has(canonicalMat))
								  .save(output);
	}

	private void metal(Map<Form, DeferredItem<? extends Item>> things, TagKey<Item> ingot, TagKey<Item> nugget, TagKey<Item> block, TagKey<Item> sheet, String name,
					   RecipeOutput output)
	{
		if (things.containsKey(Form.INGOT))
			ingot(things.get(Form.INGOT), things.get(Form.NUGGET), ingot, nugget, name, output);
		if (things.containsKey(Form.BLOCK))
			block(things.get(Form.BLOCK), things.get(Form.INGOT), block, ingot, name, output);
		plateBlock(things.get(Form.PLATE_BLOCK), sheet, name, output);
		sheet(things.get(Form.SHEET), ingot, name, output);
		rod(things.get(Form.ROD), ingot, name, output);
		tripHammerMetals(things.get(Form.SHEET), things.get(Form.ROD), block, ingot, name, output);
	}

	private void ingot(ItemLike ingot, ItemLike nugget, TagKey<Item> ingotI, TagKey<Item> nuggetI, String name, RecipeOutput output)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
							  .requires(ingotI)
							  .group(name + "_nugget")
							  .unlockedBy("has_" + name + "_ingot", has(ingotI))
							  .save(output, FactoryAutomation.name(name + "_nugget_from_ingot"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', nuggetI)
						   .group(name + "_ingot")
						   .unlockedBy("has_" + name + "_nugget", has(nuggetI))
						   .save(output, FactoryAutomation.name(name + "_ingot_from_nuggets"));
	}

	private void ingot(ItemLike ingot, ItemLike nugget, String name, RecipeOutput output)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, nugget, 9)
							  .requires(ingot)
							  .group(name + "_nugget")
							  .unlockedBy("has_" + name + "_ingot", has(ingot))
							  .save(output, FactoryAutomation.name(name + "_nugget_from_ingot"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ingot)
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', nugget)
						   .group(name + "_ingot")
						   .unlockedBy("has_" + name + "_nugget", has(nugget))
						   .save(output, FactoryAutomation.name(name + "_ingot_from_nuggets"));
	}

	private void block(ItemLike block, ItemLike ingot, TagKey<Item> blockI, TagKey<Item> ingotI, String name, RecipeOutput output)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
							  .requires(blockI)
							  .group(name + "_ingot")
							  .unlockedBy("has_" + name + "_block", has(blockI))
							  .save(output, FactoryAutomation.name(name + "_ingot_from_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', ingotI)
						   .group(name + "_block")
						   .unlockedBy("has_" + name + "_ingot", has(ingotI))
						   .save(output, FactoryAutomation.name(name + "_block_from_ingots"));
	}

	private void block(ItemLike block, ItemLike ingot, String name, RecipeOutput output)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ingot, 9)
							  .requires(block)
							  .group(name + "_ingot")
							  .unlockedBy("has_" + name + "_block", has(block))
							  .save(output, FactoryAutomation.name(name + "_ingot_from_block"));
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, block)
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', ingot)
						   .group(name + "_block")
						   .unlockedBy("has_" + name + "_ingot", has(ingot))
						   .save(output, FactoryAutomation.name(name + "_block_from_ingots"));
	}

	private void plateBlock(ItemLike plateBlock, TagKey<Item> sheetI, String name, RecipeOutput output)
	{
		WorkbenchRecipeBuilder.of(plateBlock)
							  .pattern("sss")
							  .pattern("sss")
							  .pattern("sss")
							  .define('s', sheetI)
							  .tool("hammer", 1, 1)
							  .part("screw", 1, 6)
							  .unlockedBy("has_" + name + "_sheet", has(sheetI))
							  .save(output);
	}

	private void sheet(ItemLike sheet, TagKey<Item> ingotI, String name, RecipeOutput output)
	{
		WorkbenchRecipeBuilder.of(sheet)
							  .pattern("ss")
							  .define('s', ingotI)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_" + name + "_ingot", has(ingotI))
							  .save(output);
	}

	private void rod(ItemLike rod, TagKey<Item> ingotI, String name, RecipeOutput output)
	{
		WorkbenchRecipeBuilder.of(rod, 2)
							  .pattern("s")
							  .pattern("s")
							  .define('s', ingotI)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_" + name + "_ingot", has(ingotI))
							  .save(output);
	}

	private void tripHammerMetals(ItemLike sheet, ItemLike rod, TagKey<Item> blockI, TagKey<Item> ingotI, String name, RecipeOutput output)
	{
		TripHammerRecipe.of(new ItemStack(sheet, 6)).input(blockI).progress(100).beginData().endData()
						.unlockedBy("has_" + name + "_block", has(blockI)).save(output);
		TripHammerRecipe.of(new ItemStack(rod)).input(ingotI).progress(100).beginData().endData()
						.unlockedBy("has_" + name + "_ingot", has(ingotI)).save(output);
	}

	private void rawOre(ItemLike ore, ItemLike oreBlock, String name, RecipeOutput output)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ore, 9)
							  .requires(oreBlock)
							  .unlockedBy("has_" + name + "_block", has(oreBlock))
							  .save(output);
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, oreBlock)
						   .pattern("nnn")
						   .pattern("nnn")
						   .pattern("nnn")
						   .define('n', ore)
						   .unlockedBy("has_" + name, has(ore))
						   .save(output);
	}

	private void screw(RecipeOutput output, int count, TagKey<Item> nugget, TagKey<Item> rod, String name)
	{
		WorkbenchRecipeBuilder.of(FAItems.SCREW, count)
							  .pattern("nnn")
							  .pattern(" s ")
							  .define('n', nugget)
							  .define('s', rod)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_" + name + "_rod", has(rod))
							  .save(output, FactoryAutomation.name("screw_from_" + name));
	}

	private void bushing(RecipeOutput output, int count, TagKey<Item> sheet, String name)
	{
		WorkbenchRecipeBuilder.of(FAItems.BUSHING, count)
							  .pattern(" s ")
							  .pattern("s s")
							  .pattern(" s ")
							  .define('s', sheet)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_" + name + "_sheet", has(sheet))
							  .save(output, FactoryAutomation.name("bushing_from_" + name));
	}

	private void tallowMold(RecipeOutput output, Form form, Map<Form, DeferredItem<? extends Item>> forms, Map<Form, DeferredItem<? extends Item>> unfiredMolds)
	{
		ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, unfiredMolds.get(form)).requires(FAItems.GREEN_SAND).requires(forms.get(form)).unlockedBy("has_tallow_" + form.getName(), has(forms.get(form))).save(output);
	}

	private void firedTallowMold(RecipeOutput output, Form form, Map<Form, DeferredItem<? extends Item>> unfiredMolds, Map<Form, DeferredItem<? extends Item>> firedMolds)
	{
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(unfiredMolds.get(form)), RecipeCategory.MISC, firedMolds.get(form), 0, 200)
								  .unlockedBy("has_tallow_mold_" + form.getName(), has(unfiredMolds.get(form)))
								  .save(output, firedMolds.get(form).getId().withPrefix("smelting/"));
	}

	// mechanical
	private static void gearbox(RecipeOutput output, DeferredItem<BlockItem> gearbox, TagKey<Item> rod, TagKey<Item> sheet, TagKey<Item> baseMat)
	{
		WorkbenchRecipeBuilder.of(gearbox)
							  .pattern("psp")
							  .pattern("iii")
							  .define('s', rod)
							  .define('p', sheet)
							  .define('i', baseMat)
							  .tool("hammer", 2, 5)
							  .tool("wrench", 1, 5)
							  .part("screw", 1, 4)
							  .part("bearing", 1, 4)
							  .unlockedBy("has_rod", has(rod))
							  .save(output);
	}

	private static void splitter(RecipeOutput output, DeferredItem<BlockItem> splitter, TagKey<Item> rod, TagKey<Item> gear, TagKey<Item> baseMat)
	{
		WorkbenchRecipeBuilder.of(splitter)
							  .pattern("iii")
							  .pattern("sgs")
							  .pattern("isi")
							  .define('s', rod)
							  .define('g', gear)
							  .define('i', baseMat)
							  .tool("hammer", 2, 5)
							  .tool("wrench", 1, 5)
							  .part("screw", 1, 4)
							  .part("bearing", 1, 3)
							  .unlockedBy("has_gear", has(gear))
							  .save(output);
	}

	private static void joiner(RecipeOutput output, DeferredItem<BlockItem> joiner, TagKey<Item> rod, TagKey<Item> gear, TagKey<Item> baseMat)
	{
		WorkbenchRecipeBuilder.of(joiner)
							  .pattern("isi")
							  .pattern("gsg")
							  .pattern("iii")
							  .define('s', rod)
							  .define('g', gear)
							  .define('i', baseMat)
							  .tool("hammer", 2, 5)
							  .tool("wrench", 1, 5)
							  .part("screw", 1, 4)
							  .part("bearing", 1, 3)
							  .unlockedBy("has_gear", has(gear))
							  .save(output);
	}

	private static void bevelGear(RecipeOutput output, DeferredItem<BlockItem> bevelGear, TagKey<Item> rod, TagKey<Item> gear, TagKey<Item> baseMat)
	{
		WorkbenchRecipeBuilder.of(bevelGear, 2)
							  .pattern("igi")
							  .pattern("gsi")
							  .pattern("iii")
							  .define('s', rod)
							  .define('g', gear)
							  .define('i', baseMat)
							  .tool("hammer", 2, 5)
							  .tool("wrench", 1, 5)
							  .part("screw", 1, 4)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_gear", has(gear))
							  .save(output);
	}

	// pipe
	private void pipeRecipe(RecipeOutput output, DeferredItem<BlockItem> pipe, TagKey<Item> sheet)
	{
		WorkbenchRecipeBuilder.of(pipe, 6)
							  .pattern("sss")
							  .pattern("   ")
							  .pattern("sss")
							  .define('s', sheet)
							  .tool("wrench", 1, 5)
							  .part("screw", 1, 4)
							  .unlockedBy("has_sheet", has(sheet))
							  .save(output);
	}
}
