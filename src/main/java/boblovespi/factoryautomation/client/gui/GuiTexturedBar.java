package boblovespi.factoryautomation.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class GuiTexturedBar
{
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	private final float u0;
	private final float v0;
	private final float u1;
	private final float v1;
	private final int spriteW;
	private final int spriteH;
	private final ResourceLocation atlas;

	public GuiTexturedBar(int x, int y, int w, int h, TextureAtlasSprite sprite, int spriteW, int spriteH)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		atlas = sprite.atlasLocation();
		u0 = sprite.getU0();
		v0 = sprite.getV0();
		u1 = sprite.getU1();
		v1 = sprite.getV1();
		this.spriteW = spriteW;
		this.spriteH = spriteH;
	}

	public void draw(AbstractContainerScreen<?> gui, GuiGraphics graphics, float percent, int color)
	{
		var r = FastColor.ARGB32.red(color) / 255.0F;
		var g = FastColor.ARGB32.green(color) / 255.0F;
		var b = FastColor.ARGB32.blue(color) / 255.0F;
		var guiLeft = gui.getGuiLeft();
		var guiTop = gui.getGuiTop();
		var pose = graphics.pose().last().pose();
		var x1 = guiLeft + x;
		var y1 = guiTop + y;
		var y2 = y1 + h;
		var widthLeft = w;
		var height = (int) (h * percent);
		var heightLeft = height;
		while (widthLeft > 0)
		{
			while (heightLeft > 0)
			{
				innerBlit(pose, x1, y2 - Math.min(spriteH, heightLeft), x1 + Math.min(spriteW, widthLeft), y2, 0, r, g, b, 1);
				heightLeft -= spriteH;
				y2 -= spriteH;
			}
			widthLeft -= spriteW;
			x1 += spriteW;
			heightLeft = height;
			y2 = y1 + h;
		}
	}

	private void innerBlit(Matrix4f matrix4f, float x1, float y1, float x2, float y2, float blitOffset, float r, float g, float b, float a)
	{
		RenderSystem.setShaderTexture(0, atlas);
		RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
		RenderSystem.enableBlend();
		float width = (x2 - x1) / spriteW;
		float height = (y2 - y1) / spriteH;
		BufferBuilder bufferbuilder = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
		bufferbuilder.addVertex(matrix4f, x1, y1, blitOffset)
					 .setUv(u0, Mth.lerp(height, v1, v0))
					 .setColor(r, g, b, a);
		bufferbuilder.addVertex(matrix4f, x1, y2, blitOffset)
					 .setUv(u0, v1)
					 .setColor(r, g, b, a);
		bufferbuilder.addVertex(matrix4f, x2, y2, blitOffset)
					 .setUv(Mth.lerp(width, u0, u1), v1)
					 .setColor(r, g, b, a);
		bufferbuilder.addVertex(matrix4f, x2, y1, blitOffset)
					 .setUv(Mth.lerp(width, u0, u1), Mth.lerp(height, v1, v0))
					 .setColor(r, g, b, a);
		BufferUploader.drawWithShader(bufferbuilder.buildOrThrow());
		RenderSystem.disableBlend();
	}
}
