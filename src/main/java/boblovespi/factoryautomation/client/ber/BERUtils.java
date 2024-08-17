package boblovespi.factoryautomation.client.ber;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.joml.Quaternionf;

import java.util.List;

public class BERUtils
{
	public static Quaternionf quatFromAngleAxis(float deg, float x, float y, float z)
	{
		float sin = Mth.sin((float) (deg * Math.PI / 360f));
		return new Quaternionf(x * sin, y * sin, z * sin, Mth.cos((float) (deg * Math.PI / 360f)));
	}

	public static void renderColoredItemStack(ItemStack itemStack, ItemRenderer itemRenderer, PoseStack stack, MultiBufferSource buffer, int pPackedLight, int pPackedOverlay,
											  Level level, int seed, int color)
	{
		if (!itemStack.isEmpty())
		{
			var bakedmodel = itemRenderer.getModel(itemStack, level, null, seed);
			stack.pushPose();
			{
				bakedmodel = ClientHooks.handleCameraTransforms(stack, bakedmodel, ItemDisplayContext.NONE, false);
				stack.translate(-0.5F, -0.5F, -0.5F);
				if (!bakedmodel.isCustomRenderer() && !itemStack.is(Items.TRIDENT))
				{
					boolean flag1;
					if (itemStack.getItem() instanceof BlockItem blockitem)
					{
						Block block = blockitem.getBlock();
						flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
					}
					else
						flag1 = true;

					for (var model : bakedmodel.getRenderPasses(itemStack, flag1))
					{
						for (var rendertype : model.getRenderTypes(itemStack, flag1))
						{
							VertexConsumer vertexconsumer;
							if (flag1)
								vertexconsumer = ItemRenderer.getFoilBufferDirect(buffer, rendertype, true, itemStack.hasFoil());
							else
								vertexconsumer = ItemRenderer.getFoilBuffer(buffer, rendertype, true, itemStack.hasFoil());

							renderModelLists(model, pPackedLight, pPackedOverlay, stack, vertexconsumer, color);
						}
					}
				}
				else
					IClientItemExtensions.of(itemStack).getCustomRenderer().renderByItem(itemStack, ItemDisplayContext.NONE, stack, buffer, pPackedLight, pPackedOverlay);
			}
			stack.popPose();
		}
	}

	public static void renderModelLists(BakedModel pModel, int pCombinedLight, int pCombinedOverlay, PoseStack pPoseStack, VertexConsumer pBuffer, int color)
	{
		RandomSource randomsource = RandomSource.create();

		for (Direction direction : Direction.values())
		{
			randomsource.setSeed(42L);
			renderQuadList(pPoseStack, pBuffer, pModel.getQuads(null, direction, randomsource), pCombinedLight, pCombinedOverlay, color);
		}

		randomsource.setSeed(42L);
		renderQuadList(pPoseStack, pBuffer, pModel.getQuads(null, null, randomsource), pCombinedLight, pCombinedOverlay, color);
	}

	public static void renderQuadList(PoseStack pPoseStack, VertexConsumer pBuffer, List<BakedQuad> pQuads, int pCombinedLight, int pCombinedOverlay, int color)
	{
		var posestack$pose = pPoseStack.last();
		for (BakedQuad bakedquad : pQuads)
		{
			var f = (float) FastColor.ARGB32.alpha(color) / 255.0F;
			var f1 = (float) FastColor.ARGB32.red(color) / 255.0F;
			var f2 = (float) FastColor.ARGB32.green(color) / 255.0F;
			var f3 = (float) FastColor.ARGB32.blue(color) / 255.0F;
			pBuffer.putBulkData(posestack$pose, bakedquad, f1, f2, f3, f, pCombinedLight, pCombinedOverlay, true); // Neo: pass readExistingColor=true
		}
	}
}
