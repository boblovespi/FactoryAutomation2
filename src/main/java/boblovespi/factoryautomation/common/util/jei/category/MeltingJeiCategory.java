package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.BERUtils;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.BrickCrucible;
import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.jei.FAJeiPlugin;
import boblovespi.factoryautomation.common.util.jei.MeltingJeiRecipe;
import boblovespi.factoryautomation.common.util.jei.MetalIngredientRenderer;
import boblovespi.factoryautomation.common.util.jei.MetalStack;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class MeltingJeiCategory implements IRecipeCategory<MeltingJeiRecipe>
{
	private static final RecipeType<MeltingJeiRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "melting", MeltingJeiRecipe.class);
	public static final ResourceLocation TEXTURE = FactoryAutomation.name("textures/gui/jei/melting.png");
	private final IGuiHelper helper;
	private final IDrawable background;
	private final IDrawable icon;
	private final MetalIngredientRenderer metalIngredientRenderer = MetalIngredientRenderer.create(false, true, 8, 32);
	private final BlockState[] foundries;

	public MeltingJeiCategory(IGuiHelper helper)
	{
		this.helper = helper;
		background = helper.createDrawable(TEXTURE, 0, 0, 132, 98);
		icon = helper.createDrawableItemStack(FAItems.STONE_CRUCIBLE.toStack());
		foundries = new BlockState[]
							{
									FABlocks.STONE_CRUCIBLE.get().defaultBlockState().setValue(StoneCrucible.MULTIBLOCK_COMPLETE, true)
											.setValue(StoneCrucible.FACING, Direction.SOUTH),
									FABlocks.BRICK_CRUCIBLE.get().defaultBlockState().setValue(BrickCrucible.MULTIBLOCK_COMPLETE, true)
											.setValue(BrickCrucible.FACING, Direction.SOUTH)
							};
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
	public void getTooltip(ITooltipBuilder tooltip, MeltingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY)
	{
		if (mouseX >= 10 && mouseX <= 10 + 6 && mouseY >= 29 && mouseY <= 29 + 50)
			tooltip.add(Component.translatable("misc.temperature", String.format("%1$.1f", recipe.temp())));
	}

	@Override
	public IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, MeltingJeiRecipe recipe, IFocusGroup focuses)
	{
		var in = builder.addSlot(RecipeIngredientRole.INPUT, 11, 8).setSlotName("input");
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 111, 43);
		out.setCustomRenderer(FAJeiPlugin.METAL_INGREDIENT, metalIngredientRenderer);
		in.addIngredients(recipe.stack());
		out.addIngredient(FAJeiPlugin.METAL_INGREDIENT, new MetalStack(recipe.metal(), recipe.form().amount()));
	}

	@Override
	public void draw(MeltingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		var state = foundries[recipe.tier() - 1];
		var pose = graphics.pose();
		graphics.drawString(Minecraft.getInstance().font, I18n.get("misc.temperature", recipe.temp()), 11, 84, 0xff545454, false);
		var percentage = recipe.temp() / 1800f;
		var negPercentage = 1 - percentage;
		graphics.blit(TEXTURE, 13, 30 + (int) (49 * negPercentage), 132, (int) (49 * negPercentage), 4, (int) (49 * percentage));
		pose.pushPose();
		{
			pose.translate(39.75f, 54, 100);
			var scale = -16 * 0.625f * 2.3f * 0.5f * 1.5f;
			pose.scale(scale, scale, scale);
			pose.mulPose(BERUtils.quatFromAngleAxis(-30, 1, 0, 0));
			pose.mulPose(BERUtils.quatFromAngleAxis(225, 0, 1, 0));
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pose, graphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		}
		pose.popPose();
	}
}
