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
}
