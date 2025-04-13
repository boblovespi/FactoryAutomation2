package boblovespi.factoryautomation.common.blockentity.mechanical;

import boblovespi.factoryautomation.common.util.MechanicalManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public interface IPowerChainElement
{
	/**
	 * @return The source {@link IPowerChainElement}.
	 */
	@Nullable
	IPowerChainElement setSource(IPowerChainElement source, Direction inputSide);

	void notifyBroken(Direction brokenSide);

	float getRotation(float delta);

	MechanicalManager getManager();

	BlockPos getPos();
}
