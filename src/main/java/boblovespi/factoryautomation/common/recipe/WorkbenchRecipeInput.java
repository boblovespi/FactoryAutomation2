package boblovespi.factoryautomation.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record WorkbenchRecipeInput(int width, int height, List<ItemStack> items, List<ItemStack> parts, List<ItemStack> tools) implements RecipeInput
{
	@Override
	public ItemStack getItem(int pIndex)
	{
		return items.get(pIndex);
	}

	@Override
	public int size()
	{
		return items.size();
	}

	public ItemStack getItem(int x, int y)
	{
		return getItem(y + height * x);
	}
}
