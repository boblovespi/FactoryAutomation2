package boblovespi.factoryautomation.api.capability;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.api.IHeatUser;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.BlockCapability;

public class HeatCapability
{
	public static final BlockCapability<IHeatUser, Direction> BLOCK = BlockCapability.createSided(FactoryAutomation.name("heat"), IHeatUser.class);
}
