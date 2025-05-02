package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.common.util.Metal;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;

public class MetalIngredient implements IIngredientTypeWithSubtypes<Metal, MetalStack>
{
	public static Codec<MetalStack> METAL_STACK_CODEC = Codec.STRING.comapFlatMap(s -> {
		var metal = Metal.fromName(s);
		return metal == Metal.UNKNOWN ? DataResult.error(() -> "No metal with name " + s) : DataResult.success(new MetalStack(metal, 18));
	}, s -> s.metal().getName());

	@Override
	public Class<MetalStack> getIngredientClass()
	{
		return MetalStack.class;
	}

	@Override
	public Class<Metal> getIngredientBaseClass()
	{
		return Metal.class;
	}

	@Override
	public Metal getBase(MetalStack ingredient)
	{
		return ingredient.metal();
	}
}
