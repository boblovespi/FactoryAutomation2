package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.blockentity.mechanical.HorseEngineBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class HorseEngineBER extends GeoBlockRenderer<HorseEngineBE>
{
	private final BlockRenderDispatcher blockRenderer;

	public HorseEngineBER(BlockEntityRendererProvider.Context context)
	{
		super(new DefaultedBlockGeoModel<>(FactoryAutomation.name("horse_engine")));
		blockRenderer = context.getBlockRenderDispatcher();
	}

	@Override
	public void render(HorseEngineBE horseEngine, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
	{
		if (horseEngine.hasHorse())
		{
			poseStack.pushPose();
			{
				var fence = Blocks.OAK_FENCE.defaultBlockState();
				poseStack.translate(0.5, 1 + 3.5 / 16d, 0.5);
				poseStack.mulPose(BERUtils.quatFromAngleAxis(horseEngine.getRenderRot(partialTick) - 135, 0, -1, 0));
				poseStack.mulPose(BERUtils.quatFromAngleAxis(90, 0, 0, 1));
				poseStack.translate(-0.5, 1, -0.5);
				poseStack.scale(1, 3.5f, 1);
				blockRenderer.renderBatched(fence, horseEngine.getBlockPos(), horseEngine.getLevel(), poseStack, bufferSource.getBuffer(RenderType.solid()), false,
						RandomSource.create(42));
			}
			poseStack.popPose();
		}
		super.render(horseEngine, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
	}
}
