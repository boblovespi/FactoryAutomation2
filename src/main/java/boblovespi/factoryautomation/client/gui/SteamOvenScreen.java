package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.menu.SteamOvenMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SteamOvenScreen extends AbstractContainerScreen<SteamOvenMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/steam_oven.png");
	private final GuiBar progressBar;
	private final GuiFluidTank steamBar;

	public SteamOvenScreen(SteamOvenMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		imageHeight = 180;
		inventoryLabelY += 14;
		progressBar = new GuiBar(BACKGROUND_TEXTURE, 80, 34, 177, 14, 22, 16, GuiBar.ProgressDirection.RIGHT);
		steamBar =  new GuiFluidTank(BACKGROUND_TEXTURE, 43, 16, 6, 61, 182, 31);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		renderBackground(graphics, mouseX, mouseY, partialTick);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
	{
		graphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		progressBar.draw(this, graphics, Float.intBitsToFloat(menu.getData(0)));
		steamBar.draw(this, graphics, FAFluids.STEAM_SOURCE.get(), menu.getData(1) / 1000f);
	}

	@Override
	protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.renderTooltip(graphics, mouseX, mouseY);
		if (isHovering(43, 16, 6, 61, mouseX, mouseY))
		{
			var text = Component.translatable("misc.millibucket", menu.getData(1));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
	}
}
