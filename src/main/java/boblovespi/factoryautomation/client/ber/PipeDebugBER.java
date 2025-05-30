package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.logistics.PipeBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
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
		if (!Minecraft.getInstance().getDebugOverlay().showDebugScreen())
			return;
		var buffer = bufferSource.getBuffer(BERUtils.LINES_OVERLAY);
		var innerCube = AABB.ofSize(BlockPos.ZERO.getCenter(), 0.1, 0.1, 0.1);
		if (pipe.graph == null)
		{
			LevelRenderer.renderLineBox(poseStack, buffer, innerCube, 1, 0, 0, 1);
			return;
		}
		else if (!pipe.isGraphOwner)
		{
			LevelRenderer.renderLineBox(poseStack, buffer, innerCube, 1, 1, 0, 1);
			return;
		}
		var a = (float) FastColor.ARGB32.alpha(pipe.graph.getColor()) / 255.0F;
		var r = (float) FastColor.ARGB32.red(pipe.graph.getColor()) / 255.0F;
		var g = (float) FastColor.ARGB32.green(pipe.graph.getColor()) / 255.0F;
		var b = (float) FastColor.ARGB32.blue(pipe.graph.getColor()) / 255.0F;
		var size = 0.7f;
		LevelRenderer.renderLineBox(poseStack, buffer, innerCube, 0, 1, 1, 1);
		for (var pos : pipe.graph.getVertices())
		{
			var center = pos.subtract(pipe.getBlockPos()).getCenter();
			var mainCube = AABB.ofSize(center, size, size, size);
			LevelRenderer.renderLineBox(poseStack, buffer, mainCube, r, g, b, a);
			for (var dir : pipe.graph.getEdgesForVertex(pos))
			{
				var sideCube = AABB.ofSize(center.relative(dir, 0.375f), size / 3, size / 3, size / 3);
				LevelRenderer.renderLineBox(poseStack, buffer, sideCube, r, g, b, a);
			}
			if (pipe.graph.hasData(pos))
			{
				for (var entry : pipe.graph.getData(pos).entrySet())
				{
					var sideCube = AABB.ofSize(center.relative(entry.getKey(), 0.375f), size / 3, size / 3, size / 3);
					LevelRenderer.renderLineBox(poseStack, buffer, sideCube, entry.getValue().isInput() ? 0 : 1, entry.getValue().isInput() ? 0 : 0.5f, entry.getValue().isInput() ? 1 : 0, a);
				}
			}
		}
	}
}
