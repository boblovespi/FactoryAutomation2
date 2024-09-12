package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.processing.StoneCastingVesselBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.FastColor;

public class StoneCastingVesselBER implements BlockEntityRenderer<StoneCastingVesselBE>
{
	private final ItemRenderer itemRenderer;

	public StoneCastingVesselBER(BlockEntityRendererProvider.Context pContext)
	{
		itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(StoneCastingVesselBE be, float d, PoseStack stack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay)
	{
		stack.pushPose();
		{
			stack.translate(0.5f, 0.407f, 0.5f);
			stack.scale(0.99f, 1, 0.99f);
			stack.mulPose(BERUtils.quatFromAngleAxis(-90, 1, 0, 0));
			var itemStack = be.getRenderStack();
			var temp = be.getRenderTemp() + 300 - 273;
			BERUtils.renderColoredItemStack(itemStack, itemRenderer, stack, buffer, pPackedLight, pPackedOverlay, be.getLevel(), (int) be.getBlockPos().asLong(),
					FastColor.ARGB32.colorFromFloat(1, red(temp), green(temp), blue(temp)));
		}
		stack.popPose();
	}

	public float red(float temp)
	{
		return 1f;
	}

	public float green(float temp)
	{
		if (temp > 900f)
			return (0.5f / 900f) * temp;
		else if (temp > 450f)
			return (0.4f / 450f) * temp - 0.3f;
		else
			return (-0.9f / 450f) * temp + 1f;
	}

	public float blue(float temp)
	{
		if (temp > 1350f)
			return (0.8f / 450) * temp - 2.4f;
		else if (temp > 450f)
			return 0f;
		else
			return (-1f / 450f) * temp + 1f;
	}
}
