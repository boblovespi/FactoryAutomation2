package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class StampMillRecipe extends SimpleRecipe<StampMillRecipe.Input, UnitData>
{
	private static final BuilderFactory<StampMillRecipe, UnitData, UnitData.Builder<StampMillRecipe>> BUILDER_FACTORY = new BuilderFactory<>("stamp_mill", StampMillRecipe::new,
			UnitData.Builder::new);

	public static Builder<StampMillRecipe, UnitData, UnitData.Builder<StampMillRecipe>> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected StampMillRecipe(Ingredient input, ItemStack result, int progress, UnitData data)
	{
		super(input, result, progress, RecipeThings.STAMP_MILL_TYPE.get(), RecipeThings.STAMP_MILL_SERIALIZER.get());
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
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
