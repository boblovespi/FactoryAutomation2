package boblovespi.factoryautomation.client.model;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Stream;

public class PartialDynamicTextureModel implements IDynamicBakedModel
{
	private final BakedModel base;
	private final Map<Block, List<List<BakedQuad>>> unculledCache; // block -> override num -> quad list map
	private final Map<Direction, Map<Block, List<List<BakedQuad>>>> culledCache; // direction -> block -> override num -> quad list map
	private final List<BakedModel> elementOverrides; // override num -> baked model map

	public PartialDynamicTextureModel(BakedModel base, List<BakedModel> elementOverrides)
	{
		this.base = base;
		this.elementOverrides = elementOverrides;
		unculledCache = new IdentityHashMap<>();
		culledCache = new EnumMap<>(Direction.class);
		for (var dir : Direction.values())
			culledCache.put(dir, new IdentityHashMap<>());
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType)
	{
		var blockData = extraData.get(FactoryAutomation.PARTIAL_DYNAMIC_TEXTURE_PROPERTY); // override num -> block map
		if (blockData == null || state == null)
			return base.getQuads(state, side, rand, extraData, renderType);
		if (side == null)
		{
			var stream = base.getQuads(state, side, rand, extraData, renderType).stream();
			for (int i = 0; i < blockData.length; i++)
			{
				if (blockData[i] == null || blockData[i] == Blocks.AIR)
					continue;
				if (!unculledCache.containsKey(blockData[i]) || unculledCache.get(blockData[i]).get(i) == null)
				{
					// we have not cached the baked quads for the block for this specific override
					var blockRender = Minecraft.getInstance().getBlockRenderer();
					var model = elementOverrides.get(i);
					var oldQuads = model.getQuads(state, side, rand, extraData, renderType);
					var newQuads = changeQuads(oldQuads, base.getParticleIcon(extraData), blockRender.getBlockModel(blockData[i].defaultBlockState()).getParticleIcon(extraData));
					if (!unculledCache.containsKey(blockData[i])) // we haven't even made the overrides map yet
					{
						var list = new ArrayList<List<BakedQuad>>(elementOverrides.size());
						for (int j = 0; j < elementOverrides.size(); j++)
							list.add(null);
						unculledCache.put(blockData[i], list);
					}
					unculledCache.get(blockData[i]).set(i, newQuads);
				}
				stream = Stream.concat(stream, unculledCache.get(blockData[i]).get(i).stream());
			}
			return stream.toList();
		}
		var stream = base.getQuads(state, side, rand, extraData, renderType).stream();
		for (int i = 0; i < blockData.length; i++)
		{
			if (blockData[i] == null || blockData[i] == Blocks.AIR)
				continue;
			var sidedCache = culledCache.get(side);
			if (!sidedCache.containsKey(blockData[i]) || sidedCache.get(blockData[i]).get(i) == null)
			{
				// we have not cached the baked quads for the block for this specific override
				var blockRender = Minecraft.getInstance().getBlockRenderer();
				var model = elementOverrides.get(i);
				var oldQuads = model.getQuads(state, side, rand, extraData, renderType);
				var newQuads = changeQuads(oldQuads, base.getParticleIcon(extraData), blockRender.getBlockModel(blockData[i].defaultBlockState()).getParticleIcon(extraData));
				if (!sidedCache.containsKey(blockData[i])) // we haven't even made the overrides map yet
				{
					var list = new ArrayList<List<BakedQuad>>(elementOverrides.size());
					for (int j = 0; j < elementOverrides.size(); j++)
						list.add(null);
					sidedCache.put(blockData[i], list);
				}
				sidedCache.get(blockData[i]).set(i, newQuads);
			}
			stream = Stream.concat(stream, sidedCache.get(blockData[i]).get(i).stream());
		}
		return stream.toList();
	}

	@Override
	public boolean useAmbientOcclusion()
	{
		return base.useAmbientOcclusion();
	}

	@Override
	public boolean isGui3d()
	{
		return base.isGui3d();
	}

	@Override
	public boolean usesBlockLight()
	{
		return base.usesBlockLight();
	}

	@Override
	public boolean isCustomRenderer()
	{
		return false;
	}

	@Override
	public TextureAtlasSprite getParticleIcon()
	{
		return base.getParticleIcon();
	}

	@Override
	public ItemOverrides getOverrides()
	{
		return base.getOverrides();
	}

	private List<BakedQuad> changeQuads(List<BakedQuad> original, TextureAtlasSprite oldTex, TextureAtlasSprite newTex)
	{
		var newList = new ArrayList<BakedQuad>(original.size());
		for (var bakedQuad : original)
		{
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
