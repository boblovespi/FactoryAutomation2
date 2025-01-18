package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.Metal;
import net.minecraft.world.item.crafting.Ingredient;

public record MeltingJeiRecipe(Ingredient stack, float temp, Metal metal, Form form, int tier)
{
}
