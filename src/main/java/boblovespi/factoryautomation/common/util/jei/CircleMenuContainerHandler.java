package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.client.gui.CircleMenuScreen;
import mezz.jei.api.gui.handlers.IGuiContainerHandler;
import mezz.jei.api.helpers.IJeiHelpers;
import net.minecraft.client.renderer.Rect2i;

import java.util.List;

public class CircleMenuContainerHandler implements IGuiContainerHandler<CircleMenuScreen> {

    private final IJeiHelpers helpers;

    public CircleMenuContainerHandler(IJeiHelpers helpers) {
        this.helpers = helpers;
    }

    @Override
    public List<Rect2i> getGuiExtraAreas(CircleMenuScreen containerScreen) {
        return List.of(new Rect2i(0, 0, containerScreen.getMinecraft().getWindow().getWidth(), containerScreen.getMinecraft().getWindow().getHeight()));
    }
}
