package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class SimpleContainerHandler<T extends AbstractContainerScreen<?>> implements IGuiContainerHandler<T>
{
	private final IJeiHelpers helpers;
	private final List<ClickableIngredient<T, ?>> clickableIngredients;
	private final List<IGuiClickableArea> clickableAreas;

	public SimpleContainerHandler(IJeiHelpers helpers, List<ClickableIngredient<T, ?>> clickableIngredients, List<IGuiClickableArea> clickableAreas)
	{
		this.helpers = helpers;
		this.clickableIngredients = clickableIngredients;
		this.clickableAreas = clickableAreas;
	}

	@Override
	public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(T containerScreen, double mouseX, double mouseY)
	{
		return clickableIngredients.stream()
								   .filter(c -> isHovering(containerScreen.getGuiLeft(), containerScreen.getGuiTop(), mouseX, mouseY, c.x, c.y, c.width, c.height))
								   .findFirst()
								   .flatMap(t -> t.makeIngredient(helpers.getIngredientManager(), containerScreen));
	}

	@Override
	public Collection<IGuiClickableArea> getGuiClickableAreas(T containerScreen, double guiMouseX, double guiMouseY)
	{
		return clickableAreas;
	}

	private boolean isHovering(int leftPos, int topPos, double mouseX, double mouseY, int x, int y, int width, int height)
	{
		mouseX -= leftPos;
		mouseY -= topPos;
		return mouseX >= (double) (x - 1)
			   && mouseX < (double) (x + width + 1)
			   && mouseY >= (double) (y - 1)
			   && mouseY < (double) (y + height + 1);
	}

	public record ClickableIngredient<T extends AbstractContainerScreen<?>, U>(int x, int y, int width, int height, Function<T, U> factory)
	{
		private Optional<IClickableIngredient<U>> makeIngredient(IIngredientManager ingredientHelper, T t)
		{
			var t2 = factory.apply(t);
			FactoryAutomation.LOGGER.debug("making ingredient; ing: {}, type: {}", t2, t2.getClass().getName());
			return ingredientHelper.createTypedIngredient(t2).map(ing -> new ClickableIngredientImpl<>(ing, new Rect2i(x, y, width, height)));
		}
	}

	private record ClickableIngredientImpl<T>(ITypedIngredient<T> ing, Rect2i area) implements IClickableIngredient<T>
	{
		@Override
		public ITypedIngredient<T> getTypedIngredient()
		{
			return ing;
		}

		@Override
		public Rect2i getArea()
		{
			return area;
		}
	}
}
