package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.processing.FryingPan;
import boblovespi.factoryautomation.common.blockentity.processing.FryingPanBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class FryingPanBER implements BlockEntityRenderer<FryingPanBE>
{
	private final ItemRenderer itemRenderer;

	public FryingPanBER(BlockEntityRendererProvider.Context pContext)
	{
		itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(FryingPanBE be, float d, PoseStack stack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay)
	{
		stack.pushPose();
		{
			stack.translate(0.5f, 1.3 / 16f, 0.5f);
			var result = be.getRenderResult();
			if (!result.isEmpty())
			{
				stack.scale(0.7f, 0.7f, 0.7f);
				stack.mulPose(be.getBlockState().getValue(FryingPan.FACING).getOpposite().getRotation());
				itemRenderer.renderStatic(result, ItemDisplayContext.NONE, pPackedLight, pPackedOverlay, stack, buffer, be.getLevel(),
						(int) be.getBlockPos().asLong());
			}
			else
			{			stack.scale(0.3f, 0.3f, 0.3f);
				stack.translate(0.5, 0, 0.5);
				// stack.mulPose(BERUtils.quatFromAngleAxis(90, 1, 0, 0));
				var renderStacks = be.getRenderStacks();
				for (int i = 0; i < renderStacks.size(); i++)
				{
					var renderStack = renderStacks.get(i);
					stack.pushPose();
					{
						stack.mulPose(be.getBlockState().getValue(FryingPan.FACING).getOpposite().getRotation());
						itemRenderer.renderStatic(renderStack, ItemDisplayContext.NONE, pPackedLight, pPackedOverlay, stack, buffer, be.getLevel(),
								(int) be.getBlockPos().asLong());
					}
					stack.popPose();
					stack.translate(-1, 0, 0);
					if (i == 1)
						stack.translate(2, 0, -1);
				}
			}
		}
		stack.popPose();
	}
}