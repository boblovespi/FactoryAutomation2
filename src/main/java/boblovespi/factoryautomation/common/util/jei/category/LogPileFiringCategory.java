package boblovespi.factoryautomation.common.util.jei.category;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.BERUtils;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.recipe.LogPileFiringRecipe;
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
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LogPileFiringCategory implements IRecipeCategory<LogPileFiringCategory.Holder>
{
	protected final IGuiHelper helper;
	private final String localizationName;
	private final RecipeType<Holder> recipeType;
	private final IDrawable icon;
	private final IDrawable background;

	public LogPileFiringCategory(IGuiHelper helper)
	{
		recipeType = RecipeType.create(FactoryAutomation.MODID, "log_pile_firing", Holder.class);
		this.helper = helper;
		localizationName = FactoryAutomation.locString("jei", "log_pile_firing.name");
		icon = helper.createDrawableItemLike(FABlocks.LOG_PILE);
		background = helper.createBlankDrawable(100, 80);
	}

	@Override
	public RecipeType<Holder> getRecipeType()
	{
		return recipeType;
	}

	@Override
	public Component getTitle()
	{
		return Component.translatable(localizationName);
	}

	@Override
	public @Nullable IDrawable getIcon()
	{
		return icon;
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, Holder recipe, IFocusGroup focuses)
	{
		switch (recipe)
		{
			case Holder.Dummy dummy ->
			{
				builder.addSlot(RecipeIngredientRole.CATALYST, 0, 0).addItemStack(dummy.surrounder);
				builder.addSlot(RecipeIngredientRole.INPUT, 30, 30).addItemLike(dummy.input);
				builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 60).addItemStacks(dummy.drops);
			}
			case Holder.Real real ->
			{
				builder.addSlot(RecipeIngredientRole.CATALYST, 0, 0).addItemLike(real.recipe.value().getData().logPileLike());
				builder.addSlot(RecipeIngredientRole.INPUT, 30, 30).addIngredients(real.recipe.value().getInput());
				builder.addSlot(RecipeIngredientRole.OUTPUT, 60, 60).addItemStack(getResultItem(real.recipe.value()));
			}
		}
	}

	@Override
	public void draw(Holder recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY)
	{
		if (recipe instanceof Holder.Dummy dummy)
		{
			var pose = graphics.pose();
			pose.pushPose();
			{
				pose.translate(49.75f, 41 + 10, 100);
				var scale = -16 * 0.625f * 2.3f;
				pose.scale(scale, scale, scale);
				pose.mulPose(BERUtils.quatFromAngleAxis(-30, 1, 0, 0));
				pose.mulPose(BERUtils.quatFromAngleAxis(225, 0, 1, 0));
				Minecraft.getInstance().getBlockRenderer().renderSingleBlock(dummy.state, pose, graphics.bufferSource(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
			}
			pose.popPose();
		}
	}

	@Override
	public IDrawable getBackground()
	{
		return background;
	}

	protected static ItemStack getResultItem(LogPileFiringRecipe recipe)
	{
		return switch (Minecraft.getInstance().level)
		{
			case null -> throw new NullPointerException("I just did what JEI does for vanilla, why didn't it work?");
			case ClientLevel level -> recipe.getResultItem(level.registryAccess());
		};
	}

	public sealed interface Holder
	{
		record Dummy(ItemStack surrounder, ItemLike input, List<ItemStack> drops, BlockState state) implements Holder
		{

		}
		record Real(RecipeHolder<LogPileFiringRecipe> recipe) implements Holder
		{

		}
	}
}
