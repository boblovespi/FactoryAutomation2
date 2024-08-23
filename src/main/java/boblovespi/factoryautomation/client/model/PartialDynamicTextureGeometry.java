package boblovespi.factoryautomation.client.model;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.ElementsModel;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;

import java.util.ArrayList;
import java.util.function.Function;

public class PartialDynamicTextureGeometry implements IUnbakedGeometry<PartialDynamicTextureGeometry>
{
	private final BlockModel base;
	private final int[] elementOverrides; // element num -> override num map
	private final int max;

	public PartialDynamicTextureGeometry(BlockModel base, int[] elementOverrides)
	{
		this.base = base;
		this.elementOverrides = elementOverrides;
		var max = 0;
		for (int override : elementOverrides)
		{
			max = Math.max(max, override + 1);
		}
		this.max = max;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides)
	{
		var allElements = base.getElements();
		var baseElements = new ArrayList<BlockElement>(allElements.size());
		var overrideElements = new ArrayList[max]; // override num -> elements map
		for (int i = 0; i < allElements.size(); i++)
		{
			if (elementOverrides[i] == -1)
				baseElements.add(allElements.get(i));
			else
			{
				if (overrideElements[elementOverrides[i]] == null)
					overrideElements[elementOverrides[i]] = new ArrayList<BlockElement>();
				//noinspection unchecked
				overrideElements[elementOverrides[i]].add(allElements.get(i));
			}
		}
		var bakedBase = new ElementsModel(baseElements).bake(context, baker, spriteGetter, modelState, overrides);
		var bakedOverrides = new ArrayList<BakedModel>(max);
		for (var list : overrideElements)
			bakedOverrides.add(new ElementsModel(list).bake(context, baker, spriteGetter, modelState, overrides));
		return new PartialDynamicTextureModel(bakedBase, bakedOverrides);
	}

	@Override
	public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context)
	{
		base.resolveParents(modelGetter);
	}
}
