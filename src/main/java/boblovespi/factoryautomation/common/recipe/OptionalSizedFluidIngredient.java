package boblovespi.factoryautomation.common.recipe;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

public sealed interface OptionalSizedFluidIngredient
{
	Empty EMPTY = new Empty();
	Codec<OptionalSizedFluidIngredient> CODEC = Codec.either(
			SizedFluidIngredient.FLAT_CODEC.xmap(OptionalSizedFluidIngredient.Present::new, OptionalSizedFluidIngredient.Present::value),
			Codec.unit(OptionalSizedFluidIngredient.EMPTY)).xmap(OptionalSizedFluidIngredient::fromEither, OptionalSizedFluidIngredient::toEither);
	StreamCodec<RegistryFriendlyByteBuf, OptionalSizedFluidIngredient> STREAM_CODEC = ByteBufCodecs.either(
			SizedFluidIngredient.STREAM_CODEC.map(OptionalSizedFluidIngredient.Present::new, OptionalSizedFluidIngredient.Present::value),
			StreamCodec.unit(EMPTY)).map(OptionalSizedFluidIngredient::fromEither, OptionalSizedFluidIngredient::toEither);

	private static Either<Present, Empty> toEither(OptionalSizedFluidIngredient a)
	{
		return switch (a)
		{
			case Present p -> Either.left(p);
			case Empty e -> Either.right(e);
		};
	}

	private static OptionalSizedFluidIngredient fromEither(Either<Present, Empty> e)
	{
		return e.map(b -> b, b -> b);
	}

	int amount();

	record Present(SizedFluidIngredient value) implements OptionalSizedFluidIngredient
	{
		@Override
		public int amount()
		{
			return value.amount();
		}
	}

	record Empty() implements OptionalSizedFluidIngredient
	{
		@Override
		public int amount()
		{
			return 0;
		}
	}
}
