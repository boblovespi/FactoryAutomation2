package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.menu.TumblingBarrelMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluid;

public class TumblingBarrelScreen extends AbstractContainerScreen<TumblingBarrelMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/tumbling_barrel.png");
	private final GuiBar progressBar;
	private final GuiFluidTank input;
	private final GuiFluidTank output;

	public TumblingBarrelScreen(TumblingBarrelMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		imageHeight = 180;
		inventoryLabelY += 14;
		progressBar = new GuiBar(BACKGROUND_TEXTURE, 80, 34, 177, 14, 22, 16, GuiBar.ProgressDirection.RIGHT);
		input = new GuiFluidTank(BACKGROUND_TEXTURE, 8, 8, 16, 59, 182, 32);
		output = new GuiFluidTank(BACKGROUND_TEXTURE, 152, 8, 16, 59, 182, 32);
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
		input.draw(this, graphics, getFluid(3), menu.getData(1) / 2000f);
		output.draw(this, graphics, getFluid(4), menu.getData(2) / 2000f);
	}

	@Override
	protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.renderTooltip(graphics, mouseX, mouseY);
		input.drawTooltip(this, graphics, font, mouseX, mouseY, getFluid(3), menu.getData(1));
		output.drawTooltip(this, graphics, font, mouseX, mouseY, getFluid(4), menu.getData(2));
	}

	private Fluid getFluid(int index)
	{
		return BuiltInRegistries.FLUID.byId(menu.getData(index));
	}
}
