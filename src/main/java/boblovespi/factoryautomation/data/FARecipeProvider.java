package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
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
	}
}
