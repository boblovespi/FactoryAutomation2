package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.processing.BrickCastingVesselBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class BrickCastingVesselBER implements BlockEntityRenderer<BrickCastingVesselBE>
{
	private final ItemRenderer itemRenderer;

	public BrickCastingVesselBER(BlockEntityRendererProvider.Context pContext)
	{
		itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(BrickCastingVesselBE be, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int packedLight, int packedOverlay)
	{
		var itemStack = be.getRenderStack();
		if (itemStack.isEmpty())
			return;
		stack.pushPose();
		{
			stack.translate(0.5f, 0.5f, 0.5f);
			// stack.scale(12/16f, 12/16f, 12/16f);
			var state = be.getLevel().getBlockState(be.getBlockPos());
			if(state.is(FABlocks.BRICK_CASTING_VESSEL.get())){
				stack.mulPose(BERUtils.quatFromAngleAxis(state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot(), 0, 1, 0));
			}
			itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, bufferSource, be.getLevel(), 42);
		}
		stack.popPose();
	}
}
