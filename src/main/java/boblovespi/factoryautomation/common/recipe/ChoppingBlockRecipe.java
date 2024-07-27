package boblovespi.factoryautomation.common.recipe;

import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.function.Supplier;

public class ChoppingBlockRecipe extends SingleItemRecipe
{
	public ChoppingBlockRecipe(String pGroup, Ingredient pIngredient, ItemStack pResult)
	{
		super(RecipeThings.CHOPPING_BLOCK_TYPE.get(), RecipeThings.CHOPPING_BLOCK_SERIALIZER.get(), pGroup, pIngredient, pResult);
	}

	public static Supplier<? extends RecipeSerializer<ChoppingBlockRecipe>> makeSerializer()
	{
		return () -> new Serializer(ChoppingBlockRecipe::new);
	}

	@Override
	public boolean matches(SingleRecipeInput input, Level pLevel)
	{
		return ingredient.test(input.item());
	}

	public static SingleItemRecipeBuilder builder(RecipeCategory category, Ingredient ingredient, ItemLike result, int count)
	{
		return new SingleItemRecipeBuilder(category, ChoppingBlockRecipe::new, ingredient, result, count);
	}

	public static class Serializer extends SingleItemRecipe.Serializer<ChoppingBlockRecipe>
	{
		protected Serializer(Factory<ChoppingBlockRecipe> pFactory)
		{
			super(pFactory);
		}
	}
}
