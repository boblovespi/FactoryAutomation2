package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.ChoppingBlockBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class ChoppingBlockBER implements BlockEntityRenderer<ChoppingBlockBE>
{
	private final ItemRenderer itemRenderer;

	public ChoppingBlockBER(BlockEntityRendererProvider.Context pContext)
	{
		itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(ChoppingBlockBE be, float d, PoseStack stack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay)
	{
		stack.pushPose();
		{
			stack.translate(0.5f, 0.5f + 0.25f, 0.5f);
			stack.scale(0.5f, 0.5f, 0.5f);
			itemRenderer.renderStatic(be.getRenderStack(), ItemDisplayContext.NONE, pPackedLight, pPackedOverlay, stack, buffer, be.getLevel(), (int) be.getBlockPos().asLong());
		}
		stack.popPose();
	}
}
