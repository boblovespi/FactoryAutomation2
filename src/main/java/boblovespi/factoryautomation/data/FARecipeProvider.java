package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
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

		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(Blocks.SHORT_GRASS), FAItems.PLANT_FIBER, 1).unlockedBy("has_short_grass", has(Blocks.SHORT_GRASS))
						   .save(output, FactoryAutomation.name("chopping_block/plant_fiber"));

		for (var wood : WoodTypes.values())
			ChoppingBlockRecipe.builder(RecipeCategory.BUILDING_BLOCKS, Ingredient.of(wood.getLogsTag()), wood.getPlanks(), 2).unlockedBy("has_log", has(wood.getLogsTag()))
							   .save(output, FactoryAutomation.name("chopping_block/" + wood.getName() + "_planks"));
		ChoppingBlockRecipe.builder(RecipeCategory.MISC, Ingredient.of(ItemTags.PLANKS), Items.STICK, 2).unlockedBy("has_planks", has(ItemTags.PLANKS))
						   .save(output, FactoryAutomation.name("chopping_block/sticks"));
	}
}
