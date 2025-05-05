package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public record TumblingBarrelRecipe(Ingredient input,
								   OptionalSizedFluidIngredient fluidInput,
								   int time,
								   ItemStack result,
								   FluidStack fluidResult,
								   float minSpeed,
								   float maxSpeed) implements Recipe<TumblingBarrelRecipe.Input>, IProgressRecipe
{
	public static final MapCodec<TumblingBarrelRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			Ingredient.CODEC.fieldOf("input").forGetter(TumblingBarrelRecipe::input),
			OptionalSizedFluidIngredient.CODEC.fieldOf("fluid_input").forGetter(TumblingBarrelRecipe::fluidInput),
			Codec.INT.fieldOf("time").forGetter(TumblingBarrelRecipe::time),
			ItemStack.CODEC.fieldOf("result").forGetter(TumblingBarrelRecipe::result),
			FluidStack.OPTIONAL_CODEC.fieldOf("fluid_result").forGetter(TumblingBarrelRecipe::fluidResult),
			Codec.FLOAT.fieldOf("min_speed").forGetter(TumblingBarrelRecipe::minSpeed),
			Codec.FLOAT.fieldOf("max_speed").forGetter(TumblingBarrelRecipe::maxSpeed)).apply(i, TumblingBarrelRecipe::new));
	public static final StreamCodec<RegistryFriendlyByteBuf, TumblingBarrelRecipe> STREAM_CODEC = composite(
			Ingredient.CONTENTS_STREAM_CODEC, TumblingBarrelRecipe::input,
			OptionalSizedFluidIngredient.STREAM_CODEC, TumblingBarrelRecipe::fluidInput,
			ByteBufCodecs.VAR_INT, TumblingBarrelRecipe::time,
			ItemStack.OPTIONAL_STREAM_CODEC, TumblingBarrelRecipe::result,
			FluidStack.OPTIONAL_STREAM_CODEC, TumblingBarrelRecipe::fluidResult,
			ByteBufCodecs.FLOAT, TumblingBarrelRecipe::minSpeed,
			ByteBufCodecs.FLOAT, TumblingBarrelRecipe::maxSpeed,
			TumblingBarrelRecipe::new);

	public static Builder of(ItemStack stack)
	{
		return new Builder(stack);
	}

	public static Builder of(ItemLike item)
	{
		return of(new ItemStack(item, 1));
	}

	public static Builder of(FluidStack stack)
	{
		return new Builder(stack);
	}

	public static Builder of(Fluid fluid, int amount)
	{
		return of(new FluidStack(fluid, amount));
	}

	public static Builder of(ItemLike item, Fluid fluid, int amount)
	{
		return new Builder(new ItemStack(item), new FluidStack(fluid, amount));
	}

	@Override
	public int getProgress()
	{
		return time;
	}

	@Override
	public boolean matches(Input input, Level level)
	{
		return this.input.test(input.stack()) && switch (fluidInput)
		{
			case OptionalSizedFluidIngredient.Present p -> p.value().test(input.fluid);
			case OptionalSizedFluidIngredient.Empty e -> input.fluid.isEmpty();
		} && input.speed >= minSpeed && input.speed <= maxSpeed;
	}

	@Override
	public ItemStack assemble(Input input, HolderLookup.Provider registries)
	{
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int width, int height)
	{
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider registries)
	{
		return result;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeThings.TUMBLING_BARREL_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeThings.TUMBLING_BARREL_TYPE.get();
	}

	public static class Input extends SimpleRecipe.Input
	{
		private final FluidStack fluid;
		private final float speed;

		public Input(ItemStack stack, FluidStack fluid, float speed)
		{
			super(stack);
			this.fluid = fluid;
			this.speed = speed;
		}
	}

	public static class Builder implements RecipeBuilder
	{
		private OptionalSizedFluidIngredient fluid = OptionalSizedFluidIngredient.EMPTY;
		private final ItemStack result;
		private final FluidStack fluidResult;
		private float minSpeed;
		private float maxSpeed;
		private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
		private Ingredient input;
		private int time;

		private Builder(ItemStack result)
		{
			this.result = result;
			fluidResult = FluidStack.EMPTY;
		}

		private Builder(FluidStack fluidResult)
		{
			this.fluidResult = fluidResult;
			result = ItemStack.EMPTY;
		}

		public Builder(ItemStack itemStack, FluidStack fluidStack)
		{
			result = itemStack;
			fluidResult = fluidStack;
		}

		public Builder minSpeed(float minSpeed)
		{
			this.minSpeed = minSpeed;
			return this;
		}

		public Builder maxSpeed(float maxSpeed)
		{
			this.maxSpeed = maxSpeed;
			return this;
		}

		public Builder fluidInput(SizedFluidIngredient fluid)
		{
			this.fluid = new OptionalSizedFluidIngredient.Present(fluid);
			return this;
		}

		public Builder fluidInput(Fluid fluid, int amount)
		{
			return fluidInput(SizedFluidIngredient.of(fluid, amount));
		}

		public Builder time(int time)
		{
			this.time = time;
			return this;
		}

		public Builder input(Ingredient input)
		{
			this.input = input;
			return this;
		}

		public Builder input(ItemLike input)
		{
			return input(Ingredient.of(input));
		}

		public Builder input(TagKey<Item> input)
		{
			return input(Ingredient.of(input));
		}

		@Override
		public Builder unlockedBy(String name, Criterion<?> criterion)
		{
			criteria.put(name, criterion);
			return this;
		}

		@Override
		public Builder group(@Nullable String groupName)
		{
			return this;
		}

		@Override
		public Item getResult()
		{
			return result.getItem();
		}

		@Override
		public void save(RecipeOutput output, ResourceLocation location)
		{
			if (criteria.isEmpty())
				throw new IllegalStateException("No way of obtaining recipe " + location);
			var advancementBuilder = output.advancement()
										   .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(location))
										   .rewards(AdvancementRewards.Builder.recipe(location))
										   .requirements(AdvancementRequirements.Strategy.OR);
			criteria.forEach(advancementBuilder::addCriterion);
			var recipe = new TumblingBarrelRecipe(input, fluid, time, result, fluidResult, minSpeed, maxSpeed);
			output.accept(location, recipe, advancementBuilder.build(location.withPrefix("recipes/")));
		}

		@Override
		public void save(RecipeOutput output)
		{
			if (result.isEmpty())
				save(output, BuiltInRegistries.FLUID.getKey(fluidResult.getFluid()).withPrefix("tumbling_barrel/"));
			else
				save(output, BuiltInRegistries.ITEM.getKey(result.getItem()).withPrefix("tumbling_barrel/"));
		}
	}

	static <B, C, T1, T2, T3, T4, T5, T6, T7> StreamCodec<B, C> composite(
			final StreamCodec<? super B, T1> codec1,
			final Function<C, T1> getter1,
			final StreamCodec<? super B, T2> codec2,
			final Function<C, T2> getter2,
			final StreamCodec<? super B, T3> codec3,
			final Function<C, T3> getter3,
			final StreamCodec<? super B, T4> codec4,
			final Function<C, T4> getter4,
			final StreamCodec<? super B, T5> codec5,
			final Function<C, T5> getter5,
			final StreamCodec<? super B, T6> codec6,
			final Function<C, T6> getter6,
			final StreamCodec<? super B, T7> codec7,
			final Function<C, T7> getter7,
			final Function7<T1, T2, T3, T4, T5, T6, T7, C> factory
																		 )
	{
		return new StreamCodec<B, C>()
		{
			@Override
			public C decode(B p_330310_)
			{
				T1 t1 = codec1.decode(p_330310_);
				T2 t2 = codec2.decode(p_330310_);
				T3 t3 = codec3.decode(p_330310_);
				T4 t4 = codec4.decode(p_330310_);
				T5 t5 = codec5.decode(p_330310_);
				T6 t6 = codec6.decode(p_330310_);
				T7 t7 = codec7.decode(p_330310_);
				return factory.apply(t1, t2, t3, t4, t5, t6, t7);
			}

			@Override
			public void encode(B p_332052_, C p_331912_)
			{
				codec1.encode(p_332052_, getter1.apply(p_331912_));
				codec2.encode(p_332052_, getter2.apply(p_331912_));
				codec3.encode(p_332052_, getter3.apply(p_331912_));
				codec4.encode(p_332052_, getter4.apply(p_331912_));
				codec5.encode(p_332052_, getter5.apply(p_331912_));
				codec6.encode(p_332052_, getter6.apply(p_331912_));
				codec7.encode(p_332052_, getter7.apply(p_331912_));
			}
		};
	}

	private interface Function7<T1, T2, T3, T4, T5, T6, T7, C>
	{
		C apply(T1 t1, T2 t2, T3 t3, T4 t4, T5 t5, T6 t6, T7 t7);
	}

	public static class Serializer implements RecipeSerializer<TumblingBarrelRecipe>
	{
		@Override
		public MapCodec<TumblingBarrelRecipe> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, TumblingBarrelRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}
}
