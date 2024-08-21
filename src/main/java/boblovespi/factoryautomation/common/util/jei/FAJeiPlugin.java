package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.category.CastingJeiCategory;
import boblovespi.factoryautomation.common.util.jei.category.ChoppingBlockJeiCategory;
import boblovespi.factoryautomation.common.util.jei.category.WorkbenchJeiCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

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

		var castingRecipes = new ArrayList<CastingJeiRecipe>();
		CasterType.STONE.efficiencies().forEach((f, e) -> castingRecipes.add(new CastingJeiRecipe(f, CasterType.STONE, e, FAItems.GREEN_SAND.toStack())));
		registration.addRecipes(castingJeiCategory.getRecipeType(), castingRecipes);
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		FAItems.CHOPPING_BLOCKS.values().forEach(b -> registration.addRecipeCatalyst(b.toStack(), choppingBlockJeiCategory.getRecipeType()));
		registration.addRecipeCatalyst(FAItems.STONE_WORKBENCH.toStack(), workbenchJeiCategory.getRecipeType());
		registration.addRecipeCatalyst(FAItems.STONE_CASTING_VESSEL.toStack(), castingJeiCategory.getRecipeType());
	}

}
