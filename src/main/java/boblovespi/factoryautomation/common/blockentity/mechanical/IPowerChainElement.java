package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface IPowerChainElement
{
	/**
	 * @return The source {@link IPowerChainElement}.
	 */
	IPowerChainElement setSource(IPowerChainElement source, Direction inputSide);

	void notifyBroken(Direction brokenSide);

	float getRotation(float delta);

	MechanicalManager getManager();

	BlockPos getPos();
}
