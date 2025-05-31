package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;

public class FryingPanRecipe extends MultiInputRecipe<FryingPanRecipe.Input, FryingPanRecipe.Data>
{
	private static final MultiInputRecipe.BuilderFactory<FryingPanRecipe, Data, Data.Builder> BUILDER_FACTORY = new FryingPanRecipe.BuilderFactory<>("frying", FryingPanRecipe::new,
			Data.Builder::new);
	public static final MapCodec<Data> DATA_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(FluidIngredient.CODEC.fieldOf("liquid").forGetter(Data::liquid), Ingredient.CODEC.fieldOf("plate").forGetter(Data::plate)).apply(i, Data::new));
	public static final StreamCodec<? super RegistryFriendlyByteBuf, Data> DATA_STREAM_CODEC = StreamCodec.composite(FluidIngredient.STREAM_CODEC, Data::liquid,
			Ingredient.CONTENTS_STREAM_CODEC, Data::plate, Data::new);

	private final Data data;

	protected FryingPanRecipe(List<Ingredient> inputs, ItemStack result, int progress, Data data)
	{
		super(inputs, result, progress, RecipeThings.FRYING_PAN_TYPE.get(), RecipeThings.FRYING_PAN_SERIALIZER.get());
		this.data = data;
	}

	public static MultiInputRecipe.Builder<FryingPanRecipe, FryingPanRecipe.Data, FryingPanRecipe.Data.Builder> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
	{
		return data.liquid.test(input.liquid());
	}

	@Override
	public Data getData()
	{
		return data;
	}

	public static class Input extends MultiInputRecipe.Input
	{
		private final FluidStack liquid;

		public Input(List<ItemStack> stack, FluidStack liquid)
		{
			super(stack);
			this.liquid = liquid;
		}

		public FluidStack liquid()
		{
			return liquid;
		}

		@Override
		public boolean isEmpty()
		{
			return super.isEmpty() && liquid.isEmpty();
		}
	}

	public record Data(FluidIngredient liquid, Ingredient plate)
	{
		public static class Builder extends MultiInputRecipe.DataBuilder<FryingPanRecipe, FryingPanRecipe.Data, FryingPanRecipe.Data.Builder>
		{
			private Ingredient plate = Ingredient.EMPTY;
			private FluidIngredient liquid = FluidIngredient.empty();

			protected Builder(MultiInputRecipe.Builder<FryingPanRecipe, Data, Builder> builder)
			{
				super(builder);
			}

			public Builder plate(Ingredient plate)
			{
				this.plate = plate;
				return this;
			}

			public Builder liquid(FluidIngredient liquid)
			{
				this.liquid = liquid;
				return this;
			}

			@Override
			protected Data build()
			{
				return new Data(liquid, plate);
			}
		}
	}
}
