package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.FactoryAutomation;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalIngredientHelper implements IIngredientHelper<MetalStack>
{
	@Override
	public IIngredientType<MetalStack> getIngredientType()
	{
		return FAJeiPlugin.METAL_INGREDIENT;
	}

	@Override
	public String getDisplayName(MetalStack ingredient)
	{
		return I18n.get("metal." + ingredient.metal().getName() + ".name");
	}

	@Override
	public String getUniqueId(MetalStack ingredient, UidContext context)
	{
		return ingredient.metal().getName();
	}

	@Override
	public ResourceLocation getResourceLocation(MetalStack ingredient)
	{
		return FactoryAutomation.name(ingredient.metal().getName());
	}

	@Override
	public MetalStack copyIngredient(MetalStack ingredient)
	{
		return ingredient;
	}

	@Override
	public String getErrorInfo(@Nullable MetalStack ingredient)
	{
		return ingredient != null ? "%s[%d u]".formatted(ingredient.metal().getName(), ingredient.quantity()) : "null";
	}

	@Override
	public boolean hasSubtypes(MetalStack ingredient)
	{
		return false;
	}

	@Override
	public long getAmount(MetalStack ingredient)
	{
		return ingredient.quantity();
	}

	@Override
	public MetalStack normalizeIngredient(MetalStack ingredient)
	{
		return new MetalStack(ingredient.metal(), 18);
	}

	@Override
	public MetalStack copyWithAmount(MetalStack ingredient, long amount)
	{
		return new MetalStack(ingredient.metal(), (int) amount);
	}

	@Override
	public Iterable<Integer> getColors(MetalStack ingredient)
	{
		return List.of(ingredient.metal().color());
	}
}
