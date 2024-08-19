package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class StoneCastingVesselScreen extends AbstractContainerScreen<StoneCastingVesselMenu>
{
	private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/container/stone_casting_vessel.png");
	private Button ingot;
	private Button nugget;
	private Button sheet;
	private Button rod;
	private Button coin;
	private Button gear;
	private GuiMultiImage image;

	public StoneCastingVesselScreen(StoneCastingVesselMenu pMenu, Inventory pPlayerInventory, Component pTitle)
	{
		super(pMenu, pPlayerInventory, pTitle);
		imageHeight = 180;
		inventoryLabelY += 14;
	}

	@Override
	public void init()
	{
		super.init();
		ingot = new ExtendedButton(leftPos + 33, topPos + 20, 18, 18, Component.literal("Ingot"), unused -> setForm(0));
		addRenderableWidget(ingot);
		nugget = new ExtendedButton(leftPos + 33, topPos + 39, 18, 18, Component.literal("Nugget"), unused -> setForm(1));
		addRenderableWidget(nugget);
		sheet = new ExtendedButton(leftPos + 33, topPos + 58, 18, 18, Component.literal("Sheet"), unused -> setForm(2));
		addRenderableWidget(sheet);
		rod = new ExtendedButton(leftPos + 52, topPos + 20, 18, 18, Component.literal("Rod"), unused -> setForm(3));
		addRenderableWidget(rod);
		coin = new ExtendedButton(leftPos + 52, topPos + 39, 18, 18, Component.literal("Coin"), unused -> setForm(0));
		addRenderableWidget(coin);
		gear = new ExtendedButton(leftPos + 52, topPos + 58, 18, 18, Component.literal("Gear"), unused -> setForm(4));
		addRenderableWidget(gear);
		image = new GuiMultiImage(85, 16, 64, 64, 0, 0, 16, 16, Lists.newArrayList(
				FactoryAutomation.name("textures/block/green_sand.png"),
				FactoryAutomation.name("textures/block/casting_sand_ingot_pattern.png"),
				FactoryAutomation.name("textures/block/casting_sand_nugget_pattern.png"),
				FactoryAutomation.name("textures/block/casting_sand_sheet_pattern.png"),
				FactoryAutomation.name("textures/block/casting_sand_rod_pattern.png"),
				FactoryAutomation.name("textures/block/casting_sand_gear_pattern.png"),
				FactoryAutomation.name("textures/block/casting_sand_coin_pattern.png")));
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
		image.setTexture(menu.getForm());
		image.draw(this, graphics);
	}

	private void setForm(int form)
	{
		image.setTexture(form + 1);
		minecraft.gameMode.handleInventoryButtonClick(menu.containerId, form + 1);
	}
}
