package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.mechanical.BevelGearBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.FrontAndTop;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
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

public class BevelGear extends Block implements EntityBlock
{
	public static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
	public final float maxSpeed;
	public final float maxTorque;
	private static final VoxelShape NORTH = Block.box(4, 4, 0, 12, 12, 12);
	private static final VoxelShape SOUTH = Block.box(4, 4, 4, 12, 12, 16);
	private static final VoxelShape EAST = Block.box(4, 4, 4, 16, 12, 12);
	private static final VoxelShape WEST = Block.box(0, 4, 4, 12, 12, 12);
	private static final VoxelShape UP = Block.box(4, 4, 4, 12, 16, 12);
	private static final VoxelShape DOWN = Block.box(4, 0, 4, 12, 12, 12);
	private static final VoxelShape[] COLLISON_BOXES = new VoxelShape[] {
			Shapes.or(DOWN, EAST),
			Shapes.or(DOWN, NORTH),
			Shapes.or(DOWN, SOUTH),
			Shapes.or(DOWN, WEST),
			Shapes.or(UP, EAST),
			Shapes.or(UP, NORTH),
			Shapes.or(UP, SOUTH),
			Shapes.or(UP, WEST),
			Shapes.or(WEST, SOUTH),
			Shapes.or(EAST, NORTH),
			Shapes.or(NORTH, WEST),
			Shapes.or(SOUTH, EAST)
	};

	public BevelGear(Properties properties, float maxSpeed, float maxTorque)
	{
		super(properties);
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BevelGearBE(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(ORIENTATION);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		var dir = pContext.getNearestLookingDirection();
		if (dir.getAxis() == Direction.Axis.Y)
		{
			var horizontalDir = pContext.getHorizontalDirection();
			return defaultBlockState().setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(dir.getOpposite(), horizontalDir));
		}
		else
			return defaultBlockState().setValue(ORIENTATION, FrontAndTop.fromFrontAndTop(dir, Direction.UP));
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return COLLISON_BOXES[pState.getValue(ORIENTATION).ordinal()];
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.BEVEL_GEAR_TYPE.get(), beType);
		return null;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.BEVEL_GEAR_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		level.getBlockEntity(pos, FABETypes.BEVEL_GEAR_TYPE.get()).ifPresent(BevelGearBE::updateInputs);
	}
}
