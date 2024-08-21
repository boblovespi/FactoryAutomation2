package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.menu.StoneFoundryMenu;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class StoneFoundryScreen extends AbstractContainerScreen<StoneFoundryMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/stone_foundry.png");
	private final GuiBar flameBar;
	private final GuiBar temperatureBar;
	private final GuiBar progressBar;

	public StoneFoundryScreen(StoneFoundryMenu pMenu, Inventory pPlayerInventory, Component pTitle)
	{
		super(pMenu, pPlayerInventory, pTitle);
		imageHeight = 180;
		inventoryLabelY += 14;
		flameBar = new GuiBar(BACKGROUND_TEXTURE, 67, 40, 176, 0, 14, 14, GuiBar.ProgressDirection.UP);
		temperatureBar = new GuiBar(BACKGROUND_TEXTURE, 53, 16, 176, 17, 6, 61, GuiBar.ProgressDirection.UP);
		progressBar = new GuiBar(BACKGROUND_TEXTURE, 84, 21, 194, 2, 22, 10, GuiBar.ProgressDirection.RIGHT);
	}

	@Override
	public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
	{
		renderBackground(graphics, mouseX, mouseY, partialTick);
		super.render(graphics, mouseX, mouseY, partialTick);
		renderTooltip(graphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY)
	{
		graphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
		flameBar.draw(this, graphics, Float.intBitsToFloat(menu.getData(0)));
		temperatureBar.draw(this, graphics, Float.intBitsToFloat(menu.getData(1)) / 1800f);
		progressBar.draw(this, graphics, Float.intBitsToFloat(menu.getData(2)));
		var metal = Metal.fromId(menu.getData(4));
		graphics.fill(leftPos + 107, topPos + (int) (76 - 59 * menu.getData(3) / (Form.INGOT.amount() * 9 * 3f)), leftPos + 123, topPos + 76, metal.color());
	}

	@Override
	protected void renderTooltip(GuiGraphics pGuiGraphics, int mouseX, int mouseY)
	{
		super.renderTooltip(pGuiGraphics, mouseX, mouseY);
		if (isHovering(53, 16, 6, 61, mouseX, mouseY))
		{
			var text = Component.translatable("misc.temperature", String.format("%1$.1f", Float.intBitsToFloat(menu.getData(1))));
			pGuiGraphics.renderTooltip(font, text, mouseX, mouseY);
		}
		if (isHovering(107, 17, 16, 59, mouseX, mouseY))
		{
			var metal = Metal.fromId(menu.getData(4));
			var text = Component.translatable("misc.metal_quantity", I18n.get("metal." + metal.getName() + ".name"), menu.getData(3));
			pGuiGraphics.renderTooltip(font, text, mouseX, mouseY);
		}
	}
}
