package boblovespi.factoryautomation.common.block.resource;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Supplier;

public class ResourceRock extends Block
{
	private static final VoxelShape BOUNDING_BOX = Block.box(3, 0, 3, 13, 5, 13);
	private final Supplier<Item> mainDrop;

	public ResourceRock(Properties p, Supplier<Item> mainDrop)
	{
		super(p);
		this.mainDrop = mainDrop;
	}

	public ResourceRock(Properties p, Item mainDrop)
	{
		super(p);
		this.mainDrop = () -> mainDrop;
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected BlockState updateShape(BlockState pState, Direction pDirection, BlockState pNeighborState, LevelAccessor pLevel, BlockPos pPos, BlockPos pNeighborPos)
	{
		return pDirection == Direction.DOWN && !pState.canSurvive(pLevel, pPos) ? Blocks.AIR.defaultBlockState() : pState;
	}

	@Override
	protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos)
	{
		return pLevel.getBlockState(pPos.below()).isFaceSturdy(pLevel, pPos.below(), Direction.UP);
	}

	@Override
	public Item asItem()
	{
		return mainDrop.get();
	}
}
