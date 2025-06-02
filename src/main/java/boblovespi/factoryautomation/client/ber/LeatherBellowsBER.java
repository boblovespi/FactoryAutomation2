package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.processing.LeatherBellowsBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class LeatherBellowsBER extends GeoBlockRenderer<LeatherBellowsBE>
{
	public LeatherBellowsBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("leather_bellows")));
	}
}
