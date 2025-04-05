package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.mechanical.SplitterBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class Splitter extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty VERTICAL = BooleanProperty.create("vertical");
	public static final VoxelShape BASE_BOX = Block.box(0, 0, 0, 16, 2, 16);
	public static final VoxelShape Z_BOX = Block.box(4, 2, 0, 12, 14, 16);
	public static final VoxelShape Z_V_BOX = Block.box(2, 4, 0, 14, 12, 16);
	public static final VoxelShape X_BOX = Block.box(0, 2, 4, 16, 14, 12);
	public static final VoxelShape X_V_BOX = Block.box(0, 4, 2, 16, 12, 14);
	private static final VoxelShape[] COLLISON_BOXES = new VoxelShape[] {
			BASE_BOX,
			BASE_BOX,
			Shapes.or(BASE_BOX, X_BOX, Block.box(2, 0, 12, 14, 16, 16), Block.box(4, 0, 12, 12, 14, 14)),
			Shapes.or(BASE_BOX, X_BOX, Block.box(2, 0, 0, 14, 16, 4), Block.box(4, 0, 2, 12, 14, 4)),
			Shapes.or(BASE_BOX, Z_BOX, Block.box(12, 0, 2, 16, 16, 14), Block.box(12, 0, 4, 14, 14, 12)),
			Shapes.or(BASE_BOX, Z_BOX, Block.box(0, 0, 2, 4, 16, 14), Block.box(2, 0, 4, 4, 14, 12)),
			};
	public static final VoxelShape V_BASE_NS = Block.box(0, 0, 0, 16, 16, 2);
	public static final VoxelShape V_BASE_EW = Block.box(0, 0, 0, 2, 16, 16);
	private static final VoxelShape[] VERTICAL_COLLISION_BOXES = new VoxelShape[] {
			BASE_BOX,
			BASE_BOX,
			Shapes.or(V_BASE_NS, X_V_BOX, Block.box(2, 0, 0, 14, 4, 16)),
			Shapes.or(V_BASE_NS, X_V_BOX, Block.box(2, 12, 0, 14, 16, 16)),
			Shapes.or(V_BASE_EW, Z_V_BOX, Block.box(0, 0, 2, 16, 4, 14)),
			Shapes.or(V_BASE_EW, Z_V_BOX, Block.box(0, 12, 2, 16, 16, 14)),
			};

	public final float maxSpeed;
	public final float maxTorque;

	public Splitter(Properties properties, float maxSpeed, float maxTorque)
	{
		super(properties);
		this.maxSpeed = maxSpeed;
		this.maxTorque = maxTorque;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SplitterBE(pos, state);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return pState.getValue(VERTICAL) ? VERTICAL_COLLISION_BOXES[pState.getValue(FACING).ordinal()] :  COLLISON_BOXES[pState.getValue(FACING).ordinal()];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING, VERTICAL);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.SPLITTER_TYPE.get(), beType);
		else
			return null;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		if (pContext.getNearestLookingDirection().getAxis() == Direction.Axis.Y)
		{
			var dir = pContext.getHorizontalDirection();
			var up = pContext.getNearestLookingDirection() == Direction.UP;
			return defaultBlockState().setValue(FACING, Direction.get(up ? Direction.AxisDirection.POSITIVE : Direction.AxisDirection.NEGATIVE, dir.getAxis()))
									  .setValue(VERTICAL, true);
		}
		else
			return defaultBlockState().setValue(FACING, pContext.getNearestLookingDirection().getOpposite()).setValue(VERTICAL, false);
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.SPLITTER_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		level.getBlockEntity(pos, FABETypes.SPLITTER_TYPE.get()).ifPresent(SplitterBE::updateInputs);
	}
}
