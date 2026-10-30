package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.StampMillRecipe;
import boblovespi.factoryautomation.common.util.jei.category.FAJeiCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;

public class StampMillJeiCategory extends FAJeiCategory<StampMillRecipe>
{
	public StampMillJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.STAMP_MILL_TYPE.get(), helper, FABlocks.STAMP_MILL, FactoryAutomation.locString("jei", "stamp_mill.name"));
	}

	@Override
	protected IDrawable createBackground()
	{
		return new Background(helper.createDrawable(FactoryAutomation.name("textures/gui/jei/milling.png"), 0, 0, 123, 74));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<StampMillRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15, 19 + 2).addIngredients(recipe.value().getInput());
		var resultItem = getResultItem(recipe.value());
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10);
		out.addItemStack(resultItem);
	}

	@Override
	public void draw(RecipeHolder<StampMillRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{

	}

	private record Background(IDrawable bg) implements IDrawable
	{
		@Override
		public int getWidth()
		{
			return 143;
		}

		@Override
		public int getHeight()
		{
			return 74;
		}

		@Override
		public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset)
		{
			bg.draw(guiGraphics, xOffset, yOffset);
			/*guiGraphics.pose().pushPose();
			{
				var scale = 2.3f;
				guiGraphics.pose().translate(-8 * scale, -8 * scale, 0);
				guiGraphics.pose().scale(scale, scale, scale);
				guiGraphics.pose().translate(7 + 15 / scale, 13.5 + 10 / scale, -100);
				guiGraphics.renderFakeItem(FABlocks.STEAM_OVEN.toStack(), 0, 0);
			}
			guiGraphics.pose().popPose();*/
		}
	}
}
