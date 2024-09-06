package boblovespi.factoryautomation.common.recipe;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public abstract class SimpleRecipe<T extends SimpleRecipe.Input, U> implements Recipe<T>, IProgressRecipe
{
	protected final Ingredient input;
	protected final ItemStack result;
	protected final int progress;
	private final RecipeSerializer<?> serializer;
	private final RecipeType<?> type;

	protected SimpleRecipe(Ingredient input, ItemStack result, int progress, RecipeType<?> type, RecipeSerializer<?> serializer)
	{
		this.input = input;
		this.result = result;
		this.serializer = serializer;
		this.type = type;
		this.progress = progress;
	}

	@Override
	public RecipeType<?> getType()
	{
		return type;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return serializer;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries)
	{
		return result;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return pWidth * pHeight > 0;
	}

	@Override
	public ItemStack assemble(T pInput, HolderLookup.Provider pRegistries)
	{
		return result.copy();
	}

	@Override
	public boolean matches(T pInput, Level pLevel)
	{
		return input.test(pInput.stack()) && matchExtra(pInput, pLevel);
	}

	protected abstract boolean matchExtra(T input, Level level);

	protected abstract U getData();

	@Override
	public int getProgress()
	{
		return progress;
	}

	public static class Input implements RecipeInput
	{
		private final ItemStack stack;

		public Input(ItemStack stack)
		{
			this.stack = stack;
		}

		@Override
		public ItemStack getItem(int pIndex)
		{
			return pIndex == 0 ? stack : ItemStack.EMPTY;
		}

		@Override
		public int size()
		{
			return 1;
		}

		public ItemStack stack()
		{
			return stack;
		}

		@Override
		public String toString()
		{
			return "Input<" + getClass().getSimpleName() + ">[" + "stack=" + stack + ']';
		}

	}

	private record SimpleRecipeData(Ingredient input, ItemStack result, int progress)
	{

	}

	@FunctionalInterface
	public interface Factory<T extends SimpleRecipe<?, U>, U>
	{
		T create(Ingredient input, ItemStack result, int progress, U extraData);
	}

	public static class Serializer<T extends SimpleRecipe<?, U>, U> implements RecipeSerializer<T>
	{
		private static final MapCodec<SimpleRecipeData> BASE_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(SimpleRecipeData::input),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(SimpleRecipeData::result),
				Codec.INT.fieldOf("progress").forGetter(SimpleRecipeData::progress)).apply(i, SimpleRecipeData::new));
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

		public Serializer(Factory<T, U> con, MapCodec<U> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, U> dataStream)
		{
			codec = Codec.mapPair(BASE_CODEC, dataCodec).xmap(
					p -> con.create(p.getFirst().input(), p.getFirst().result(), p.getFirst().progress(), p.getSecond()),
					r -> new Pair<>(new SimpleRecipeData(r.input, r.result, r.progress), r.getData()));
			streamCodec = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
					ItemStack.STREAM_CODEC, r -> r.result,
					ByteBufCodecs.VAR_INT, r -> r.progress,
					dataStream, r -> r.getData(), con::create);
		}

		@Override
		public MapCodec<T> codec()
		{
			return codec;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec()
		{
			return streamCodec;
		}
	}
}
