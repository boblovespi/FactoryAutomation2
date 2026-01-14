package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.util.MathHelper;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.util.Mth;

public class GuiUtils
{
	/**
	 * Draws a sector (a pie slice of a circle).
	 *
	 * @param vc The vertex consumer to draw to.
	 * @param mat The last pose of the pose stack.
	 * @param x The x position of the center of the sector.
	 * @param y The y position of the center of the sector.
	 * @param radius The radius of the sector.
	 * @param startAngle The start angle of the sector.
	 * @param endAngle The end angle of the sector.
	 * @param color The color to draw with.
	 * @param vertexCount The number of external vertices of the sector. More vertices makes a smoother outer edge. Must be at least 2.
	 */
	public static void drawSector(VertexConsumer vc, PoseStack.Pose mat, float x, float y, float radius, float startAngle, float endAngle, int color, int vertexCount)
	{
		var sliceAngle = MathHelper.normalizeAngle(endAngle - startAngle) / ((float) vertexCount - 1);
		vc.addVertex(mat, x, y, 0).setColor(color);
		var vX = 0f;
		var vY = 0f;
		for (int i = 0; i < vertexCount; i++)
		{
			var angle = startAngle + sliceAngle * (vertexCount - i - 1);
			vX = x + Mth.cos(angle) * radius;
			vY = y + Mth.sin(angle) * radius;
			vc.addVertex(mat, vX, vY, 0).setColor(color);
			if (i % 2 == 0 && i > 0 && i != vertexCount - 1)
			{
				vc.addVertex(mat, x, y, 0).setColor(color);
				vc.addVertex(mat, vX, vY, 0).setColor(color);
			}
		}
		if (vertexCount % 2 == 0)
			vc.addVertex(mat, vX, vY, 0).setColor(color);
	}
}
