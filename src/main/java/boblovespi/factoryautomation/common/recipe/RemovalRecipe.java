package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class RemovalRecipe implements Recipe<RemovalRecipe.NoInput>
{
	private static final RemovalRecipe UNIT = new RemovalRecipe();

	private RemovalRecipe()
	{
	}

	@Override
	public boolean matches(NoInput pInput, Level pLevel)
	{
		return false;
	}

	@Override
	public ItemStack assemble(NoInput pInput, HolderLookup.Provider pRegistries)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int pWidth, int pHeight)
	{
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pRegistries)
	{
		return ItemStack.EMPTY;
	}

	@Override
	public RecipeSerializer<?> getSerializer()
	{
		return Serializer.INSTANCE;
	}

	@Override
	public RecipeType<?> getType()
	{
		return RecipeThings.REMOVAL_RECIPE_TYPE.get();
	}

	public static Builder unitFor(ItemLike target)
	{
		return new Builder(target);
	}

	public record NoInput() implements RecipeInput
	{
		@Override
		public ItemStack getItem(int pIndex)
		{
			return ItemStack.EMPTY;
		}

		@Override
		public int size()
		{
			return 0;
		}
	}

	public static class Serializer implements RecipeSerializer<RemovalRecipe>
	{
		public static final Serializer INSTANCE = new Serializer();
		private static final MapCodec<RemovalRecipe> MAP_CODEC = MapCodec.unit(UNIT);
		private static final StreamCodec<RegistryFriendlyByteBuf, RemovalRecipe> STREAM_CODEC = StreamCodec.unit(UNIT);

		@Override
		public MapCodec<RemovalRecipe> codec()
		{
			return MAP_CODEC;
		}

		@Override
		public StreamCodec<RegistryFriendlyByteBuf, RemovalRecipe> streamCodec()
		{
			return STREAM_CODEC;
		}
	}

	public static final class Builder implements RecipeBuilder
	{
		private final ItemLike target;

		private Builder(ItemLike target)
		{
			this.target = target;
		}

		@Override
		public RecipeBuilder unlockedBy(String pName, Criterion<?> pCriterion)
		{
			return this;
		}

		@Override
		public RecipeBuilder group(@Nullable String pGroupName)
		{
			return this;
		}

		@Override
		public Item getResult()
		{
			return target.asItem();
		}

		@Override
		public void save(RecipeOutput output, ResourceLocation id)
		{
			output.accept(id, UNIT, null);
		}

		public void forSmelting(RecipeOutput output, ItemLike from)
		{
			save(output, BuiltInRegistries.ITEM.getKey(target.asItem()).getPath() + "_from_smelting_" + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
			save(output, BuiltInRegistries.ITEM.getKey(target.asItem()).getPath() + "_from_blasting_" + BuiltInRegistries.ITEM.getKey(from.asItem()).getPath());
		}

		@Override
		public boolean equals(Object obj)
		{
			if (obj == this)
				return true;
			if (obj == null || obj.getClass() != this.getClass())
				return false;
			var that = (Builder) obj;
			return Objects.equals(this.target, that.target);
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(target);
		}

		@Override
		public String toString()
		{
			return "Builder[" +
				   "target=" + target + ']';
		}

	}
}
