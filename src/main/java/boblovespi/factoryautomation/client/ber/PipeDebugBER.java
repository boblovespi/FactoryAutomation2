package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.logistics.PipeBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.AABB;

public class PipeDebugBER implements BlockEntityRenderer<PipeBE>
{
	private final BlockRenderDispatcher blockRenderer;

	public PipeDebugBER(BlockEntityRendererProvider.Context pContext)
	{
		blockRenderer = pContext.getBlockRenderDispatcher();
	}

	@Override
	public void render(PipeBE pipe, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
	{
		if (pipe.graph == null || !pipe.isGraphOwner)
			return;
		var a = (float) FastColor.ARGB32.alpha(pipe.graph.getColor()) / 255.0F;
		var r = (float) FastColor.ARGB32.red(pipe.graph.getColor()) / 255.0F;
		var g = (float) FastColor.ARGB32.green(pipe.graph.getColor()) / 255.0F;
		var b = (float) FastColor.ARGB32.blue(pipe.graph.getColor()) / 255.0F;
		var size = 0.7f;
		for (var pos : pipe.graph.getVertices())
		{
			var aabb2 = AABB.ofSize(pos.subtract(pipe.getBlockPos()).getCenter(), size, size, size);
			LevelRenderer.renderLineBox(poseStack, bufferSource.getBuffer(RenderType.lines()), aabb2, r, g, b, a);
		}
	}
}
