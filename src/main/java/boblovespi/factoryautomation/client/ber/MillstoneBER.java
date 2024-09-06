package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.MillstoneBE;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.loading.math.MathParser;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MillstoneBER extends GeoBlockRenderer<MillstoneBE>
{
	public MillstoneBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("millstone"))
		{
			@Override
			public void applyMolangQueries(AnimationState<MillstoneBE> animationState, double animTime)
			{
				super.applyMolangQueries(animationState, animTime);
				MathParser.setVariable("query.rot", () -> animationState.getAnimatable().getRenderRot(animationState.getPartialTick()));
			}
		});
	}
}
