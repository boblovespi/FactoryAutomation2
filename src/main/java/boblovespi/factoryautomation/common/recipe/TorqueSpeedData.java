package boblovespi.factoryautomation.common.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record TorqueSpeedData(float torque, float speed)
{
	public static final MapCodec<TorqueSpeedData> CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(Codec.FLOAT.fieldOf("torque").forGetter(TorqueSpeedData::torque), Codec.FLOAT.fieldOf("speed").forGetter(TorqueSpeedData::speed))
				  .apply(i, TorqueSpeedData::new));

	public static final StreamCodec<ByteBuf, TorqueSpeedData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.FLOAT, TorqueSpeedData::torque, ByteBufCodecs.FLOAT,
			TorqueSpeedData::speed, TorqueSpeedData::new);

	public static class Builder<T extends SimpleRecipe<?, TorqueSpeedData>> extends SimpleRecipe.DataBuilder<T, TorqueSpeedData, Builder<T>>
	{
		private float torque;
		private float speed;

		protected Builder(SimpleRecipe.Builder<T, TorqueSpeedData, Builder<T>> builder)
		{
			super(builder);
		}

		public Builder<T> torque(float torque)
		{
			this.torque = torque;
			return this;
		}

		public Builder<T> speed(float speed)
		{
			this.speed = speed;
			return this;
		}

		@Override
		protected TorqueSpeedData build()
		{
			return new TorqueSpeedData(torque, speed);
		}
	}
}
