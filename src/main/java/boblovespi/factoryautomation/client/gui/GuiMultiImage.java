package boblovespi.factoryautomation.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class GuiMultiImage
{
	private final int x;
	private final int y;
	private final float w;
	private final float h;
	private final int texX;
	private final int texY;
	private final int texW;
	private final int texH;
	private final List<ResourceLocation> texLocs;
	private int texture;

	public GuiMultiImage(int x, int y, int w, int h, int texX, int texY, int texW, int texH, List<ResourceLocation> texLocs)
	{
		this.x = x;
		this.y = y;
		this.w = (float) w / texW;
		this.h = (float) h / texH;
		this.texX = texX;
		this.texY = texY;
		this.texW = texW;
		this.texH = texH;
		this.texLocs = texLocs;
	}

	public void setTexture(int number)
	{
		if (number < 0)
			number = 0;
		if (number >= texLocs.size())
			number = texLocs.size() - 1;
		texture = number;
	}

	public void draw(AbstractContainerScreen<?> gui, GuiGraphics graphics)
	{
		int guiLeft = gui.getGuiLeft();
		int guiTop = gui.getGuiTop();
		graphics.pose().pushPose();
		{
			graphics.pose().translate(guiLeft + x, guiTop + y, 0);
			graphics.pose().scale(w, h, 1);
			graphics.blit(texLocs.get(texture), 0, 0, texX, texY, texW, texH, texW, texH);
		}
		graphics.pose().popPose();
	}
}
