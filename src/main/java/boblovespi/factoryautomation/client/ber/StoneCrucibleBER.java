package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.blockentity.processing.StoneCrucibleBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.FastColor;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;

public class StoneCrucibleBER implements BlockEntityRenderer<StoneCrucibleBE>
{
	private final BakedQuad quad;

	public StoneCrucibleBER(BlockEntityRendererProvider.Context pContext)
	{
		var vc = new QuadBakingVertexConsumer();
		vc.addVertex(3 / 16f, 0, 3 / 16f);
		vc.setColor(0xffffffff);
		vc.addVertex(3 / 16f, 0, 13 / 16f);
		vc.setColor(0xffffffff);
		vc.addVertex(13 / 16f, 0, 13 / 16f);
		vc.setColor(0xffffffff);
		vc.addVertex(13 / 16f, 0, 3 / 16f);
		vc.setColor(0xffffffff);
		quad = vc.bakeQuad();
	}


	@Override
	public void render(StoneCrucibleBE be, float d, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
	{
		if (be.getAmount() > 0)
		{
			stack.pushPose();
			{
				var dir = be.getBlockState().getValue(StoneCrucible.FACING);
				var color = be.getColor();
				stack.translate(-2 / 16f * dir.getStepX(), 1/16f + 0.77 * be.getAmount(), -2 / 16f * dir.getStepZ());
				var a = FastColor.ARGB32.alpha(color) / 255.0F;
				var r = FastColor.ARGB32.red(color) / 255.0F;
				var g = FastColor.ARGB32.green(color) / 255.0F;
				var b = FastColor.ARGB32.blue(color) / 255.0F;
				buffer.getBuffer(RenderType.SOLID).putBulkData(stack.last(), quad, r, g, b, a, LightTexture.FULL_BRIGHT, packedOverlay, true);
			}
			stack.popPose();
		}
	}
}
