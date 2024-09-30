package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.*;
import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;
import vazkii.patchouli.api.PatchouliAPI;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("SameParameterValue")
public class FARecipeProvider extends RecipeProvider
{
	public FARecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries)
	{
		super(pOutput, pRegistries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		// common ingredients
		var cobbleSlabs = Ingredient.of(Items.COBBLESTONE_SLAB, Items.BLACKSTONE_SLAB, Items.COBBLED_DEEPSLATE_SLAB);

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

		ingot(Items.COPPER_INGOT, FAItems.COPPER_THINGS.get(Form.NUGGET), Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, "copper", output);
		metal(FAItems.COPPER_THINGS, Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, Tags.Items.STORAGE_BLOCKS_COPPER, FATags.Items.COPPER_SHEET, "copper", output);
		metal(FAItems.TIN_THINGS, FATags.Items.TIN_INGOT, FATags.Items.TIN_NUGGET, FATags.Items.TIN_BLOCK, FATags.Items.TIN_SHEET, "tin", output);
		metal(FAItems.IRON_THINGS, Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON, Tags.Items.STORAGE_BLOCKS_IRON, FATags.Items.IRON_SHEET, "iron", output);
		metal(FAItems.BRONZE_THINGS, FATags.Items.BRONZE_INGOT, FATags.Items.BRONZE_NUGGET, FATags.Items.BRONZE_BLOCK, FATags.Items.BRONZE_SHEET, "bronze", output);

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

		ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, FAItems.FIREBOW)
							  .requires(Items.BOW)
							  .requires(Tags.Items.RODS_WOODEN)
							  .unlockedBy("has_bow", has(Items.BOW))
							  .save(output);

		tool(FAItems.COPPER_SHOVEL, FAItems.COPPER_PICKAXE, FAItems.COPPER_AXE, FAItems.COPPER_HOE, FAItems.COPPER_SWORD, FAItems.COPPER_HAMMER, null, "copper",
				Tags.Items.INGOTS_COPPER, output);
		tool(null, null, null, null, null, FAItems.IRON_HAMMER, FAItems.IRON_WRENCH, "iron", Tags.Items.INGOTS_IRON, output);

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

		// Workbench
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

		WorkbenchRecipeBuilder.of(FAItems.LIMONITE_CHARCOAL_MIX)
							  .pattern("lc")
							  .pattern("cc")
							  .define('l', FAItems.RAW_LIMONITE)
							  .define('c', Items.CHARCOAL)
							  .tool("hammer", 1, 10)
							  .unlockedBy("has_limonite", has(FAItems.RAW_LIMONITE))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.FIREBOW)
							  .pattern(" sr")
							  .pattern("ssr")
							  .pattern(" sr")
							  .define('s', Tags.Items.RODS_WOODEN)
							  .define('r', Tags.Items.STRINGS)
							  .unlockedBy("has_string", has(Tags.Items.STRINGS))
							  .save(output);

		WorkbenchRecipeBuilder.of(FAItems.WOOD_POWER_SHAFT)
							  .pattern("srs")
							  .define('s', ItemTags.PLANKS)
							  .define('r', FATags.Items.COPPER_ROD)
							  .tool("hammer", 2, 5)
							  .part("screw", 1, 2)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_copper_rod", has(FATags.Items.COPPER_ROD))
							  .save(output);

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

		WorkbenchRecipeBuilder.of(FAItems.MILLSTONE)
							  .pattern("ccc")
							  .pattern("srs")
							  .pattern("crc")
							  .define('c', ItemTags.STONE_CRAFTING_MATERIALS)
							  .define('s', cobbleSlabs)
							  .define('r', FAItems.WOOD_POWER_SHAFT) // TODO: replace with iron power shaft
							  .tool("hammer", 2, 25)
							  .part("bearing", 1, 2)
							  .unlockedBy("has_power_shaft", has(FAItems.WOOD_POWER_SHAFT))
							  .save(output);

		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.BREAD), RecipeCategory.FOOD, FAItems.TOASTED_BREAD, 0.35f, 300)
								  .unlockedBy("has_bread", has(Items.BREAD))
								  .save(output, FactoryAutomation.name("campfire/toasted_bread"));
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(FATags.Items.CALCIUM_CARBONATE_DUST), RecipeCategory.MISC, FAItems.QUICKLIME, 1, 200)
								  .unlockedBy("has_calcium_carbonate", has(FATags.Items.CALCIUM_CARBONATE_DUST))
								  .save(output, FactoryAutomation.name("campfire/quicklime"));

		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(Blocks.SHORT_GRASS), FAItems.PLANT_FIBER, 1).unlockedBy("has_short_grass", has(Blocks.SHORT_GRASS))
						   .save(output, FactoryAutomation.name("chopping_block/plant_fiber"));

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

		BrickDryingRecipe.of(Blocks.DIRT).input(Blocks.MUD).time(20 * 5).blocks(Blocks.MUD, Blocks.DIRT).unlockedBy("has_mud", has(Blocks.MUD)).save(output);

		// Millstone

		MillstoneRecipe.of(new ItemStack(Items.BONE_MEAL, 4)).input(Tags.Items.BONES).progress(30).beginData().speed(1).torque(5).endData()
					   .unlockedBy("has_bones", has(Tags.Items.BONES)).save(output);
		MillstoneRecipe.of(new ItemStack(Items.BLAZE_POWDER, 4)).input(Tags.Items.RODS_BLAZE).progress(50).beginData().speed(1).torque(4).endData()
					   .unlockedBy("has_blaze_rods", has(Tags.Items.RODS_BLAZE)).save(output);
		MillstoneRecipe.of(new ItemStack(Items.GLOWSTONE_DUST, 4)).input(Items.GLOWSTONE).progress(50).beginData().speed(1).torque(4).endData()
					   .unlockedBy("has_glowstone", has(Items.GLOWSTONE)).save(output);
		MillstoneRecipe.of(FAItems.WHEAT_FLOUR.toStack()).input(Tags.Items.CROPS_WHEAT).progress(50).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_wheat", has(Tags.Items.CROPS_WHEAT)).save(output);
		MillstoneRecipe.of(FAItems.CALCITE_DUST.toStack()).input(Items.CALCITE).progress(50).beginData().speed(1).torque(10).endData()
					   .unlockedBy("has_calcite", has(Items.CALCITE)).save(output);

		// dyes
		MillstoneRecipe.of(new ItemStack(Items.MAGENTA_DYE, 2)).input(Items.ALLIUM).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_allium", has(Items.ALLIUM)).saveNoteFrom(output, "allium");
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 2)).input(Items.BEETROOT).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_beetroot", has(Items.BEETROOT)).saveNoteFrom(output, "beetroot");
		MillstoneRecipe.of(new ItemStack(Items.LIGHT_BLUE_DYE, 2)).input(Items.BLUE_ORCHID).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_blue_orchid", has(Items.BLUE_ORCHID)).saveNoteFrom(output, "blue_orchid");
		MillstoneRecipe.of(new ItemStack(Items.BROWN_DYE, 2)).input(Items.COCOA_BEANS).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_cocoa_beans", has(Items.COCOA_BEANS)).saveNoteFrom(output, "cocoa_beans");
		MillstoneRecipe.of(new ItemStack(Items.BLUE_DYE, 2)).input(Items.CORNFLOWER).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_cornflower", has(Items.CORNFLOWER)).saveNoteFrom(output, "cornflower");
		MillstoneRecipe.of(new ItemStack(Items.YELLOW_DYE, 2)).input(Items.DANDELION).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_dandelion", has(Items.DANDELION)).saveNoteFrom(output, "dandelion");
		MillstoneRecipe.of(new ItemStack(Items.BLUE_DYE, 3)).input(Items.LAPIS_LAZULI).progress(20).beginData().speed(1).torque(2).endData()
					   .unlockedBy("has_lapis_lazuli", has(Items.LAPIS_LAZULI)).saveNoteFrom(output, "lapis_lazuli");
		var lightGrayFlower = Ingredient.of(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP);
		MillstoneRecipe.of(new ItemStack(Items.LIGHT_GRAY_DYE, 2)).input(lightGrayFlower).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_light_gray_flower", inventoryTrigger(ItemPredicate.Builder.item().of(Items.AZURE_BLUET, Items.OXEYE_DAISY, Items.WHITE_TULIP)))
					   .saveNoteFrom(output, "light_gray_flower");
		MillstoneRecipe.of(new ItemStack(Items.MAGENTA_DYE, 4)).input(Items.LILAC).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_lilac", has(Items.LILAC)).saveNoteFrom(output, "lilac");
		MillstoneRecipe.of(new ItemStack(Items.WHITE_DYE, 2)).input(Items.LILY_OF_THE_VALLEY).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_lily_of_the_valley", has(Items.LILY_OF_THE_VALLEY)).saveNoteFrom(output, "lily_of_the_valley");
		MillstoneRecipe.of(new ItemStack(Items.ORANGE_DYE, 2)).input(Items.ORANGE_TULIP).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_orange_tulip", has(Items.ORANGE_TULIP)).saveNoteFrom(output, "orange_tulip");
		MillstoneRecipe.of(new ItemStack(Items.PINK_DYE, 2)).input(Items.PEONY).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_peony", has(Items.PEONY)).saveNoteFrom(output, "peony");
		MillstoneRecipe.of(new ItemStack(Items.PINK_DYE, 2)).input(Items.PINK_TULIP).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_pink_tulip", has(Items.PINK_TULIP)).saveNoteFrom(output, "pink_tulip");
		var redFlower = Ingredient.of(Items.POPPY, Items.RED_TULIP);
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 2)).input(redFlower).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_red_flower", inventoryTrigger(ItemPredicate.Builder.item().of(Items.POPPY, Items.RED_TULIP))).saveNoteFrom(output, "red_flower");
		MillstoneRecipe.of(new ItemStack(Items.RED_DYE, 4)).input(Items.ROSE_BUSH).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_rose_bush", has(Items.ROSE_BUSH)).saveNoteFrom(output, "rose_bush");
		MillstoneRecipe.of(new ItemStack(Items.YELLOW_DYE, 4)).input(Items.SUNFLOWER).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_sunflower", has(Items.SUNFLOWER)).saveNoteFrom(output, "sunflower");
		MillstoneRecipe.of(new ItemStack(Items.BLACK_DYE, 2)).input(Items.WITHER_ROSE).progress(10).beginData().speed(1).torque(1).endData()
					   .unlockedBy("has_wither_rose", has(Items.WITHER_ROSE)).saveNoteFrom(output, "wither_rose");

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
		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, unfiredMolds.get(form))
						   .pattern("s s")
						   .pattern("sms")
						   .pattern("sss")
						   .define('s', FAItems.GREEN_SAND)
						   .define('m', forms.get(form))
						   .unlockedBy("has_tallow_" + form.getName(), has(forms.get(form)))
						   .save(output);
	}

	private void firedTallowMold(RecipeOutput output, Form form, Map<Form, DeferredItem<? extends Item>> unfiredMolds, Map<Form, DeferredItem<? extends Item>> firedMolds)
	{
		SimpleCookingRecipeBuilder.smelting(Ingredient.of(unfiredMolds.get(form)), RecipeCategory.MISC, firedMolds.get(form), 0, 200)
								  .unlockedBy("has_tallow_mold_" + form.getName(), has(unfiredMolds.get(form)))
								  .save(output, firedMolds.get(form).getId().withPrefix("smelting/"));
	}
}
