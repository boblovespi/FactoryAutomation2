package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.mechanical.HandCrank;
import boblovespi.factoryautomation.common.blockentity.mechanical.HandCrankBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;

public class HandCrankBER implements BlockEntityRenderer<HandCrankBE>
{
	private final BlockRenderDispatcher blockRenderer;

	public HandCrankBER(BlockEntityRendererProvider.Context pContext)
	{
		blockRenderer = pContext.getBlockRenderDispatcher();
	}

	@Override
	public void render(HandCrankBE be, float delta, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay)
	{
		stack.pushPose();
		{
			var hanging = be.getBlockState().getValue(HandCrank.HANGING);
			stack.translate(0.5, 0, 0.5);
			stack.mulPose(BERUtils.quatFromAngleAxis(be.getRenderRot(delta), 0,1, 0));
			stack.translate(-0.5, 0, -0.5);
			blockRenderer.renderBatched(be.getBlockState(), be.getBlockPos(), be.getLevel(), stack, bufferSource.getBuffer(RenderType.SOLID), false, RandomSource.create(42));
		}
		stack.popPose();
	}
}
