package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.client.gui.StoneFoundryScreen;
import boblovespi.factoryautomation.common.util.Metal;
import boblovespi.factoryautomation.common.util.jei.category.MeltingJeiCategory;
import mezz.jei.api.gui.handlers.IGuiClickableArea;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.client.renderer.Rect2i;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class StoneFoundryContainerHandler implements IGuiContainerHandler<StoneFoundryScreen>
{
	private final IJeiHelpers helpers;

	public StoneFoundryContainerHandler(IJeiHelpers helpers)
	{
		this.helpers = helpers;
	}

	@Override
	public Optional<IClickableIngredient<?>> getClickableIngredientUnderMouse(StoneFoundryScreen containerScreen, double mouseX, double mouseY)
	{
		if (isHovering(containerScreen.getGuiLeft(), containerScreen.getGuiTop(), mouseX, mouseY, 107, 17, 16, 59))
			return Optional.of(new MetalStackClickableIngredient(containerScreen.getMetal()));
		return Optional.empty();
	}

	@Override
	public Collection<IGuiClickableArea> getGuiClickableAreas(StoneFoundryScreen containerScreen, double guiMouseX, double guiMouseY)
	{
		return List.of(IGuiClickableArea.createBasic(84, 18, 22, 16, MeltingJeiCategory.TYPE));
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

	private class MetalStackClickableIngredient implements IClickableIngredient<MetalStack>
	{
		private final Metal metal;

		public MetalStackClickableIngredient(Metal metal)
		{
			this.metal = metal;
		}

		@Override
		public Rect2i getArea()
		{
			return new Rect2i(107, 17, 16, 59);
		}

		@Override
		public ITypedIngredient<MetalStack> getTypedIngredient()
		{
			return helpers.getIngredientManager().createTypedIngredient(new MetalStack(metal, 1)).get();
		}
	}
}
