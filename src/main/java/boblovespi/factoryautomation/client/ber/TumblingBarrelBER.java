package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.processing.TumblingBarrelBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TumblingBarrelBER extends GeoBlockRenderer<TumblingBarrelBE> {

    public TumblingBarrelBER(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("tumbling_barrel")));
    }

}
