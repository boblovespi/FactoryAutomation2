package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TemperatureData(float temperature, float power)
{
	public static final MapCodec<TemperatureData> CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(Codec.FLOAT.fieldOf("temperature").forGetter(TemperatureData::temperature), Codec.FLOAT.fieldOf("power").forGetter(TemperatureData::power))
				  .apply(i, TemperatureData::new));

	public static final StreamCodec<ByteBuf, TemperatureData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, TemperatureData::temperature,
			ByteBufCodecs.FLOAT, TemperatureData::power, TemperatureData::new);

	public static class Builder<T extends SimpleRecipe<?, TemperatureData>> extends SimpleRecipe.DataBuilder<T, TemperatureData, TemperatureData.Builder<T>>
	{
		private float temperature;
		private float power;

		protected Builder(SimpleRecipe.Builder<T, TemperatureData, TemperatureData.Builder<T>> builder)
		{
			super(builder);
		}

		public TemperatureData.Builder<T> temperature(float temperature)
		{
			this.temperature = temperature;
			return this;
		}

		public TemperatureData.Builder<T> power(float power)
		{
			this.power = power;
			return this;
		}

		@Override
		protected TemperatureData build()
		{
			return new TemperatureData(temperature, power);
		}
	}
}
