package boblovespi.factoryautomation.client.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.EmptyFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;

public class GuiFluidTank
{
	private final GuiBar gauge;
	private final int x;
	private final int y;
	private final int w;
	private final int h;
	@Nullable
	private GuiTexturedBar fluidBar;
	private Fluid fluid;
	private int color;

	public GuiFluidTank(ResourceLocation sprite, int x, int y, int w, int h, int gaugeX, int gaugeY)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		gauge = new GuiBar(sprite, x, y, gaugeX, gaugeY, w, h, GuiBar.ProgressDirection.UP);
		fluid = Fluids.EMPTY;
	}

	public void draw(AbstractContainerScreen<?> gui, GuiGraphics graphics, Fluid fluid, float percentage)
	{
		if (this.fluid != fluid)
		{
			this.fluid = fluid;
			if (fluid instanceof  EmptyFluid)
				fluidBar = null;
			else
			{
				var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid).getStillTexture());
				color = IClientFluidTypeExtensions.of(fluid).getTintColor();
				fluidBar = new GuiTexturedBar(x, y, w, h, sprite, 16, 16);
			}
		}
		if (this.fluid instanceof EmptyFluid || fluidBar == null)
			return;
		fluidBar.draw(gui, graphics, percentage, color);
		gauge.draw(gui, graphics, 1);
	}

	public void drawTooltip(AbstractContainerScreen<?> gui, GuiGraphics graphics, Font font, int mouseX, int mouseY, Fluid fluid, int amount)
	{
		if (mouseX >= x + gui.getGuiLeft() && mouseX <= x + gui.getGuiLeft() + w && mouseY >= y + gui.getGuiTop() && mouseY <= y + gui.getGuiTop() + h)
		{
			var description = List.of(fluid.getFluidType().getDescription(FluidStack.EMPTY), Component.translatable("misc.millibucket", amount).withStyle(ChatFormatting.GRAY));
			graphics.renderComponentTooltip(font, description, mouseX, mouseY);
		}
	}
}
