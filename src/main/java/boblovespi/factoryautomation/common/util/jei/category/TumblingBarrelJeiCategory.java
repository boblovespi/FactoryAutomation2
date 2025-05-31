package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.OptionalSizedFluidIngredient;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.common.recipe.TumblingBarrelRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class TumblingBarrelJeiCategory extends FAJeiCategory<TumblingBarrelRecipe>
{
	public TumblingBarrelJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.TUMBLING_BARREL_TYPE.get(), helper, FABlocks.TUMBLING_BARREL, FactoryAutomation.locString("jei", "tumbling_barrel.name"));
	}

	@Override
	protected IDrawable createBackground()
	{
		return new Background(helper.createDrawable(FactoryAutomation.name("textures/gui/jei/tumbling_barrel.png"), 0, 0, 147, 93));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<TumblingBarrelRecipe> recipe, IFocusGroup iFocusGroup)
	{
		if (!recipe.value().input().isEmpty())
			builder.addSlot(RecipeIngredientRole.INPUT, 19, 12).addIngredients(recipe.value().input());
		if (recipe.value().fluidInput() instanceof OptionalSizedFluidIngredient.Present p)
			builder.addSlot(RecipeIngredientRole.INPUT, 11, 34).addIngredients(NeoForgeTypes.FLUID_STACK, List.of(p.value().getFluids())).setFluidRenderer(500, true, 16, 16);
		if (!recipe.value().result().isEmpty())
			builder.addSlot(RecipeIngredientRole.OUTPUT, 113, 48).addItemStack(recipe.value().result());
		if (!recipe.value().fluidResult().isEmpty())
			builder.addSlot(RecipeIngredientRole.OUTPUT, 122, 70).addIngredient(NeoForgeTypes.FLUID_STACK, recipe.value().fluidResult()).setFluidRenderer(500, true, 16, 16);
	}

	private record Background(IDrawable bg) implements IDrawable
	{
		@Override
		public int getWidth()
		{
			return 147;
		}

		@Override
		public int getHeight()
		{
			return 93;
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
				guiGraphics.pose().translate(7 + (35 + 7) / scale, 13.5 + 16 / scale, -100);
				guiGraphics.renderFakeItem(FABlocks.TUMBLING_BARREL.toStack(), 0, 0);
			}
			guiGraphics.pose().popPose();
		}
	}
}
