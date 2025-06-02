package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.LeatherBellowsBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class LeatherBellows extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape SHAPE = Shapes.or(Block.box(0, 0, 0, 16, 3, 16), Block.box(2, 2, 2, 14, 16, 14));

	public LeatherBellows(Properties properties)
	{
		super(properties);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new LeatherBellowsBE(pos, state);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.LEATHER_BELLOWS_TYPE.get(), beType);
		return ITickable.makeTicker(FABETypes.LEATHER_BELLOWS_TYPE.get(), beType);
	}

	@Override
	protected RenderShape getRenderShape(BlockState state)
	{
		return RenderShape.INVISIBLE;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}
}
