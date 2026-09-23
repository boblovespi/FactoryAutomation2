package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.mechanical.LargeWaterwheelBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LargeWaterwheelBER extends GeoBlockRenderer<LargeWaterwheelBE>
{
	public LargeWaterwheelBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("large_waterwheel")));
	}
}
