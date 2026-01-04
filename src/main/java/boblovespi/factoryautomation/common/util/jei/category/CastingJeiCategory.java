package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.BERUtils;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.processing.StoneCastingVessel;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Metal;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

import java.util.Locale;

public class CastingJeiCategory implements IRecipeCategory<CastingJeiRecipe>
{
	private static final RecipeType<CastingJeiRecipe> TYPE = RecipeType.create(FactoryAutomation.MODID, "casting", CastingJeiRecipe.class);
	private static final ResourceLocation TEXTURE = FactoryAutomation.name("textures/gui/jei/casting.png");
	private final IGuiHelper helper;
	private final IDrawable background;
	private final IDrawable shortArrow;
	private final IDrawable icon;
	private final MetalIngredientRenderer metalIngredientRenderer = MetalIngredientRenderer.create(false, false, 32, 8);

	public CastingJeiCategory(IGuiHelper helper)
	{
		this.helper = helper;
		background = helper.createDrawable(TEXTURE, 0, 0, 150, 78);
		shortArrow = helper.createDrawable(TEXTURE, 151, 0, 25, 22);
		icon = helper.createDrawableItemStack(FAItems.STONE_CASTING_VESSEL.toStack());
	}

	@Override
	public RecipeType<CastingJeiRecipe> getRecipeType()
	{
		return TYPE;
	}

	@Override
	public Component getTitle()
	{
		return Component.translatable(FactoryAutomation.locString("jei", "casting.name"));
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
	public void setRecipe(IRecipeLayoutBuilder builder, CastingJeiRecipe recipe, IFocusGroup focuses)
	{
		var in = builder.addSlot(RecipeIngredientRole.INPUT, 11, 12).setSlotName("input");
		var out = builder.addSlot(RecipeIngredientRole.OUTPUT, 121, 48);
		var cast = builder.addSlot(RecipeIngredientRole.CATALYST, 11, 48);
		cast.addItemStack(recipe.cast());
		in.setCustomRenderer(FAJeiPlugin.METAL_INGREDIENT, metalIngredientRenderer);
		var metals = Metal.allMetals();
		for (var metal : metals)
		{
			var result = Metal.itemForMetalAndForm(metal, recipe.form());
			if (result != Items.AIR)
			{
				var stack = new MetalStack(metal, (int) (recipe.form().amount() * recipe.efficiency()));
				in.addIngredient(FAJeiPlugin.METAL_INGREDIENT, stack);
				out.addItemStack(result.getDefaultInstance());
			}
		}
		builder.createFocusLink(in, out);
	}

	@Override
	public void draw(CastingJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		var state = FABlocks.STONE_CASTING_VESSEL.get().defaultBlockState();
		if (recipe.type() == CasterType.STONE)
			state = FABlocks.STONE_CASTING_VESSEL.get().defaultBlockState().setValue(StoneCastingVessel.MOLD,
					StoneCastingVessel.CastingVesselStates.valueOf(recipe.form().getName().toUpperCase(Locale.ROOT)));
		if (recipe.type() == CasterType.BRICK)
		{
			// graphics.blitSprite(TEXTURE, 256, 256, 151, 0, 46, 13, 25, 18);
			shortArrow.draw(graphics, 46, 13);
			state = FABlocks.BRICK_CASTING_VESSEL.get().defaultBlockState();
		}
		var pose = graphics.pose();
		var input = recipeSlotsView.findSlotByName("input").orElseThrow().getDisplayedIngredient(FAJeiPlugin.METAL_INGREDIENT).map(MetalStack::quantity).orElse(0);
		graphics.drawString(Minecraft.getInstance().font, I18n.get("misc.metal_quantity_nameless", input), 10, 23, 0xff545454, false);
		pose.pushPose();
		{
			pose.translate(49.75f, 41 + 14, 100);
			var scale = -16 * 0.625f * 2.3f;
			pose.scale(scale, scale, scale);
			pose.mulPose(BERUtils.quatFromAngleAxis(-30, 1, 0, 0));
			pose.mulPose(BERUtils.quatFromAngleAxis(225, 0, 1, 0));
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(state, pose, graphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
		}
		pose.popPose();
	}
}
