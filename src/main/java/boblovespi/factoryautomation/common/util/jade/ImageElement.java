package boblovespi.factoryautomation.common.util.jade;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;
import snownee.jade.api.ui.IDisplayHelper;

public class ImageElement extends Element
{
	private final ResourceLocation image;
	private final int width;
	private final int height;

	public ImageElement(ResourceLocation image, int width, int height)
	{
		this.image = image;
		this.width = width;
		this.height = height;
	}

	@Override
	public Vec2 getSize()
	{
		return new Vec2(width, height);
	}

	@Override
	public void render(GuiGraphics guiGraphics, float x, float y, float v2, float v3)
	{
		RenderSystem.enableBlend();
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, IDisplayHelper.get().opacity());
		guiGraphics.blit(image, (int) x, (int) y, 0, 0, width, height, width, height);
		guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
