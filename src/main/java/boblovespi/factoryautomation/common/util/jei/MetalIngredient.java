package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.common.util.Metal;
import mezz.jei.api.ingredients.IIngredientTypeWithSubtypes;

public class MetalIngredient implements IIngredientTypeWithSubtypes<Metal, MetalStack>
{
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
