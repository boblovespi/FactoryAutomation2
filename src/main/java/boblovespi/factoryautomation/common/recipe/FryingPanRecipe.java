package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.List;

public class FryingPanRecipe extends MultiInputRecipe<FryingPanRecipe.Input, FryingPanRecipe.Data>
{
	private static final MultiInputRecipe.BuilderFactory<FryingPanRecipe, Data, Data.Builder> BUILDER_FACTORY = new FryingPanRecipe.BuilderFactory<>("frying", FryingPanRecipe::new,
			Data.Builder::new);
	public static final MapCodec<Data> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Ingredient.CODEC.fieldOf("plate").forGetter(Data::plate)).apply(i, Data::new));
	public static final StreamCodec<? super RegistryFriendlyByteBuf, Data> DATA_STREAM_CODEC = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, Data::plate, Data::new);

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
		return true;
	}

	@Override
	public Data getData()
	{
		return data;
	}

	public static class Input extends MultiInputRecipe.Input
	{
		public Input(List<ItemStack> stack)
		{
			super(stack);
		}
	}

	public record Data(Ingredient plate)
	{
		public static class Builder extends MultiInputRecipe.DataBuilder<FryingPanRecipe, FryingPanRecipe.Data, FryingPanRecipe.Data.Builder>
		{
			private Ingredient plate = Ingredient.EMPTY;

			protected Builder(MultiInputRecipe.Builder<FryingPanRecipe, Data, Builder> builder)
			{
				super(builder);
			}

			public Builder plate(Ingredient plate)
			{
				this.plate = plate;
				return this;
			}

			@Override
			protected Data build()
			{
				return new Data(plate);
			}
		}
	}
}
