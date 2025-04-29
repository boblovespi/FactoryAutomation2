package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class TripHammerRecipe extends SimpleRecipe<TripHammerRecipe.Input, TripHammerRecipe.Data>
{
	private static final BuilderFactory<TripHammerRecipe, Data, Data.Builder> BUILDER_FACTORY = new BuilderFactory<>("log_pile_firing", TripHammerRecipe::new,
			TripHammerRecipe.Data.Builder::new);

	public static final MapCodec<TripHammerRecipe.Data> DATA_CODEC = MapCodec.unit(Data.INSTANCE);
	public static final StreamCodec<? super RegistryFriendlyByteBuf, TripHammerRecipe.Data> DATA_STREAM_CODEC = StreamCodec.unit(Data.INSTANCE);
	private final Data data;

	public static Builder<TripHammerRecipe, Data, Data.Builder> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected TripHammerRecipe(Ingredient input, ItemStack result, int progress, Data data)
	{
		super(input, result, progress, RecipeThings.TRIP_HAMMER_TYPE.get(), RecipeThings.TRIP_HAMMER_SERIALIZER.get());
		this.data = data;
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

	public static class Input extends SimpleRecipe.Input
	{
		public Input(ItemStack stack)
		{
			super(stack);
		}
	}

	public record Data()
	{
		private static final Data INSTANCE = new Data();

		public static class Builder extends DataBuilder<TripHammerRecipe, Data, Builder>
		{
			protected Builder(SimpleRecipe.Builder<TripHammerRecipe, Data, Builder> builder)
			{
				super(builder);
			}

			@Override
			protected Data build()
			{
				return INSTANCE;
			}
		}
	}
}
