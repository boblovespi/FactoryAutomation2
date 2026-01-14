package boblovespi.factoryautomation.client.gui;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.menu.StoneCastingVesselMenu;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.GearMaterial;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class CircleMenuScreen extends AbstractContainerScreen<StoneCastingVesselMenu> {

    private Button ingot;
    private Button nugget;
    private Button sheet;
    private Button rod;
    private Button coin;
    private Button gear;
    private GuiMultiImage image;
    private static final ResourceLocation BACKGROUND_TEXTURE = FactoryAutomation.name("textures/gui/hud/circle_menu_bg.png");
    protected int imageWidth = 176;
    protected int imageHeight = 166;
    protected int centeredX;
    protected int centeredY;
    protected final StoneCastingVesselMenu menu;
    protected final Component playerInventoryTitle;

    public int sliceNum = 6;
    //TODO rn im lazy, since idk if this will be re-used elsewhere, but, if we want to, the first step towards this being modular is to turn this integer into like a list that holds custom class of "RadialPage" that would store the data on each page instead of the hardcoded "pages" in init(); and the lenght of this list could be used in the calculations instead of this hardcoded int;
    public int radius = 64;

    public CircleMenuScreen(StoneCastingVesselMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        this.menu = pMenu;
        this.playerInventoryTitle = pPlayerInventory.getDisplayName();
        imageHeight = 180;
    }

    @Override
    public void init() {
        super.init();
        this.centeredX = (this.width) / 2;
        this.centeredY = (this.height) / 2;

        ingot = placeItemLookingButtonRadially(Items.IRON_INGOT,"Ingot", 0, 1);
        addRenderableWidget(ingot);
        nugget = placeItemLookingButtonRadially(Items.IRON_NUGGET, "Nugget", 1, 2);
        addRenderableWidget(nugget);
        sheet = placeItemLookingButtonRadially(FAItems.IRON_THINGS.get(Form.SHEET).get(), "Sheet", 2, 3);
        addRenderableWidget(sheet);
        rod = placeItemLookingButtonRadially(FAItems.IRON_THINGS.get(Form.ROD).get(), "Rod", 3, 4);
        addRenderableWidget(rod);
        coin = placeItemLookingButtonRadially(Items.IRON_INGOT, "Coin", 0, 5);
        addRenderableWidget(coin);
        gear = placeItemLookingButtonRadially(FAItems.GEARS.get(GearMaterial.IRON).get(), "Gear", 4, 6);
        addRenderableWidget(gear);
        image = new GuiMultiImage(0, 0, 64, 64, 0, 0, 16, 16, Lists.newArrayList(
                FactoryAutomation.name("textures/block/green_sand.png"),
                FactoryAutomation.name("textures/block/casting_sand_ingot_pattern.png"),
                FactoryAutomation.name("textures/block/casting_sand_nugget_pattern.png"),
                FactoryAutomation.name("textures/block/casting_sand_sheet_pattern.png"),
                FactoryAutomation.name("textures/block/casting_sand_rod_pattern.png"),
                FactoryAutomation.name("textures/block/casting_sand_gear_pattern.png"),
                FactoryAutomation.name("textures/block/casting_sand_coin_pattern.png")));
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {

        drawRadialSeparator(graphics, 0);
        drawRadialSeparator(graphics, 1);
        drawRadialSeparator(graphics, 2);
        drawRadialSeparator(graphics, 3);
        drawRadialSeparator(graphics, 4);
        drawRadialSeparator(graphics, 5);

        graphics.pose().pushPose();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.blit(BACKGROUND_TEXTURE, centeredX-80, centeredY-80, 0, 0, 160, 160, 160, 160);
        graphics.pose().popPose();

        renderBackground(graphics, mouseX, mouseY, partialTick);
        for (net.minecraft.client.gui.components.Renderable renderable : this.renderables) {
            renderable.render(graphics, mouseX, mouseY, partialTick);
        }
		graphics.pose().translate(0, 0, 1);
		image.draw(graphics, centeredY -32, centeredX -32);
		graphics.pose().translate(0, 0, -1);
        this.renderLabels(graphics, mouseX, mouseY);
    }

    protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
        image.setTexture(menu.getForm());

    }

    private void setForm(int form) {
        image.setTexture(form + 1);
        minecraft.gameMode.handleInventoryButtonClick(menu.containerId, form + 1);
    }

    @Override
    public void renderTransparentBackground(GuiGraphics guiGraphics) {

    }

    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawCenteredString(this.font, this.title, centeredX, centeredY-100, 16777215);
    }

    public Button placeItemLookingButtonRadially(ItemLike itemLike, String literal, int formIndex, int sliceSelect){
        // int sliceSizeInDeg = 360/this.sliceNum;
        // double x = radius * Math.cos(Math.PI * 2 * ((double) (sliceSizeInDeg * sliceSelect) /360));
        // double y = radius * Math.sin(Math.PI * 2 * ((double) (sliceSizeInDeg * sliceSelect) /360));
        // return new ItemLookingButton(itemLike, (int)Math.round(centeredX + x)-8, (int)Math.round(centeredY + y)-8, (int)Math.round((-x)), (int)Math.round((-y)+40), Component.literal(literal), (unused) -> {
        //     setForm(formIndex);
        //     this.onClose();
        // });
		return new RadialButton(itemLike, centeredX, centeredY, radius, 80, sliceNum, sliceSelect - 1, Component.literal(literal), a -> {
			setForm(formIndex);
			onClose();
		});
    }

    public void drawRadialSeparator(GuiGraphics graphics, int sliceSelect) {
        int sliceSizeInDeg = 360/this.sliceNum;
        double angle = (sliceSizeInDeg*sliceSelect)+((double) sliceSizeInDeg /2);

        graphics.pose().pushPose();
        graphics.pose().rotateAround(Axis.ZN.rotation((float) Math.toRadians(angle)) , centeredX, centeredY, 0);
        graphics.hLine(centeredX, centeredX+79, centeredY, FastColor.ARGB32.color(0,0,0));
        graphics.pose().popPose();
    }

}


