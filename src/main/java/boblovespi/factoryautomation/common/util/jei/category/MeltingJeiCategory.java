package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.BERUtils;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.jei.*;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;

public class MeltingJeiCategory implements IRecipeCategory<MeltingJeiRecipe>
{
	private static final RecipeType<MeltingJeiRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "melting", MeltingJeiRecipe.class);
	private final IGuiHelper helper;
	private final IDrawable background;
	private final IDrawable icon;
	private final MetalIngredientRenderer metalIngredientRenderer = MetalIngredientRenderer.create(false, true, 8, 32);

	public MeltingJeiCategory(IGuiHelper helper)
	{
		this.helper = helper;
		background = helper.createDrawable(FactoryAutomation.name("textures/gui/jei/melting.png"), 0, 0, 150, 74);
		icon = helper.createDrawableItemStack(FAItems.STONE_CRUCIBLE.toStack());
	}

	@Override
	public RecipeType<MeltingJeiRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Override
	public Component getTitle()
	{
		return Component.translatable(FactoryAutomation.locString("jei", "melting.name"));
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MeltingJeiRecipe recipe, IFocusGroup focuses)
	{
		var in = builder.addSlot(RecipeIngredientRole.INPUT, 27, 8).setSlotName("input");
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 125, 27);
		out.setCustomRenderer(FAJeiPlugin.METAL_INGREDIENT, metalIngredientRenderer);
		in.addIngredients(recipe.stack());
		out.addIngredient(FAJeiPlugin.METAL_INGREDIENT, new MetalStack(recipe.metal(), recipe.form().amount()));
	}

	@Override
	public void draw(MeltingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		var state = FABlocks.STONE_CRUCIBLE.get().defaultBlockState().setValue(StoneCrucible.MULTIBLOCK_COMPLETE, true).setValue(StoneCrucible.FACING, Direction.SOUTH);
		var pose = graphics.pose();
		graphics.drawString(Minecraft.getInstance().font, I18n.get("misc.temperature", recipe.temp()), 10, 23, 0xff545454, false);
		pose.pushPose();
		{
			pose.translate(49.75f, 41 + 10, 100);
			var scale = -16 * 0.625f * 2.3f * 0.5f;
			pose.scale(scale, scale, scale);
			pose.mulPose(BERUtils.quatFromAngleAxis(-30, 1, 0, 0));
			pose.mulPose(BERUtils.quatFromAngleAxis(225, 0, 1, 0));
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pose, graphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		}
		pose.popPose();
	}
}
