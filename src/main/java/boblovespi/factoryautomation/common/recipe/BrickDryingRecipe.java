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
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class BrickDryingRecipe implements Recipe<BrickDryingRecipe.Input>, IProgressRecipe
{
	private final Ingredient input;
	private final ItemStack result;
	private final Block inBlock;
	private final Block outBlock;
	private final int time;

	public BrickDryingRecipe(Ingredient input, ItemStack result, int time, Block inBlock, Block outBlock)
	{
		this.input = input;
		this.result = result;
		this.inBlock = inBlock;
		this.outBlock = outBlock;
		this.time = time;
	}

	public static Builder of(ItemLike item)
	{
		return new Builder(item.asItem().getDefaultInstance());
	}

	@Override
	public boolean matches(Input pInput, Level pLevel)
	{
		return input.test(pInput.input);
	}

	@Override
	public ItemStack assemble(Input pInput, HolderLookup.Provider pRegistries)
	{
		return result.copy();
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return true;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries)
	{
		return result;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return RecipeThings.BRICK_DRYING_SERIALIZER.get();
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeThings.BRICK_DRYING_TYPE.get();
	}

	@Override
	public int getProgress()
	{
		return time;
	}

	public Block getInBlock()
	{
		return inBlock;
	}

	public Block getOutBlock()
	{
		return outBlock;
	}

	public record Input(ItemStack input) implements RecipeInput
	{
		@Override
		public ItemStack getItem(int slot)
		{
			if (slot != 0)
				throw new IllegalArgumentException("No item for index " + slot);
			return input;
		}

		@Override
		public int size()
		{
			return 1;
		}
	}

	public static class Serializer implements RecipeSerializer<BrickDryingRecipe>
	{
		public static final MapCodec<BrickDryingRecipe> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
				Ingredient.CODEC_NONEMPTY.fieldOf("input").forGetter(r -> r.input),
				ItemStack.STRICT_CODEC.fieldOf("result").forGetter(r -> r.result),
				Codec.INT.fieldOf("time").forGetter(r -> r.time),
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("inBlock").forGetter(r -> r.inBlock),
				BuiltInRegistries.BLOCK.byNameCodec().fieldOf("outBlock").forGetter(r -> r.outBlock)).apply(i, BrickDryingRecipe::new));
		public static final StreamCodec<RegistryFriendlyByteBuf, BrickDryingRecipe> STREAM_CODEC = StreamCodec.composite(
				Ingredient.CONTENTS_STREAM_CODEC, r -> r.input,
				ItemStack.STREAM_CODEC, r -> r.result,
				ByteBufCodecs.VAR_INT, r -> r.time,
				ByteBufCodecs.fromCodecWithRegistries(BuiltInRegistries.BLOCK.byNameCodec()), r -> r.inBlock,
				ByteBufCodecs.fromCodecWithRegistries(BuiltInRegistries.BLOCK.byNameCodec()), r -> r.outBlock,
				BrickDryingRecipe::new);

		@Override
		public MapCodec<BrickDryingRecipe> codec()
		{
			return CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, BrickDryingRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}

	public static class Builder implements RecipeBuilder
	{
		private final ItemStack result;
		private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
		private Ingredient input;
		private int time;
		private Block inBlock;
		private Block outBlock;

		private Builder(ItemStack result)
		{
			this.result = result;
		}

		@Override
		public Builder unlockedBy(String pName, Criterion<?> pCriterion)
		{
			criteria.put(pName, pCriterion);
			return this;
		}

		@Override
		public Builder group(@Nullable String pGroupName)
		{
			return this;
		}

		@Override
		public Item getResult()
		{
			return result.getItem();
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

		public Builder time(int time)
		{
			this.time = time;
			return this;
		}

		public Builder blocks(Block in, Block out)
		{
			inBlock = in;
			outBlock = out;
			return this;
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
			var recipe = new BrickDryingRecipe(input, result, time, inBlock, outBlock);
			output.accept(location, recipe, advancementBuilder.build(location.withPrefix("recipes/")));
		}

		@Override
		public void save(RecipeOutput output)
		{
			save(output, BuiltInRegistries.ITEM.getKey(result.getItem()).withPrefix("brick_drying/"));
		}
	}
}
