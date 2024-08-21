package boblovespi.factoryautomation.common.util.jei;

import boblovespi.factoryautomation.common.util.Form;
import net.minecraft.world.item.ItemStack;

public record CastingJeiRecipe(Form form, CasterType type, float efficiency, ItemStack cast)
{
}
