package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.recipe.RemovalRecipe;
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
import vazkii.patchouli.api.PatchouliAPI;

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
						   .save(output);

		twoByTwoPacker(output, RecipeCategory.BUILDING_BLOCKS, Blocks.GRAVEL, FAItems.ROCK);
		ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, FABlocks.GREEN_SAND, 4)
						   .pattern("sc")
						   .pattern("cs")
						   .define('c', Blocks.CLAY)
						   .define('s', Tags.Items.SANDS_COLORLESS)
						   .unlockedBy("has_clay", has(Blocks.CLAY))
						   .save(output);

		ingot(Items.COPPER_INGOT, FAItems.COPPER_THINGS.get(Form.NUGGET), Tags.Items.INGOTS_COPPER, FATags.Items.COPPER_NUGGET, "copper", output);

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

		tool(FAItems.COPPER_SHOVEL, FAItems.COPPER_PICKAXE, FAItems.COPPER_AXE, FAItems.COPPER_HOE, FAItems.COPPER_SWORD, Ingredient.of(Tags.Items.INGOTS_COPPER), "copper",
				Tags.Items.INGOTS_COPPER, output);

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

	private void tool(ItemLike shovel, ItemLike pickaxe, ItemLike axe, ItemLike hoe, ItemLike sword, Ingredient mat, String matName, TagKey<Item> canonicalMat, RecipeOutput output)
	{
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
}
