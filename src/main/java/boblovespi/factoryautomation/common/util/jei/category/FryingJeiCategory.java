package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.FryingPanRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.stream.Stream;

public class FryingJeiCategory extends FAJeiCategory<FryingPanRecipe>
{
	public FryingJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.FRYING_PAN_TYPE.get(), helper, FABlocks.FRYING_PAN, FactoryAutomation.locString("jei", "frying.name"));
	}

	@Override
	protected IDrawable createBackground()
	{
		return new Background(helper.createDrawable(FactoryAutomation.name("textures/gui/jei/frying.png"), 0, 0, 123, 73));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<FryingPanRecipe> recipe, IFocusGroup focuses)
	{
		var recipeV = recipe.value();
		var ingredients = recipeV.getInputs();
		for (int i = 0; i < ingredients.size(); i++)
		{
			var ingredient = ingredients.get(i);
			builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15 - 9 + (i % 2) * 18, 19 + 6 - (i / 2) * 18).addIngredients(ingredient);
		}
		var resultItem = getResultItem(recipeV);
		var data = recipeV.getData();
		if (!data.plate().isEmpty())
			builder.addSlot(RecipeIngredientRole.INPUT, 79, 51 - 4).addIngredients(data.plate());
		if (!data.liquid().isEmpty())
			builder.addSlot(RecipeIngredientRole.INPUT, 14, 7)
				   .setFluidRenderer(250, false, 34, 34)
				   .addIngredients(NeoForgeTypes.FLUID_STACK, Stream.of(data.liquid().getStacks()).map(s -> s.copyWithAmount(250)).toList());
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10 - 4);
		out.addItemStack(resultItem);
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
			return 73;
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
				guiGraphics.pose().translate(7 + 15 / scale, 17.5 + 14 / scale, -100);
				guiGraphics.renderFakeItem(FABlocks.FRYING_PAN.toStack(), 0, 0);
			}
			guiGraphics.pose().popPose();
		}
	}
}
