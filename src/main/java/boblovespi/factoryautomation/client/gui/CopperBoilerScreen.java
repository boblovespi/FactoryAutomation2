package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.client.ber.BERUtils;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.menu.CopperBoilerMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.material.Fluids;

public class CopperBoilerScreen extends AbstractContainerScreen<CopperBoilerMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/copper_boiler.png");
	private final GuiBar boilerTemperatureBar;
	private final GuiBar waterTemperatureBar;
	private final GuiFluidTank waterTankBar;
	private final GuiFluidTank steamTankBar;

	public CopperBoilerScreen(CopperBoilerMenu menu, Inventory playerInventory, Component title)
	{
		super(menu, playerInventory, title);
		imageHeight = 180;
		inventoryLabelY += 14;
		boilerTemperatureBar = new GuiBar(BACKGROUND_TEXTURE, 32, 16, 176, 31, 6, 61, GuiBar.ProgressDirection.UP);
		waterTemperatureBar = new GuiBar(BACKGROUND_TEXTURE, 47, 16, 176, 31, 6, 61, GuiBar.ProgressDirection.UP);
		waterTankBar = new GuiFluidTank(BACKGROUND_TEXTURE, 57, 16, 6, 61, 182, 31);
		steamTankBar = new GuiFluidTank(BACKGROUND_TEXTURE, 73, 16, 6, 61, 182, 31);
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
		var temp = Float.intBitsToFloat(menu.getData(0));
		boilerTemperatureBar.draw(this, graphics, Mth.clampedMap(temp, 300, 1000, 0, 1));
		waterTemperatureBar.draw(this, graphics, Mth.clampedMap(Float.intBitsToFloat(menu.getData(1)), 300, 1000, 0, 1));
		waterTankBar.draw(this, graphics, Fluids.WATER, menu.getData(2) / 10000f);
		steamTankBar.draw(this, graphics, FAFluids.STEAM_SOURCE.get(), menu.getData(3) / 10000f);

		// pressure gauge
		graphics.pose().pushPose();
		{
			var vc = graphics.bufferSource().getBuffer(RenderType.gui());
			graphics.pose().translate(leftPos + 119.5, topPos + 46.5, 0);
			var bar0 = 5 * Mth.PI / 4;
			var bar6 = -Mth.PI / 4;
			var bar4p5 = Mth.lerp(4.5f / 6, bar0, bar6);
			var barPressureLimit = Mth.clampedMap(temp, 450, 700, 1, 0);
			barPressureLimit = barPressureLimit * 3 + 1.5f;
			var barplm1 = Mth.lerp((barPressureLimit - 1) / 6f, bar0, bar6);
			barPressureLimit = Mth.lerp(barPressureLimit / 6f, bar0, bar6);

			GuiUtils.drawSector(vc, graphics.pose().last(), 0, 0, 27, -barplm1, -barPressureLimit, 0xffFFBB1E, 5);
			GuiUtils.drawSector(vc, graphics.pose().last(), 0, 0, 27, -barPressureLimit, -bar4p5, 0xffEF5847, 5);
			GuiUtils.drawSector(vc, graphics.pose().last(), 0, 0, 27, -bar4p5, -bar6, 0xffDB274B, 7);

			for (int i = 0; i <= 6; i++)
			{
				var angle = Mth.lerp(i / 6f, bar0, bar6);
				var number = String.format("%d", i + 1);
				var x = 23 * Mth.cos(angle) - font.width(number) / 2f;
				var y = -23 * Mth.sin(angle) - font.lineHeight / 2f + 1.5f;
				graphics.drawString(font, number, (int) x, (int) y, 0xff373737, false);
			}

			var pressureScale = Float.intBitsToFloat(menu.getData(4)) / 600_000f - 1 / 6f;
			var pressureAngle = Mth.clampedLerp(bar0, bar6, pressureScale);
			graphics.pose().mulPose(BERUtils.quatFromAngleAxis((float) Math.toDegrees(pressureAngle), 0, 0, -1));
			var mat = graphics.pose().last();
			vc = graphics.bufferSource().getBuffer(RenderType.gui());
			vc.addVertex(mat, -6, -0.5f, 0).setColor(0xff373737);
			vc.addVertex(mat, -6, 0.5f, 0).setColor(0xff373737);
			vc.addVertex(mat, 20, 0.5f, 0).setColor(0xff373737);
			vc.addVertex(mat, 20, -0.5f, 0).setColor(0xff373737);
			// graphics.hLine(0, 20, 0, 0xff000000);
		}
		graphics.pose().popPose();

		graphics.pose().pushPose();
		{
			graphics.pose().translate(0, 0, 1);
			graphics.blit(BACKGROUND_TEXTURE, leftPos + 117, topPos + 44, 190, 0, 5, 5);
		}
		graphics.pose().popPose();
	}

	@Override
	protected void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY)
	{
		super.renderTooltip(graphics, mouseX, mouseY);
		if (boilerTemperatureBar.isHovering(this, mouseX, mouseY))
		{
			var text = Component.translatable("misc.temperature", String.format("%1$.1f", Float.intBitsToFloat(menu.getData(0))));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
		if (waterTemperatureBar.isHovering(this, mouseX, mouseY))
		{
			var text = Component.translatable("misc.temperature", String.format("%1$.1f", Float.intBitsToFloat(menu.getData(1))));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
		if (isHovering(57, 16, 6, 61, mouseX, mouseY))
		{
			var text = Component.translatable("misc.millibucket", menu.getData(2));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
		if (isHovering(73, 16, 6, 61, mouseX, mouseY))
		{
			var text = Component.translatable("misc.millibucket", menu.getData(3));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
		// pressure gauge
		var dx = mouseX - leftPos - 119.5;
		var dy = mouseY - topPos - 46.5;
		if (dx * dx + dy * dy <= 31 * 31)
		{
			var text = Component.translatable("misc.pressure", String.format("%1$.2f", Float.intBitsToFloat(menu.getData(4)) / 100_000f));
			graphics.renderTooltip(font, text, mouseX, mouseY);
		}
	}
}
