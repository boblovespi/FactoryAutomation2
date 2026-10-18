package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.menu.LeatherSackMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class LeatherSackScreen extends AbstractContainerScreen<LeatherSackMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/leather_sack.png");

	public LeatherSackScreen(LeatherSackMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		titleLabelX = 62 - 7;
		imageHeight = 177;
		inventoryLabelY += 8 + 3;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		renderBackground(graphics, mouseX, mouseY, partialTick);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}
}
