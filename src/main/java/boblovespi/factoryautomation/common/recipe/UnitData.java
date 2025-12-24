package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public enum UnitData
{
	INSTANCE;

	public static final MapCodec<UnitData> DATA_CODEC = MapCodec.unit(UnitData.INSTANCE);
	public static final StreamCodec<? super RegistryFriendlyByteBuf, UnitData> DATA_STREAM_CODEC = StreamCodec.unit(UnitData.INSTANCE);

	public static class Builder<R extends SimpleRecipe<?, UnitData>> extends SimpleRecipe.DataBuilder<R, UnitData, UnitData.Builder<R>>
	{
		protected Builder(SimpleRecipe.Builder<R, UnitData, UnitData.Builder<R>> builder)
		{
			super(builder);
		}

		@Override
		protected UnitData build()
		{
			return INSTANCE;
		}
	}
}
