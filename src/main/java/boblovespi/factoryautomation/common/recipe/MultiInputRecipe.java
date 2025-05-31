package boblovespi.factoryautomation.common.recipe;

import com.mojang.datafixers.util.Pair;
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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public abstract class MultiInputRecipe<T extends MultiInputRecipe.Input, U> implements Recipe<T>, IProgressRecipe
{
	protected final List<Ingredient> inputs;
	protected final ItemStack result;
	protected final int progress;
	private final RecipeSerializer<?> serializer;
	private final RecipeType<?> type;

	protected MultiInputRecipe(List<Ingredient> inputs, ItemStack result, int progress, RecipeType<?> type, RecipeSerializer<?> serializer)
	{
		this.inputs = inputs;
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
	public boolean matches(T input, Level level)
	{
		return inputs.stream().allMatch(ing -> input.stack().stream().anyMatch(ing)) && inputs.size() == input.stack().stream().filter(i -> !i.isEmpty()).count() &&
			   matchExtra(input, level);
	}

	protected abstract boolean matchExtra(T input, Level level);

	public abstract U getData();

	@Override
	public int getProgress()
	{
		return progress;
	}

	public List<Ingredient> getInputs()
	{
		return inputs;
	}

	public static class Input implements RecipeInput
	{
		private final List<ItemStack> stacks;

		public Input(List<ItemStack> stacks)
		{
			this.stacks = stacks;
		}

		@Override
		public ItemStack getItem(int pIndex)
		{
			return stacks.get(pIndex);
		}

		@Override
		public int size()
		{
			return stacks.size();
		}

		public List<ItemStack> stack()
		{
			return stacks;
		}

		@Override
		public String toString()
		{
			return "Input<" + getClass().getSimpleName() + ">[" + "stacks=" + stacks + ']';
		}
	}

	private record MultiInputRecipeData(List<Ingredient> inputs, ItemStack result, int progress)
	{

	}

	@FunctionalInterface
	public interface Factory<T extends MultiInputRecipe<?, U>, U>
	{
		T create(List<Ingredient> input, ItemStack result, int progress, U extraData);
	}

	public static class Serializer<T extends MultiInputRecipe<?, U>, U> implements RecipeSerializer<T>
	{
		private static final MapCodec<MultiInputRecipeData> BASE_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Ingredient.CODEC_NONEMPTY.listOf().fieldOf("inputs").forGetter(MultiInputRecipeData::inputs),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(MultiInputRecipeData::result),
				Codec.INT.fieldOf("progress").forGetter(MultiInputRecipeData::progress)).apply(i, MultiInputRecipeData::new));
		private final MapCodec<T> codec;
		private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

		public Serializer(Factory<T, U> con, MapCodec<U> dataCodec, StreamCodec<? super RegistryFriendlyByteBuf, U> dataStream)
		{
			codec = Codec.mapPair(BASE_CODEC, dataCodec).xmap(
					p -> con.create(p.getFirst().inputs(), p.getFirst().result(), p.getFirst().progress(), p.getSecond()),
					r -> new Pair<>(new MultiInputRecipeData(r.inputs, r.result, r.progress), r.getData()));
			streamCodec = StreamCodec.composite(Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), r -> r.inputs,
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

	public static class Builder<T extends MultiInputRecipe<?, U>, U, V extends DataBuilder<T, U, V>> implements RecipeBuilder
	{
		private final String category;
		private final Factory<T, U> factory;
		private final Function<Builder<T, U, V>, V> dataBuilderFactory;
		private final ItemStack result;
		private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
		private List<Ingredient> input;
		private U data;
		private int progress;

		public Builder(String category, Factory<T, U> factory, Function<Builder<T, U, V>, V> dataBuilderFactory, ItemStack result)
		{
			this.category = category;
			this.factory = factory;
			this.dataBuilderFactory = dataBuilderFactory;
			this.result = result;
			input = new ArrayList<>();
		}

		@Override
		public Builder<T, U, V> unlockedBy(String pName, Criterion<?> pCriterion)
		{
			criteria.put(pName, pCriterion);
			return this;
		}

		@Override
		public Builder<T, U, V> group(@Nullable String pGroupName)
		{
			return this;
		}

		@Override
		public Item getResult()
		{
			return result.getItem();
		}

		public Builder<T, U, V> input(Ingredient input)
		{
			this.input.add(input);
			return this;
		}

		public Builder<T, U, V> input(List<Ingredient> input)
		{
			this.input = input;
			return this;
		}

		public Builder<T, U, V> input(ItemLike input)
		{
			return input(Ingredient.of(input));
		}

		public Builder<T, U, V> input(TagKey<Item> input)
		{
			return input(Ingredient.of(input));
		}

		public Builder<T, U, V> progress(int progress)
		{
			this.progress = progress;
			return this;
		}

		public Builder<T, U, V> setData(U data)
		{
			this.data = data;
			return this;
		}

		public V beginData()
		{
			return dataBuilderFactory.apply(this);
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
			var recipe = factory.create(List.copyOf(input), result, progress, data);
			output.accept(location, recipe, advancementBuilder.build(location.withPrefix("recipes/")));
		}

		@Override
		public void save(RecipeOutput output)
		{
			save(output, BuiltInRegistries.ITEM.getKey(result.getItem()).withPrefix(category + "/"));
		}

		public void saveNoteFrom(RecipeOutput output, String from)
		{
			save(output, BuiltInRegistries.ITEM.getKey(result.getItem()).withPrefix(category + "/").withSuffix("_from_" + from));
		}
	}

	public static abstract class DataBuilder<T extends MultiInputRecipe<?, U>, U, V extends DataBuilder<T, U, V>>
	{
		private final Builder<T, U, V> builder;

		protected DataBuilder(Builder<T, U, V> builder)
		{
			this.builder = builder;
		}

		protected abstract U build();

		public Builder<T, U, V> endData()
		{
			builder.setData(build());
			return builder;
		}
	}

	protected static class BuilderFactory<T extends MultiInputRecipe<?, U>, U, V extends DataBuilder<T, U, V>>
	{
		private final String category;
		private final Factory<T, U> factory;
		private final Function<Builder<T, U, V>, V> dataBuilderFactory;

		protected BuilderFactory(String category, Factory<T, U> factory, Function<Builder<T, U, V>, V> dataBuilderFactory)
		{
			this.category = category;
			this.factory = factory;
			this.dataBuilderFactory = dataBuilderFactory;
		}

		public Builder<T, U, V> of(ItemStack stack)
		{
			return new Builder<>(category, factory, dataBuilderFactory, stack);
		}
	}
}
