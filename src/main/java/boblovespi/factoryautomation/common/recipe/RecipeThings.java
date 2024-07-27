package boblovespi.factoryautomation.common.recipe;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class RecipeThings
{
	public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, FactoryAutomation.MODID);
	public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, FactoryAutomation.MODID);

	public static final Supplier<RecipeType<ChoppingBlockRecipe>> CHOPPING_BLOCK_TYPE = RECIPE_TYPES.register("chopping_block",
			() -> RecipeType.simple(FactoryAutomation.name("chopping_block")));
	public static final Supplier<RecipeSerializer<ChoppingBlockRecipe>> CHOPPING_BLOCK_SERIALIZER = RECIPE_SERIALIZERS.register("chopping_block",
			ChoppingBlockRecipe.makeSerializer());
}
