package boblovespi.factoryautomation.common.util;

import java.util.Optional;
import java.util.function.Function;

public interface ICastingVessel
{
	void cast(Function<Integer, Optional<Metal>> metalSource);
}
