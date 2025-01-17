package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;

import javax.annotation.Nullable;

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

	@Nullable
	default <T> T getCapability(BlockPos offset, BlockCapability<T, Direction> capability, Direction dir)
	{
		return null;
	}
}
