package boblovespi.factoryautomation.api.capability;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class MechanicalCapability
{
	public static final BlockCapability<IMechanicalOutput, Direction> BLOCK = BlockCapability.createSided(FactoryAutomation.name("mechanical"), IMechanicalOutput.class);
}
