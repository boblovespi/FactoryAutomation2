package boblovespi.factoryautomation.common.util.jei;

import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MetalIngredientRenderer implements IIngredientRenderer<MetalStack>
{
	private final boolean fillBar;
	private final boolean vertical;
	private final int width;
	private final int height;

	private MetalIngredientRenderer(boolean fillBar, boolean vertical, int width, int height)
	{
		this.vertical = vertical;
		this.width = width;
		this.fillBar = fillBar;
		this.height = height;
	}

	public static MetalIngredientRenderer create()
	{
		return create(true, false, 16, 16);
	}

	public static MetalIngredientRenderer create(boolean fillBar, boolean vertical, int width, int height)
	{
		return new MetalIngredientRenderer(fillBar, vertical, width, height);
	}

	@Override
	public void render(GuiGraphics graphics, MetalStack ingredient)
	{
		graphics.fill(0, fillBar || !vertical ? 0 : height - ingredient.quantity(), fillBar || vertical ? width : ingredient.quantity(), height, ingredient.metal().color());
	}

	@SuppressWarnings("removal")
	@Override
	public List<Component> getTooltip(MetalStack ingredient, TooltipFlag tooltipFlag)
	{
		return List.of(Component.translatable("metal." + ingredient.metal().getName() + ".name"),
				Component.translatable("misc.metal_quantity_nameless", ingredient.quantity()).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public int getHeight()
	{
		return height;
	}

	@Override
	public int getWidth()
	{
		return width;
	}
}
