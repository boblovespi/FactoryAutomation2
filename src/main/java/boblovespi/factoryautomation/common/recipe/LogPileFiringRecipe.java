package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class LogPileFiringRecipe extends SimpleRecipe<LogPileFiringRecipe.Input, LogPileFiringRecipe.Data>
{
	private static final BuilderFactory<LogPileFiringRecipe, Data, Data.Builder> BUILDER_FACTORY = new BuilderFactory<>("log_pile_firing",
			LogPileFiringRecipe::new, Data.Builder::new);

	public static final MapCodec<Data> DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("logPileLike").forGetter(Data::logPileLike)).apply(i, Data::new));
	public static final StreamCodec<? super RegistryFriendlyByteBuf, Data> DATA_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.registry(Registries.BLOCK), Data::logPileLike,
			Data::new);

	public static Builder<LogPileFiringRecipe, Data, Data.Builder> of(Block block)
	{
		return BUILDER_FACTORY.of(new ItemStack(block));
	}

	private final Data data;

	protected LogPileFiringRecipe(Ingredient input, ItemStack result, int progress, Data data)
	{
		super(input, result, progress, RecipeThings.LOG_PILE_FIRING_TYPE.get(), RecipeThings.LOG_PILE_FIRING_SERIALIZER.get());
		this.data = data;
	}

	@Override
	protected boolean matchExtra(Input input, Level level)
	{
		return input.logPileLike == data.logPileLike;
	}

	@Override
	public Data getData()
	{
		return data;
	}

	public static class Input extends SimpleRecipe.Input
	{
		private final Block logPileLike;

		public Input(ItemStack stack, Block logPileLike)
		{
			super(stack);
			this.logPileLike = logPileLike;
		}
	}

	public record Data(Block logPileLike)
	{
		public static class Builder extends DataBuilder<LogPileFiringRecipe, Data, Builder>
		{
			private Block logPileLike = Blocks.AIR;

			protected Builder(SimpleRecipe.Builder<LogPileFiringRecipe, Data, Builder> builder)
			{
				super(builder);
			}

			public Builder logPileLike(Block logPileLike)
			{
				this.logPileLike = logPileLike;
				return this;
			}

			@Override
			protected Data build()
			{
				return new Data(logPileLike);
			}
		}

	}
}
