package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WorkbenchRecipe implements Recipe<WorkbenchRecipeInput>, IProgressRecipe
{
	public static final int MAX_SIZE = 5;
	private final int width;
	private final int height;
	private final RecipePatternWrapper recipe;

	private final Map<ResourceLocation, Usage> parts;
	private final Map<ResourceLocation, Usage> tools;
	private final ItemStack result;

	public WorkbenchRecipe(RecipePatternWrapper recipe, Map<ResourceLocation, Usage> parts, Map<ResourceLocation, Usage> tools, ItemStack result)
	{
		width = recipe.real[0].length;
		height = recipe.real.length;
		this.recipe = recipe;
		this.parts = parts;
		this.tools = tools;
		this.result = result;
	}

	@Override
	public boolean matches(WorkbenchRecipeInput input, Level level)
	{
		// check input width/height are ok
		if (input.width() < width || input.height() < height)
			return false;
		// check recipe grid is ok
		for (int y = 0; y < input.height(); y++)
		{
			for (int x = 0; x < input.width(); x++)
			{
				if (y < height && x < width)
				{
					if (!recipe.real[y][x].test(input.getItem(x, y)))
						return false;
				}
				else if (!input.getItem(x, y).isEmpty())
					return false;
			}
		}
		// check parts are ok
		for (var part : parts.entrySet())
		{
			var present = false;
			for (ItemStack stack : input.parts())
			{
				var data = stack.getItemHolder().getData(Workbench.PART_DATA);
				if (data != null && data.name().equals(part.getKey()) && data.tier() >= part.getValue().tier)
				{
					present = true;
					break;
				}
			}
			if (!present)
				return false;
		}
		// check tools are ok
		for (var tool : tools.entrySet())
		{
			var present = false;
			for (ItemStack stack : input.tools())
			{
				var data = stack.getItemHolder().getData(Workbench.TOOL_DATA);
				if (data != null && data.name().equals(tool.getKey()) && data.tier() >= tool.getValue().tier)
				{
					present = true;
					break;
				}
			}
			if (!present)
				return false;
		}

		return true;
	}

	@Override
	public ItemStack assemble(WorkbenchRecipeInput pInput, HolderLookup.Provider pRegistries)
	{
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return pWidth >= width && pHeight >= height;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries)
	{
		return result;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeThings.WORKBENCH_RECIPE_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeThings.WORKBENCH_RECIPE_TYPE.get();
	}

	@Override
	public int getProgress()
	{
		return 0;
	}

	public Map<ResourceLocation, Usage> getParts()
	{
		return parts;
	}

	public Map<ResourceLocation, Usage> getTools()
	{
		return tools;
	}

	public static class Serializer implements RecipeSerializer<WorkbenchRecipe>
	{
		public static final MapCodec<WorkbenchRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(RecipePatternWrapper.CODEC.forGetter(r -> r.recipe),
				Codec.unboundedMap(ResourceLocation.CODEC, Usage.CODEC).optionalFieldOf("parts", Map.of()).forGetter(r -> r.parts),
				Codec.unboundedMap(ResourceLocation.CODEC, Usage.CODEC).optionalFieldOf("tools", Map.of()).forGetter(r -> r.tools),
				ItemStack.CODEC.fieldOf("result").forGetter(r -> r.result)).apply(i, WorkbenchRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> STREAM_CODEC = StreamCodec.composite(RecipePatternWrapper.STREAM_CODEC, r -> r.recipe,
				ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, Usage.STREAM_CODEC, MAX_SIZE), r -> r.parts,
				ByteBufCodecs.map(HashMap::new, ResourceLocation.STREAM_CODEC, Usage.STREAM_CODEC, MAX_SIZE), r -> r.tools, ItemStack.STREAM_CODEC, r -> r.result,
				WorkbenchRecipe::new);

		@Override
		public MapCodec<WorkbenchRecipe> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, WorkbenchRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}

	public record RecipePatternWrapper(Ingredient[][] real, @Nullable RecipePatternData data)
	{
		public static final MapCodec<RecipePatternWrapper> CODEC = RecipePatternData.MAP_CODEC.flatXmap(RecipePatternWrapper::unwrap, RecipePatternWrapper::wrap);
		public static final StreamCodec<RegistryFriendlyByteBuf, RecipePatternWrapper> STREAM_CODEC = StreamCodec.of(RecipePatternWrapper::stream, RecipePatternWrapper::unstream);

		public static RecipePatternWrapper of(Map<Character, Ingredient> key, List<String> pattern)
		{
			return unwrap(new RecipePatternData(key, pattern)).getOrThrow();
		}

		private static DataResult<RecipePatternWrapper> unwrap(RecipePatternData wrapper)
		{
			var width = wrapper.pattern.getFirst().length();
			var height = wrapper.pattern.size();
			var real = new Ingredient[height][width];
			for (int y = 0; y < wrapper.pattern.size(); y++)
			{
				for (int x = 0; x < wrapper.pattern.get(y).length(); x++)
				{
					var c = wrapper.pattern.get(y).charAt(x);
					var ingredient = c == ' ' ? Ingredient.EMPTY : wrapper.key.get(c);
					if (ingredient == null)
						return DataResult.error(() -> "Pattern references symbol '" + c + "' but it's not defined in the key");
					real[y][x] = ingredient;
				}
			}
			return DataResult.success(new RecipePatternWrapper(real, wrapper));
		}

		private static DataResult<? extends RecipePatternData> wrap(RecipePatternWrapper wrapper)
		{
			if (wrapper.data != null)
				return DataResult.success(wrapper.data);
			return DataResult.error(() -> "Cannot encode unpacked recipe");
		}

		private static void stream(RegistryFriendlyByteBuf buffer, RecipePatternWrapper wrapper)
		{
			buffer.writeByte(wrapper.real[0].length);
			buffer.writeByte(wrapper.real.length);
			for (var width : wrapper.real)
				for (var ing : width)
					Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ing);
		}

		private static RecipePatternWrapper unstream(RegistryFriendlyByteBuf buffer)
		{
			var width = buffer.readByte();
			var height = buffer.readByte();
			var real = new Ingredient[height][width];
			for (int y = 0; y < height; y++)
			{
				for (int x = 0; x < width; x++)
				{
					real[y][x] = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
				}
			}
			return new RecipePatternWrapper(real, null);
		}
	}

	public record RecipePatternData(Map<Character, Ingredient> key, List<String> pattern)
	{
		private static final Codec<List<String>> PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
			if (strings.size() > MAX_SIZE)
				return DataResult.error(() -> "Invalid pattern: too many rows, %s is maximum".formatted(MAX_SIZE));
			else if (strings.isEmpty())
				return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
			else
			{
				var i = strings.getFirst().length();
				for (String s : strings)
				{
					if (s.length() > MAX_SIZE)
						return DataResult.error(() -> "Invalid pattern: too many columns, %s is maximum".formatted(MAX_SIZE));
					if (i != s.length())
						return DataResult.error(() -> "Invalid pattern: each row must be the same width");
				}

				return DataResult.success(strings);
			}
		}, Function.identity());
		private static final Codec<Character> SYMBOL_CODEC = Codec.STRING.comapFlatMap(char_ -> {
			if (char_.length() != 1)
				return DataResult.error(() -> "Invalid key entry: '" + char_ + "' is an invalid symbol (must be 1 character only).");
			else
				return " ".equals(char_) ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.") : DataResult.success(char_.charAt(0));
		}, String::valueOf);
		public static final MapCodec<RecipePatternData> MAP_CODEC = RecordCodecBuilder.mapCodec(
				data -> data.group(ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(p_312509_ -> p_312509_.key),
						PATTERN_CODEC.fieldOf("pattern").forGetter(p_312713_ -> p_312713_.pattern)).apply(data, RecipePatternData::new));
	}

	public record Usage(int tier, int usage)
	{
		public static final Codec<Usage> CODEC = RecordCodecBuilder.create(
				i -> i.group(Codec.INT.fieldOf("tier").forGetter(Usage::tier), Codec.INT.fieldOf("usage").forGetter(Usage::usage)).apply(i, Usage::new));
		public static final StreamCodec<ByteBuf, Usage> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, Usage::tier, ByteBufCodecs.VAR_INT, Usage::usage, Usage::new);
	}
}
