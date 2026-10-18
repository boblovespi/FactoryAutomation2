package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.util.ItemContainerTooltipData;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;

public record ItemContainerTooltip(ItemContainerTooltipData data) implements ClientTooltipComponent
{
	private int getXSlots()
	{
		return Math.min(5, data.contents().size());
	}

	private int getYSlots()
	{
		return (data.contents().size() + 4) / 5;
	}

	@Override
	public int getHeight()
	{
		return getYSlots() * 18 + 5;
	}

	@Override
	public int getWidth(Font font)
	{
		return getXSlots() * 18 + 2;
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics graphics)
	{
		var width = getWidth(font);
		// graphics.fill(x, y, x + width, y + getHeight(), 0xffaa00aa);
		graphics.fill(x, y + 1, x + width, y + getHeight() - 2, 0x9033211A);
		var topBorder = 0xff89461C;
		var bottomBorder = 0xff542D1B;
		graphics.hLine(x, x + width - 1, y + 1, topBorder);
		graphics.hLine(x, x + width - 1, y + getHeight() - 3, bottomBorder);
		graphics.fillGradient(x, y + 1, x + 1, y + getHeight() -2, topBorder, bottomBorder);
		graphics.fillGradient(x + width - 1, y + 1, x + width, y + getHeight() -2, topBorder, bottomBorder);
		outer:
		for (int j = 0; j < getYSlots(); j++)
		{
			for (int i = 0; i < getXSlots(); i++)
			{
				var idx = i + j * getXSlots();
				if (idx >= data.contents().size())
					break outer;
				var item = data.contents().get(idx);
				graphics.renderItem(item, x + i * 18 + 2, y + j * 18 + 3);
				graphics.renderItemDecorations(font, item, x + i * 18 + 2, y + j * 18 + 3);
			}
		}
	}
}
