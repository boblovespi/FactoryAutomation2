package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.category.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.stream.Collectors;

@SuppressWarnings("NotNullFieldNotInitialized")
@JeiPlugin
public class FAJeiPlugin implements IModPlugin
{
	public static final IIngredientType<MetalStack> METAL_INGREDIENT = new MetalIngredient();
	private ChoppingBlockJeiCategory choppingBlockJeiCategory;
	private WorkbenchJeiCategory workbenchJeiCategory;
	private CastingJeiCategory castingJeiCategory;
	private MeltingJeiCategory meltingJeiCategory;
	private BrickDryingJeiCategory brickDryingJeiCategory;
	private MillstoneJeiCategory millstoneJeiCategory;

	@Override
	public ResourceLocation getPluginUid()
	{
		return FactoryAutomation.name("jei_plugin");
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration)
	{
		var guiHelper = registration.getJeiHelpers().getGuiHelper();
		choppingBlockJeiCategory = new ChoppingBlockJeiCategory(guiHelper);
		registration.addRecipeCategories(choppingBlockJeiCategory);
		workbenchJeiCategory = new WorkbenchJeiCategory(guiHelper);
		registration.addRecipeCategories(workbenchJeiCategory);
		castingJeiCategory = new CastingJeiCategory(guiHelper);
		registration.addRecipeCategories(castingJeiCategory);
		meltingJeiCategory = new MeltingJeiCategory(guiHelper);
		registration.addRecipeCategories(meltingJeiCategory);
		brickDryingJeiCategory = new BrickDryingJeiCategory(guiHelper);
		registration.addRecipeCategories(brickDryingJeiCategory);
		millstoneJeiCategory = new MillstoneJeiCategory(guiHelper);
		registration.addRecipeCategories(millstoneJeiCategory);
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration)
	{
		registration.register(METAL_INGREDIENT, Metal.allMetals().stream().map(k -> new MetalStack(k, 18)).collect(Collectors.toList()), new MetalIngredientHelper(),
				MetalIngredientRenderer.create());
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration)
	{
		RecipeManager recipeManager;
		try
		{
			var minecraft = Minecraft.getInstance();
			var level = minecraft.level;
			recipeManager = level.getRecipeManager();
		} catch (NullPointerException e)
		{
			FactoryAutomation.LOGGER.error("Somehow something is null, so we couldn't find the recipes :(", e);
			return;
		}

		registration.addRecipes(choppingBlockJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.CHOPPING_BLOCK_TYPE.get()));
		registration.addRecipes(workbenchJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.WORKBENCH_RECIPE_TYPE.get()));
		registration.addRecipes(brickDryingJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.BRICK_DRYING_TYPE.get()));
		registration.addRecipes(millstoneJeiCategory.getRecipeType(), recipeManager.getAllRecipesFor(RecipeThings.MILLSTONE_TYPE.get()));

		var castingRecipes = new ArrayList<CastingJeiRecipe>();
		CasterType.STONE.efficiencies().forEach((f, e) -> castingRecipes.add(new CastingJeiRecipe(f, CasterType.STONE, e, FAItems.GREEN_SAND.toStack())));
		registration.addRecipes(castingJeiCategory.getRecipeType(), castingRecipes);

		var meltingRecipes = new ArrayList<MeltingJeiRecipe>();
		meltingRecipes.add(new MeltingJeiRecipe(Ingredient.of(Tags.Items.RAW_MATERIALS_COPPER), Metal.COPPER.meltTemp(), Metal.COPPER, Form.RAW_ORE));
		registration.addRecipes(meltingJeiCategory.getRecipeType(), meltingRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		FAItems.CHOPPING_BLOCKS.values().forEach(b -> registration.addRecipeCatalyst(b.toStack(), choppingBlockJeiCategory.getRecipeType()));
		registration.addRecipeCatalyst(FAItems.STONE_WORKBENCH.toStack(), workbenchJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.STONE_CASTING_VESSEL.toStack(), castingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.STONE_CRUCIBLE.toStack(), meltingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.BRICK_MAKER_FRAME.toStack(), brickDryingJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.MILLSTONE.toStack(), millstoneJeiCategory.getRecipeType());
	}

}
