package boblovespi.factoryautomation.common.util;

import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public interface IMaybeSerializable<T extends Tag> extends INBTSerializable<T>
{
	boolean shouldSerialize();
}
