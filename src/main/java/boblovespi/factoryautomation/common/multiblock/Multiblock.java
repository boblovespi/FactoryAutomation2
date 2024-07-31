package boblovespi.factoryautomation.common.multiblock;

import boblovespi.factoryautomation.common.block.MultiblockPart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public abstract class Multiblock
{
	private final ResourceLocation name;

	protected Multiblock(ResourceLocation name)
	{
		this.name = name;
	}

	/**
	 * Gets the unique identifier for this multiblock.
	 */
	public ResourceLocation getName()
	{
		return name;
	}

	/**
	 * Check if this multiblock is valid in the current position facing the current direction.
	 *
	 * @param level         Access to the current level
	 * @param controllerPos The position of the controller block
	 * @param facing        The current direction
	 *
	 * @return {@code true} if the multiblock is valid, {@code false} otherwise.
	 */
	public abstract boolean isValid(Level level, BlockPos controllerPos, Direction facing);

	/**
	 * Construct the multiblock, ideally replacing the existing blocks with {@link MultiblockPart}s. Should also notify the multiblock controller, if it exists, of construction via {@link IMultiblockBE#onMultiblockBuilt()}.
	 *
	 * @param level         Access to the current level
	 * @param controllerPos The position of the controller block
	 * @param facing        The current direction
	 */
	public abstract void build(Level level, BlockPos controllerPos, Direction facing);

	/**
	 * Remove the multiblock from the world. Note: parts of the multiblock may already not exist. Should also notify the multiblock controller, if it exists, of destruction via {@link IMultiblockBE#onMultiblockDestroyed()}.
	 *
	 * @param level         Access to the current level
	 * @param controllerPos The position of the controller block
	 * @param facing        The current direction
	 */
	public abstract void destroy(Level level, BlockPos controllerPos, Direction facing);
}
