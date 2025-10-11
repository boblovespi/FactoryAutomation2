package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public class SteamOvenRecipe extends SimpleRecipe<SteamOvenRecipe.Input, SteamOvenRecipe.Data>
{
	private static final BuilderFactory<SteamOvenRecipe, Data, Data.Builder> BUILDER_FACTORY = new BuilderFactory<>("steam_oven", SteamOvenRecipe::new, Data.Builder::new);

	public static final MapCodec<Data> DATA_CODEC = MapCodec.unit(Data.INSTANCE);
	public static final StreamCodec<? super RegistryFriendlyByteBuf, Data> DATA_STREAM_CODEC = StreamCodec.unit(Data.INSTANCE);
	private final SteamOvenRecipe.Data data;

	public static Builder<SteamOvenRecipe, Data, Data.Builder> of(ItemStack stack)
	{
		return BUILDER_FACTORY.of(stack);
	}

	protected SteamOvenRecipe(Ingredient input, ItemStack result, int progress, Data data)
	{
		super(input, result, progress, RecipeThings.STEAM_OVEN_TYPE.get(), RecipeThings.STEAM_OVEN_SERIALZIER.get());
		this.data = data;
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
	{
		return true;
	}

	@Override
	public SteamOvenRecipe.Data getData()
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
		private static final SteamOvenRecipe.Data INSTANCE = new SteamOvenRecipe.Data();

		public static class Builder extends DataBuilder<SteamOvenRecipe, SteamOvenRecipe.Data, SteamOvenRecipe.Data.Builder>
		{
			protected Builder(SimpleRecipe.Builder<SteamOvenRecipe, SteamOvenRecipe.Data, SteamOvenRecipe.Data.Builder> builder)
			{
				super(builder);
			}

			@Override
			protected SteamOvenRecipe.Data build()
			{
				return INSTANCE;
			}
		}
	}
}
