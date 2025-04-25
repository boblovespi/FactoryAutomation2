package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.mechanical.SmallWaterwheelBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SmallWaterwheelBER extends GeoBlockRenderer<SmallWaterwheelBE>
{
	public SmallWaterwheelBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("small_waterwheel")));
	}
}
