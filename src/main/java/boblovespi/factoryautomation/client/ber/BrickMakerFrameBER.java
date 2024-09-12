package boblovespi.factoryautomation.client.ber;

import boblovespi.factoryautomation.common.blockentity.processing.BrickMakerFrameBE;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.model.data.ModelData;

import java.util.ArrayList;
import java.util.List;

public class BrickMakerFrameBER implements BlockEntityRenderer<BrickMakerFrameBE>
{
	private final ItemRenderer itemRenderer;

	public BrickMakerFrameBER(BlockEntityRendererProvider.Context pContext)
	{
		itemRenderer = pContext.getItemRenderer();
	}

	@Override
	public void render(BrickMakerFrameBE be, float delta, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay)
	{
		var blockRender = Minecraft.getInstance().getBlockRenderer();
		var atlas = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS);
		var state = Blocks.ACACIA_STAIRS.defaultBlockState();
		var newState = Blocks.DIRT.defaultBlockState();
		var baked = blockRender.getBlockModel(state);
		var rand = RandomSource.create(42);

		var buffer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
		var oldTex = blockRender.getBlockModelShaper().getTexture(state, be.getLevel(), be.getBlockPos());
		var newTex = blockRender.getBlockModelShaper().getTexture(newState, be.getLevel(), be.getBlockPos());
		stack.pushPose();
		{
			stack.translate(0, -1, 0);
			for (Direction direction : Direction.values())
			{
				rand.setSeed(42L);
				BERUtils.renderQuadList(stack, buffer, changeQuads(baked.getQuads(state, direction, rand, ModelData.EMPTY, RenderType.SOLID), oldTex, newTex), light, overlay,
						0xffffffff);
			}
			rand.setSeed(42L);
			BERUtils.renderQuadList(stack, buffer, changeQuads(baked.getQuads(state, null, rand, ModelData.EMPTY, RenderType.SOLID), oldTex, newTex), light, overlay, 0xffffffff);
		}
		stack.popPose();
	}

	private List<BakedQuad> changeQuads(List<BakedQuad> original, TextureAtlasSprite oldTex, TextureAtlasSprite newTex)
	{
		var newList = new ArrayList<BakedQuad>(original.size());
		for (int i = 0; i < original.size(); i++)
		{
			var bakedQuad = original.get(i);
			var oldV = bakedQuad.getVertices();
			var uOffset = 0f; //newTex.getU0() - oldTex.getU0();
			var vOffset = 0f; //newTex.getV0() - oldTex.getV0();
			var a = new int[32];
			for (int j = 0; j < 32; j += 8)
			{
				a[j + 0] = oldV[j + 0];
				a[j + 1] = oldV[j + 1];
				a[j + 2] = oldV[j + 2];
				a[j + 3] = oldV[j + 3];
				uOffset = oldTex.getUOffset(Float.intBitsToFloat(oldV[j + 4]));
				vOffset = oldTex.getVOffset(Float.intBitsToFloat(oldV[j + 5]));
				a[j + 4] = Float.floatToRawIntBits(newTex.getU(uOffset));
				a[j + 5] = Float.floatToRawIntBits(newTex.getV(vOffset));
				a[j + 6] = oldV[j + 6];
				a[j + 7] = oldV[j + 7];
			}
			newList.add(new BakedQuad(a, bakedQuad.getTintIndex(), bakedQuad.getDirection(), bakedQuad.getSprite(), bakedQuad.isShade(), bakedQuad.hasAmbientOcclusion()));
		}
		return newList;
	}
}
