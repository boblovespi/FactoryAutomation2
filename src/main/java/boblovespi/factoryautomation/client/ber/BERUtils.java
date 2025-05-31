package boblovespi.factoryautomation.client.ber;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.fluids.FluidType;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.List;
import java.util.OptionalDouble;

public class BERUtils
{
	public static final RenderType LINES_OVERLAY = RenderType.create(
			"lines_overlay",
			DefaultVertexFormat.POSITION_COLOR_NORMAL,
			VertexFormat.Mode.LINES,
			1536,
			RenderType.CompositeState.builder()
									 .setShaderState(RenderStateShard.RENDERTYPE_LINES_SHADER)
									 .setLineState(new RenderStateShard.LineStateShard(OptionalDouble.empty()))
									 .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
									 .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
									 .setWriteMaskState(RenderStateShard.COLOR_WRITE)
									 .setCullState(RenderStateShard.NO_CULL)
									 .createCompositeState(false));

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

	/**
	 * Renders a single quad of the specified fluid.
	 *
	 * @param u0 Starting U texture coordinate (0.0-1.0, left edge of texture subsection)
	 * @param v0 Starting V texture coordinate (0.0-1.0, bottom edge of texture subsection)
	 * @param u1 Ending U texture coordinate (0.0-1.0, right edge of texture subsection)
	 * @param v1 Ending V texture coordinate (0.0-1.0, top edge of texture subsection)
	 */
	public static void renderFluidQuad(PoseStack stack, VertexConsumer buffer, FluidType fluid, int light, int overlay, float x0, float y0, float z0, float x1, float y1, float z1,
									   float x2, float y2, float z2, float u0, float v0, float u1, float v1)
	{
		var sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid).getStillTexture());
		var color = IClientFluidTypeExtensions.of(fluid).getTintColor();
		u0 = sprite.getU(u0);
		v0 = sprite.getV(v0);
		u1 = sprite.getU(u1);
		v1 = sprite.getV(v1);
		var normal = new Vector3f(x1 - x2, y1 - y2, z1 - z2);
		normal.cross(x1 - x0, y1 - y0, z1 - z0);
		if (normal.lengthSquared() > 1.01f || normal.lengthSquared() < 0.99f)
			normal.normalize();
		stack.last().transformNormal(normal, normal);
		buffer.addVertex(stack.last(), x0, y0, z0)
			  .setUv(u0, v0)
			  .setColor(color)
			  .setLight(light)
			  .setOverlay(overlay)
			  .setNormal(normal.x, normal.y, normal.z);
		buffer.addVertex(stack.last(), x1, y1, z1)
			  .setUv(u1, v0)
			  .setColor(color)
			  .setLight(light)
			  .setOverlay(overlay)
			  .setNormal(normal.x, normal.y, normal.z);
		buffer.addVertex(stack.last(), x2, y2, z2)
			  .setUv(u1, v1)
			  .setColor(color)
			  .setLight(light)
			  .setOverlay(overlay)
			  .setNormal(normal.x, normal.y, normal.z);
		buffer.addVertex(stack.last(), x0 - x1 + x2, y0 - y1 + y2, z0 - z1 + z2)
			  .setUv(u0, v1)
			  .setColor(color)
			  .setLight(light)
			  .setOverlay(overlay)
			  .setNormal(normal.x, normal.y, normal.z);
	}
}
