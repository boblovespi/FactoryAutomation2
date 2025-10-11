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
	public static final Supplier<RecipeType<RemovalRecipe>> REMOVAL_RECIPE_TYPE = RECIPE_TYPES.register("remove_recipe",
			() -> RecipeType.simple(FactoryAutomation.name("remove_recipe")));
	public static final Supplier<RecipeSerializer<RemovalRecipe>> REMOVAL_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("remove_recipe", () -> RemovalRecipe.Serializer.INSTANCE);
	public static final Supplier<RecipeType<WorkbenchRecipe>> WORKBENCH_RECIPE_TYPE = RECIPE_TYPES.register("workbench",
			() -> RecipeType.simple(FactoryAutomation.name("workbench")));
	public static final Supplier<RecipeSerializer<WorkbenchRecipe>> WORKBENCH_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("workbench", WorkbenchRecipe.Serializer::new);
	public static final Supplier<RecipeType<BrickDryingRecipe>> BRICK_DRYING_TYPE = RECIPE_TYPES.register("brick_drying",
			() -> RecipeType.simple(FactoryAutomation.name("brick_drying")));
	public static final Supplier<RecipeSerializer<BrickDryingRecipe>> BRICK_DRYING_SERIALIZER = RECIPE_SERIALIZERS.register("brick_drying", BrickDryingRecipe.Serializer::new);
	public static final Supplier<RecipeType<MillstoneRecipe>> MILLSTONE_TYPE = RECIPE_TYPES.register("millstone", () -> RecipeType.simple(FactoryAutomation.name("millstone")));
	public static final Supplier<RecipeSerializer<MillstoneRecipe>> MILLSTONE_SERIALIZER = RECIPE_SERIALIZERS.register("millstone",
			() -> new SimpleRecipe.Serializer<>(MillstoneRecipe::new, TorqueSpeedData.CODEC, TorqueSpeedData.STREAM_CODEC));
	public static final Supplier<RecipeType<LogPileFiringRecipe>> LOG_PILE_FIRING_TYPE = RECIPE_TYPES.register("log_pile_firing",
			() -> RecipeType.simple(FactoryAutomation.name("log_pile_firing")));
	public static final Supplier<RecipeSerializer<LogPileFiringRecipe>> LOG_PILE_FIRING_SERIALIZER = RECIPE_SERIALIZERS.register("log_pile_firing",
			() -> new SimpleRecipe.Serializer<>(LogPileFiringRecipe::new, LogPileFiringRecipe.DATA_CODEC, LogPileFiringRecipe.DATA_STREAM_CODEC));
	public static final Supplier<RecipeType<TripHammerRecipe>> TRIP_HAMMER_TYPE = RECIPE_TYPES.register("trip_hammer",
			() -> RecipeType.simple(FactoryAutomation.name("trip_hammer")));
	public static final Supplier<RecipeSerializer<TripHammerRecipe>> TRIP_HAMMER_SERIALIZER = RECIPE_SERIALIZERS.register("trip_hammer",
			() -> new SimpleRecipe.Serializer<>(TripHammerRecipe::new, TripHammerRecipe.DATA_CODEC, TripHammerRecipe.DATA_STREAM_CODEC));
	public static final Supplier<RecipeType<TumblingBarrelRecipe>> TUMBLING_BARREL_TYPE = RECIPE_TYPES.register("tumbling_barrel",
			() -> RecipeType.simple(FactoryAutomation.name("tumbling_barrel")));
	public static final Supplier<RecipeSerializer<TumblingBarrelRecipe>> TUMBLING_BARREL_SERIALIZER = RECIPE_SERIALIZERS.register("tumbling_barrel",
			TumblingBarrelRecipe.Serializer::new);
	public static final Supplier<RecipeType<FryingPanRecipe>> FRYING_PAN_TYPE = RECIPE_TYPES.register("frying", () -> RecipeType.simple(FactoryAutomation.name("frying")));
	public static final Supplier<RecipeSerializer<FryingPanRecipe>> FRYING_PAN_SERIALIZER = RECIPE_SERIALIZERS.register("frying",
			() -> new MultiInputRecipe.Serializer<>(FryingPanRecipe::new, FryingPanRecipe.DATA_CODEC, FryingPanRecipe.DATA_STREAM_CODEC));
	public static final Supplier<RecipeType<KilnRecipe>> KILN_TYPE = RECIPE_TYPES.register("kiln", () -> RecipeType.simple(FactoryAutomation.name("kiln")));
	public static final Supplier<RecipeSerializer<KilnRecipe>> KILN_SERIALIZER = RECIPE_SERIALIZERS.register("kiln",
			() -> new SimpleRecipe.Serializer<>(KilnRecipe::new, TemperatureData.CODEC, TemperatureData.STREAM_CODEC));
	public static final Supplier<RecipeType<SteamOvenRecipe>> STEAM_OVEN_TYPE = RECIPE_TYPES.register("steam_oven", () -> RecipeType.simple(FactoryAutomation.name("steam_oven")));
	public static final Supplier<RecipeSerializer<SteamOvenRecipe>> STEAM_OVEN_SERIALZIER = RECIPE_SERIALIZERS.register("steam_oven",
			() -> new SimpleRecipe.Serializer<>(SteamOvenRecipe::new, SteamOvenRecipe.DATA_CODEC, SteamOvenRecipe.DATA_STREAM_CODEC));
}
