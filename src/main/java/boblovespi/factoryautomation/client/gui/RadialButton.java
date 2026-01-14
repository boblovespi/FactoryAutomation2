package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.common.util.MathHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import org.joml.Vector2d;

public class RadialButton extends ExtendedButton
{
	private final float startAngle;
	private final float endAngle;
	private final int centerX;
	private final int centerY;
	private final int radius;
	private final int radiusSquared;
	private final ItemStack item;
	private final int itemX;
	private final int itemY;

	public RadialButton(ItemLike displayItem, int xPos, int yPos, int displayRadius, int outerRadius, int buttonCount, int index, Component displayString, OnPress handler)
	{
		super(xPos - outerRadius, yPos - outerRadius, outerRadius * 2, outerRadius * 2, displayString, handler);
		radius = outerRadius;
		var angle = 2 * Mth.PI / buttonCount;
		startAngle = angle * index - angle / 2;
		endAngle = angle * (index + 1) - angle / 2;
		centerX = xPos;
		centerY = yPos;
		radiusSquared = radius * radius;
		item = displayItem.asItem().getDefaultInstance();
		itemX = (int) (centerX + Math.cos(angle * index) * displayRadius) - 8;
		itemY = (int) (centerY + Math.sin(angle * index) * displayRadius) - 8;
	}

	@Override
	public boolean isMouseOver(double mouseX, double mouseY)
	{
		var dist = Vector2d.distanceSquared(centerX, centerY, mouseX, mouseY);
		if (dist > radiusSquared)
			return false;
		var angle = Math.atan2(mouseY - centerY, mouseX - centerX);
		return MathHelper.isAngleBetween((float) angle, startAngle, endAngle);
	}

	@Override
	protected boolean clicked(double mouseX, double mouseY)
	{
		return isMouseOver(mouseX, mouseY);
	}

	@Override
	public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		var mc = Minecraft.getInstance();
		graphics.renderFakeItem(item, itemX, itemY);
		isHovered = isMouseOver(mouseX, mouseY);

		if (isHoveredOrFocused())
		{
			var buttonText = getMessage();
			graphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), itemX + 8, itemY - 24 + 8, getFGColor());
		}

		if (isHoveredOrFocused())
		{
			GuiUtils.drawSector(graphics.bufferSource().getBuffer(RenderType.GUI), graphics.pose().last(), centerX, centerY, radius, startAngle, endAngle, 0xff888888, 20);
		}
	}
}
