package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.block.FABlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class LogPile extends LogPileLike
{
	private static final VoxelShape BOUNDING_BOX = Shapes.or(Block.box(0, 0, 0, 4, 16, 4), Block.box(6, 0, 0, 10, 16, 4),
			Block.box(12, 0, 0, 16, 16, 4), Block.box(0, 0, 6, 4, 16, 10),
			Block.box(6, 0, 6, 10, 16, 10), Block.box(12, 0, 6, 16, 16, 10),
			Block.box(0, 0, 12, 4, 16, 16), Block.box(6, 0, 12, 10, 16, 16),
			Block.box(12, 0, 12, 16, 16, 16));

	public LogPile(Properties properties)
	{
		super(properties, 20 * 60 * 5); // 5 minutes
	}

	@Override
	public BlockState getResultState()
	{
		return FABlocks.CHARCOAL_PILE.get().defaultBlockState();
	}

	@Override
	public boolean isValidSurroundingBlock(Level level, BlockState state, BlockPos pos, Direction face)
	{
		return state.isFaceSturdy(level, pos, face);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}
}
