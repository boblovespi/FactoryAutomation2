package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.BrickDryingRecipe;
import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.recipe.RemovalRecipe;
import boblovespi.factoryautomation.common.recipe.WorkbenchRecipeBuilder;
import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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

public class FARecipeProvider extends RecipeProvider
{
	public FARecipeProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries)
	{
		super(pOutput, pRegistries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output)
	{
		// TODO: make not break if patchouli is not loaded
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

		rawOre(FAItems.RAW_CASSITERITE, FAItems.RAW_CASSITERITE_BLOCK, "raw_cassiterite", output);
		rawOre(FAItems.RAW_LIMONITE, FAItems.RAW_LIMONITE_BLOCK, "raw_limonite", output);

		ingot(Items.COPPER_INGOT, FAItems.COPPER_THINGS.get(Form.NUGGET), Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, "copper", output);
		metal(FAItems.COPPER_THINGS, Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, Tags.Items.STORAGE_BLOCKS_COPPER, FATags.Items.COPPER_SHEET, "copper", output);
		metal(FAItems.TIN_THINGS, FATags.Items.TIN_INGOT, FATags.Items.TIN_NUGGET, FATags.Items.TIN_BLOCK, FATags.Items.TIN_SHEET, "tin", output);
		metal(FAItems.IRON_THINGS, Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON, Tags.Items.STORAGE_BLOCKS_IRON, FATags.Items.IRON_SHEET, "iron", output);

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

		tool(FAItems.COPPER_SHOVEL, FAItems.COPPER_PICKAXE, FAItems.COPPER_AXE, FAItems.COPPER_HOE, FAItems.COPPER_SWORD, FAItems.COPPER_HAMMER, "copper", Tags.Items.INGOTS_COPPER,
				output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, FAItems.COPPER_SHEARS)
						   .pattern(" i")
						   .pattern("i ")
						   .define('i', Tags.Items.INGOTS_COPPER)
						   .unlockedBy("has_copper_ingot", has(Tags.Items.INGOTS_COPPER))
						   .save(output);

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
						   .define('s', Ingredient.of(Items.COBBLESTONE_SLAB, Items.BLACKSTONE_SLAB, Items.COBBLED_DEEPSLATE_SLAB))
						   .unlockedBy("has_cobblestone", has(ItemTags.STONE_CRAFTING_MATERIALS)).save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.MISC, FAItems.STONE_WORKBENCH)
						   .pattern("sss")
						   .pattern("cwc")
						   .pattern("c c")
						   .define('c', Tags.Items.INGOTS_COPPER)
						   .define('s', Items.SMOOTH_STONE_SLAB)
						   .define('w', Tags.Items.PLAYER_WORKSTATIONS_CRAFTING_TABLES)
						   .unlockedBy("has_copper", has(Tags.Items.INGOTS_COPPER)).save(output);

		SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(Items.BREAD), RecipeCategory.FOOD, FAItems.TOASTED_BREAD, 0.35f, 300)
								  .unlockedBy("has_bread", has(Items.BREAD))
								  .save(output, FactoryAutomation.name("campfire/toasted_bread"));

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

	private void tool(ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe, ItemLike sword, @Nullable ItemLike hammer, String matName, TagKey<Item> canonicalMat,
					  RecipeOutput output)
	{
		var mat = Ingredient.of(canonicalMat);
		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, shovel)
						   .pattern("m")
						   .pattern("s")
						   .pattern("s")
						   .define('m', mat)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_" + matName, has(canonicalMat))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pickaxe)
						   .pattern("mmm")
						   .pattern(" s ")
						   .pattern(" s ")
						   .define('m', mat)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_" + matName, has(canonicalMat))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, axe)
						   .pattern("mm")
						   .pattern("ms")
						   .pattern(" s")
						   .define('m', mat)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_" + matName, has(canonicalMat))
						   .save(output);

		ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, hoe)
						   .pattern("mm")
						   .pattern(" s")
						   .pattern(" s")
						   .define('m', mat)
						   .define('s', Tags.Items.RODS_WOODEN)
						   .unlockedBy("has_" + matName, has(canonicalMat))
						   .save(output);

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

	private static void plateBlock(ItemLike plateBlock, TagKey<Item> sheetI, String name, RecipeOutput output)
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

	private static void sheet(ItemLike sheet, TagKey<Item> ingotI, String name, RecipeOutput output)
	{
		WorkbenchRecipeBuilder.of(sheet)
							  .pattern("ss")
							  .define('s', ingotI)
							  .tool("hammer", 1, 1)
							  .unlockedBy("has_" + name + "_ingot", has(ingotI))
							  .save(output);
	}

	private static void rod(ItemLike rod, TagKey<Item> ingotI, String name, RecipeOutput output)
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

	private static void screw(RecipeOutput output, int count, TagKey<Item> nugget, TagKey<Item> rod, String name)
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

	private static void bushing(RecipeOutput output, int count, TagKey<Item> sheet, String name)
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
}
