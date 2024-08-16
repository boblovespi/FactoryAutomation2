package boblovespi.factoryautomation.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

public class GuiBar
{
	private final ResourceLocation sprite;
	private final int x;
	private final int y;
	private final int barX;
	private final int barY;
	private final int w;
	private final int h;
	private final ProgressDirection direction;

	public GuiBar(ResourceLocation sprite, int x, int y, int barX, int barY, int l, int w, ProgressDirection direction)
	{
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		this.barX = barX;
		this.barY = barY;
		this.w = l;
		this.h = w;
		this.direction = direction;
	}

	public void draw(AbstractContainerScreen<?> gui, GuiGraphics graphics, float percentage)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		float negPercentage = 1 - percentage;
		switch (direction)
		{
			case DOWN -> graphics.blit(sprite, guiLeft + x, guiTop + y, barX, barY, w, (int) (h * percentage));
			case UP -> graphics.blit(sprite, guiLeft + x, guiTop + y + (int) (h * negPercentage), barX, barY + (int) (h * negPercentage), w, (int) (h * percentage));
			case RIGHT -> graphics.blit(sprite, guiLeft + x, guiTop + y, barX, barY, (int) (w * percentage), h);
			case LEFT -> graphics.blit(sprite, guiLeft + x + (int) (w * negPercentage), guiTop + y, barX + (int) (w * negPercentage), barY, (int) (w * percentage), h);
		}
	}

	public enum ProgressDirection
	{
		LEFT, RIGHT, UP, DOWN
	}
}
