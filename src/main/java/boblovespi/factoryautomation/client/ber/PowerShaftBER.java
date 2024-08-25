package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.PowerShaft;
import boblovespi.factoryautomation.common.blockentity.PowerShaftBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;

public class PowerShaftBER implements BlockEntityRenderer<PowerShaftBE>
{
	private final BlockRenderDispatcher blockRenderer;

	public PowerShaftBER(BlockEntityRendererProvider.Context pContext)
	{
		blockRenderer = pContext.getBlockRenderDispatcher();
	}

	@Override
	public void render(PowerShaftBE be, float delta, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay)
	{
		stack.pushPose();
		{
			var axis = be.getBlockState().getValue(PowerShaft.AXIS);
			stack.translate(0.5 - axis.choose(0.5, 0, 0), 0.5 - axis.choose(0, 0.5, 0), 0.5 - axis.choose(0, 0, 0.5));
			stack.mulPose(BERUtils.quatFromAngleAxis(be.getRenderRot(delta), axis.choose(1, 0, 0), axis.choose(0, 1, 0), axis.choose(0, 0, 1)));
			blockRenderer.renderBatched(be.getBlockState(), be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();
	}
}
