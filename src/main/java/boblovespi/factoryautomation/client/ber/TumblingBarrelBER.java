package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.processing.TumblingBarrel;
import boblovespi.factoryautomation.common.blockentity.processing.TumblingBarrelBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class TumblingBarrelBER extends GeoBlockRenderer<TumblingBarrelBE> {

    public TumblingBarrelBER(BlockEntityRendererProvider.Context context) {
        super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("tumbling_barrel")));
    }

    @Override
    protected Direction getFacing(TumblingBarrelBE block)
    {
        return block.getBlockState().getValue(TumblingBarrel.AXIS) == Direction.Axis.X ? Direction.NORTH : Direction.EAST;
    }
}
