package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class OrePile extends LogPileLike
{
	private final BlockState state;

	public OrePile(Properties properties, int processTime, BlockState state)
	{
		super(properties, processTime, false);
		this.state = state;
	}

	@Override
	public BlockState getResultState()
	{
		return state;
	}

	@Override
	public boolean isValidSurroundingBlock(Level level, BlockState state, BlockPos pos, Direction face)
	{
		return state.is(FABlocks.COPPER_PLATE_BLOCK);
	}
}
