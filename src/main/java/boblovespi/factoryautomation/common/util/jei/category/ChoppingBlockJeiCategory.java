package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.ChoppingBlockRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import boblovespi.factoryautomation.client.PartialTickHelper;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.common.crafting.DifferenceIngredient;
import org.joml.AxisAngle4f;
import org.joml.Quaternionf;

public class ChoppingBlockJeiCategory extends FAJeiCategory<ChoppingBlockRecipe>
{
	public ChoppingBlockJeiCategory(IGuiHelper helper)
	{
		super(RecipeThings.CHOPPING_BLOCK_TYPE.get(), helper, FABlocks.CHOPPING_BLOCK, FactoryAutomation.locString("jei", "chopping_block.name"));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ChoppingBlockRecipe> recipe, IFocusGroup focuses)
	{
		builder.addSlot(RecipeIngredientRole.INPUT, 8 + 15, 19 + 10).addIngredients(recipe.value().getInput());
		var resultItem = getResultItem(recipe.value());
		var doubleItem = resultItem.copyWithCount(resultItem.getCount() * 2);
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 62 + 25, 19 + 10);
		var axe = builder.addSlot(RecipeIngredientRole.CATALYST, 62 + 26 + 8, 19 + 10 + 16 + 6).setSlotName("axe");
		var badAxe = DifferenceIngredient.of(Ingredient.of(ItemTags.AXES), Ingredient.of(FATags.Items.GOOD_AXES)).getItems();
		var goodAxe = Ingredient.of(FATags.Items.GOOD_AXES).getItems();
		for (var b : badAxe)
		{
			b.setDamageValue(4);
			out.addItemStack(resultItem);
			axe.addItemStack(b);
		}
		for (var g : goodAxe)
		{
			g.setDamageValue(4);
			out.addItemStack(doubleItem);
			axe.addItemStack(g);
		}
		builder.createFocusLink(out, axe);
	}

	@Override
	public void draw(RecipeHolder<ChoppingBlockRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY)
	{
		float delta = PartialTickHelper.getPartialFor(30) / 15;
				// guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true)), 0, 16, 0xffffffff);
		var stack = recipeSlotsView.findSlotByName("axe").flatMap(IRecipeSlotView::getDisplayedItemStack).get();
		guiGraphics.pose().pushPose();
		{
			var s = 0.05f;
			var q = 1.27913f;
			var a = 1 / Mth.sqrt(0.641742f);
			if (delta < 1)
				delta = 1 - delta;
			else
				delta = q - a * Mth.sqrt(2 + s - delta);
			if (Float.isNaN(delta))
				delta = 1;
			guiGraphics.pose().translate(33 + 15, 17 + 10, 0);
			guiGraphics.pose().rotateAround(new Quaternionf(new AxisAngle4f((float) (-delta * Math.PI / 1.4), 0, 0, 1)), 0, 0, 0);
			guiGraphics.renderFakeItem(stack, 0, -16);
			guiGraphics.pose().translate(0, 0, 200);
			//			guiGraphics.fill(-1, -1, 1, 1, 0xffffff00);
		}
		guiGraphics.pose().popPose();
		//		guiGraphics.drawString(Minecraft.getInstance().font, String.valueOf(delta), 0, 0, 0xffffffff);
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
				guiGraphics.renderFakeItem(FABlocks.CHOPPING_BLOCK.toStack(), 0, 0);
			}
			guiGraphics.pose().popPose();
		}
	}
}
