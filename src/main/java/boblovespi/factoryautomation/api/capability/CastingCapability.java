package boblovespi.factoryautomation.api.capability;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.util.ICastingVessel;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class CastingCapability
{
	public static final BlockCapability<ICastingVessel, Direction> BLOCK = BlockCapability.createSided(FactoryAutomation.name("casting"), ICastingVessel.class);
}
