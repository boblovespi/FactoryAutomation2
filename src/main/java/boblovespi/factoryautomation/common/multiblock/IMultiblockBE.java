package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface IMultiblockBE
{
	/**
	 * Called whenever the multiblock is successfully formed from {@link Multiblock#build(Level, BlockPos, Direction)}.
	 */
	void onMultiblockBuilt();

	/**
	 * Called whenever the multiblock is successfully broken from {@link Multiblock#destroy(Level, BlockPos, Direction)}.
	 */
	void onMultiblockDestroyed();
}
