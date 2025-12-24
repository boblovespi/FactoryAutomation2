package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SteamOvenRecipe extends SimpleRecipe<SteamOvenRecipe.Input, UnitData>
{
	private static final BuilderFactory<SteamOvenRecipe, UnitData, UnitData.Builder<SteamOvenRecipe>> BUILDER_FACTORY = new BuilderFactory<>("steam_oven", SteamOvenRecipe::new,
			UnitData.Builder::new);

	public static Builder<SteamOvenRecipe, UnitData, UnitData.Builder<SteamOvenRecipe>> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected SteamOvenRecipe(Ingredient input, ItemStack result, int progress, UnitData data)
	{
		super(input, result, progress, RecipeThings.STEAM_OVEN_TYPE.get(), RecipeThings.STEAM_OVEN_SERIALZIER.get());
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
