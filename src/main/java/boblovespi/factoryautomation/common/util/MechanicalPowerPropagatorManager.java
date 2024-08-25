package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.capability.MechanicalCapability;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class MechanicalPowerPropagatorManager
{
	private final Map<Direction, BlockCapabilityCache<IMechanicalInput, Direction>> cache;
	private final Collection<Direction> validPuts;
	private final BooleanSupplier invalidator;
	private final Consumer<Direction> onChanged;
	private final Consumer<Direction> update;

	public MechanicalPowerPropagatorManager(Collection<Direction> validOutputs, BooleanSupplier invalidator, Consumer<Direction> onChanged, Consumer<Direction> update)
	{
		this.validPuts = validOutputs;
		this.invalidator = invalidator;
		this.onChanged = onChanged;
		this.update = update;
		cache = new EnumMap<>(Direction.class);
	}

	public void load(ServerLevel level, BlockPos pos)
	{
		for (var dir : validPuts)
			cache.put(dir, BlockCapabilityCache.create(MechanicalCapability.INPUT, level, pos.relative(dir), dir.getOpposite(), invalidator, () -> onChanged.accept(dir)));
	}

	@Nullable
	public IMechanicalInput get(Direction dir)
	{
		return cache.get(dir).getCapability();
	}

	public void updateAll()
	{
		for (var dir : validPuts)
			update.accept(dir);
	}
}
