package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class KilnRecipe extends SimpleRecipe<KilnRecipe.Input, TemperatureData>
{
	private static final BuilderFactory<KilnRecipe, TemperatureData, TemperatureData.Builder<KilnRecipe>> BUILDER_FACTORY = new BuilderFactory<>("kiln",
			KilnRecipe::new, TemperatureData.Builder::new);
	private final TemperatureData data;

	public static Builder<KilnRecipe, TemperatureData, TemperatureData.Builder<KilnRecipe>> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected KilnRecipe(Ingredient input, ItemStack result, int progress, TemperatureData data)
	{
		super(input, result, progress, RecipeThings.KILN_TYPE.get(), RecipeThings.KILN_SERIALIZER.get());
		this.data = data;
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
	{
		return input.temperature >= data.temperature();
	}

	@Override
	public TemperatureData getData()
	{
		return data;
	}

	public static class Input extends SimpleRecipe.Input
	{
		private final float temperature;

		public Input(ItemStack input, float temperature)
		{
			super(input);
			this.temperature = temperature;
		}
	}
}
