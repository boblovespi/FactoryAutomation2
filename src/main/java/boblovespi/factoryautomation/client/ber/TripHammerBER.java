package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.processing.TripHammerBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TripHammerBER extends GeoBlockRenderer<TripHammerBE> {

    public TripHammerBER(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("trip_hammer")));
    }
}
