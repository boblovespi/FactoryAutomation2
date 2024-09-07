package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.MillstoneRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.crafting.RecipeHolder;

public class MillstoneJeiCategory extends FAJeiCategory<MillstoneRecipe>
{
	public MillstoneJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.MILLSTONE_TYPE.get(), helper, FABlocks.MILLSTONE, FactoryAutomation.locString("jei", "millstone.name"));
	}

	@Override
	protected IDrawable createBackground()
	{
		return helper.createDrawable(FactoryAutomation.name("textures/gui/jei/chopping_block.png"), 0, 0, 123, 74);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<MillstoneRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15, 19 + 10).addIngredients(recipe.value().getInput());
		var resultItem = getResultItem(recipe.value());
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10);
		out.addItemStack(resultItem);
	}
}
