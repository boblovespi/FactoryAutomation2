package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class BasketDryingRecipe extends SimpleRecipe<BasketDryingRecipe.Input, UnitData>
{
	private static final BuilderFactory<BasketDryingRecipe, UnitData, UnitData.Builder<BasketDryingRecipe>> BUILDER_FACTORY = new BuilderFactory<>("basket_drying",
			BasketDryingRecipe::new,
			UnitData.Builder::new);

	public static Builder<BasketDryingRecipe, UnitData, UnitData.Builder<BasketDryingRecipe>> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected BasketDryingRecipe(Ingredient input, ItemStack result, int progress, UnitData data)
	{
		super(input, result, progress, RecipeThings.BASKET_DRYING_TYPE.get(), RecipeThings.BASKET_DRYING_SERIALZIER.get());
	}

	@Override
	protected boolean matchExtra(BasketDryingRecipe.Input input, Level level)
	{
		return true;
	}

	@Override
	public UnitData getData()
	{
		return UnitData.INSTANCE;
	}

	public static class Input extends SimpleRecipe.Input
	{
		public Input(ItemStack stack)
		{
			super(stack);
		}
	}
}
