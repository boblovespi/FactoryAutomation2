package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.processing.BambooBasketBE;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemDisplayContext;

public class BambooBasketBER implements BlockEntityRenderer<BambooBasketBE>
{
	private final ItemRenderer itemRenderer;

	public BambooBasketBER(BlockEntityRendererProvider.Context context)
	{
		itemRenderer = context.getItemRenderer();
	}

	@Override
	public void render(BambooBasketBE be, float partialTick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay)
	{
		var items = be.getRenderStacks();
		var k = (int) be.getBlockPos().asLong();

		for (var i = 0; i < 4; i++)
		{
			var item = items.getStackInSlot(i);
			if (!item.isEmpty())
			{
				stack.pushPose();
				stack.translate(0.5, 1 / 16f + 2.5 / 256, 0.5);
				stack.mulPose(Axis.XP.rotationDegrees(90.0F));
				stack.translate(3.5 / 16f * (1 - 2 * (i >> 1)), 3.5 / 16f * (1 - 2 * (i & 0b1)), 0.0F);
				stack.scale(5 / 16f, 5 / 16f, 5 / 16f);
				itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, buffer, be.getLevel(), k + i);
				stack.popPose();
			}
		}
		var item = items.getStackInSlot(4);
		if (!item.isEmpty())
		{
			stack.pushPose();
			stack.translate(0.5, 1 / 16f + 2.5 / 256, 0.5);
			stack.mulPose(Axis.XP.rotationDegrees(-90.0F));
			stack.scale(5 / 16f, 5 / 16f, 5 / 16f);
			itemRenderer.renderStatic(item, ItemDisplayContext.FIXED, packedLight, packedOverlay, stack, buffer, be.getLevel(), k + 4);
			stack.popPose();
		}
	}
}
