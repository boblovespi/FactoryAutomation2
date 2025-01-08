package boblovespi.factoryautomation.common.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public class Codecs
{
	public static final MapCodec<Integer> JADE_COOKING = Codec.INT.fieldOf("jade:cooking");
}
