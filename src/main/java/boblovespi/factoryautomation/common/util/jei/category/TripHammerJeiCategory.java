package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.TripHammerRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;

public class TripHammerJeiCategory extends FAJeiCategory<TripHammerRecipe>
{
	public TripHammerJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.TRIP_HAMMER_TYPE.get(), helper, FABlocks.TRIP_HAMMER, FactoryAutomation.locString("jei", "trip_hammer.name"));
	}

	@Override
	protected IDrawable createBackground()
	{
		return helper.createDrawable(FactoryAutomation.name("textures/gui/jei/milling.png"), 0, 0, 123, 74);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<TripHammerRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15, 19 + 2).addIngredients(recipe.value().getInput());
		var resultItem = getResultItem(recipe.value());
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10);
		out.addItemStack(resultItem);
	}
}
