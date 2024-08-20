package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.util.jei.category.ChoppingBlockJeiCategory;
import boblovespi.factoryautomation.common.util.jei.category.WorkbenchJeiCategory;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

@SuppressWarnings("NotNullFieldNotInitialized")
@JeiPlugin
public class FAJeiPlugin implements IModPlugin
{
	private ChoppingBlockJeiCategory choppingBlockJeiCategory;
	private WorkbenchJeiCategory workbenchJeiCategory;

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
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration)
	{
		FAItems.CHOPPING_BLOCKS.values().forEach(b -> registration.addRecipeCatalyst(b.toStack(), choppingBlockJeiCategory.getRecipeType()));
		registration.addRecipeCatalyst(FAItems.STONE_WORKBENCH.toStack(), workbenchJeiCategory.getRecipeType());
	}
}
