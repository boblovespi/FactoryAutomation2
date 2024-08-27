package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.BrickDryingRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;

public class BrickDryingJeiCategory extends FAJeiCategory<BrickDryingRecipe>
{
	public BrickDryingJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.BRICK_DRYING_TYPE.get(), helper, FABlocks.BRICK_MAKER_FRAME, FactoryAutomation.locString("jei", "brick_drying.name"));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BrickDryingRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15, 19 + 10).addIngredients(recipe.value().getInput());
		var resultItem = getResultItem(recipe.value());
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10);
		out.addItemStack(resultItem);
	}

	@Override
	protected IDrawable createBackground()
	{
		return new Background(helper.createDrawable(FactoryAutomation.name("textures/gui/jei/chopping_block.png"), 0, 0, 123, 74));
	}

	private record Background(IDrawable bg) implements IDrawable
	{
		@Override
		public int getWidth()
		{
			return 123;
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
			guiGraphics.pose().pushPose();
			{
				var scale = 2.3f;
				guiGraphics.pose().translate(-8 * scale, -8 * scale, 0);
				guiGraphics.pose().scale(scale, scale, scale);
				guiGraphics.pose().translate(7 + 15 / scale, 13.5 + 10 / scale, -100);
				guiGraphics.renderFakeItem(FABlocks.BRICK_MAKER_FRAME.toStack(), 0, 0);
			}
			guiGraphics.pose().popPose();
		}
	}
}
