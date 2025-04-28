package boblovespi.factoryautomation.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class ItemLookingButton extends ExtendedButton {

    int labelX;
    int labelY;
    Item displayItem;

    public ItemLookingButton(ItemLike displayItem, int xPos, int yPos, int labelX, int labelY, Component displayString, OnPress handler) {
        super(xPos, yPos, 16, 16, displayString, handler);
        this.labelX = labelX;
        this.labelY = labelY;
        this.displayItem = displayItem.asItem();
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        guiGraphics.renderFakeItem(this.displayItem.getDefaultInstance(), this.getX(), this.getY());

        if(this.isHovered){
            final FormattedText buttonText = this.getMessage();
            guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.labelX + (this.getX() + this.width / 2), this.labelY + (this.getY() + (this.height - 8) / 2), getFGColor());
        }
    }

}
