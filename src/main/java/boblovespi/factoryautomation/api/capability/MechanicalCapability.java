package boblovespi.factoryautomation.api.capability;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.IMechanicalInput;
import boblovespi.factoryautomation.api.IMechanicalOutput;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class MechanicalCapability
{
	public static final BlockCapability<IMechanicalOutput, Direction> OUTPUT = BlockCapability.createSided(FactoryAutomation.name("mechanical_output"), IMechanicalOutput.class);
	public static final BlockCapability<IMechanicalInput, Direction> INPUT = BlockCapability.createSided(FactoryAutomation.name("mechanical_input"), IMechanicalInput.class);
}
