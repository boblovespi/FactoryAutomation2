package boblovespi.factoryautomation.api.capability;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.IBellowsConsumer;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class BellowsCapability
{
	public static final BlockCapability<IBellowsConsumer, Direction> BLOCK = BlockCapability.createSided(FactoryAutomation.name("bellows_consumer"), IBellowsConsumer.class);
}
