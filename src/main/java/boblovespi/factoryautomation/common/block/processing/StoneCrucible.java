package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.StoneCrucibleBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class StoneCrucible extends Block implements EntityBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = BooleanProperty.create("multiblock_complete");
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BASE_SHAPE = Block.box(2, 1, 2, 14, 15, 14);
	private static final VoxelShape BASE_COMPLETE_SHAPE = BASE_SHAPE.move(0, -1 / 16f, 0);
	private static final VoxelShape[] INCOMPLETE_SHAPE = new VoxelShape[] {
			Shapes.or(BASE_SHAPE, Block.box(0, 0, 6, 2, 16, 10), Block.box(14, 0, 6, 16, 16, 10)),
			Shapes.or(BASE_SHAPE, Block.box(6, 0, 0, 10, 16, 2), Block.box(6, 0, 14, 10, 16, 16))
	};
	private static final VoxelShape[] COMPLETE_SHAPE = new VoxelShape[] {
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(0, 0, 6, 2, 16, 10), Block.box(14, 0, 6, 16, 16, 10)).move(0, 0, -2 / 16f),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(6, 0, 0, 10, 16, 2), Block.box(6, 0, 14, 10, 16, 16)).move(2 / 16f, 0, 0),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(0, 0, 6, 2, 16, 10), Block.box(14, 0, 6, 16, 16, 10)).move(0, 0, 2 / 16f),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(6, 0, 0, 10, 16, 2), Block.box(6, 0, 14, 10, 16, 16)).move(-2 / 16f, 0, 0)
	};

	public StoneCrucible(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false).setValue(FACING, Direction.NORTH));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		if (!level.isClientSide)
		{
			var be = level.getBlockEntity(pos, FABETypes.STONE_CRUCIBLE_TYPE.get()).orElseThrow();
			var castingVessel = level.getCapability(CastingCapability.BLOCK, pos.relative(state.getValue(FACING)).below(), Direction.UP);
			if (state.getValue(MULTIBLOCK_COMPLETE))
			{
				if (pHitResult.getDirection() == state.getValue(FACING) && castingVessel != null)
					be.pour(castingVessel);
				else
					player.openMenu(state.getMenuProvider(level, pos));
			}
			else if (Multiblocks.STONE_CRUCIBLE.isValid(level, pos, state.getValue(FACING)))
			{
				Multiblocks.STONE_CRUCIBLE.build(level, pos, state.getValue(FACING));
				level.setBlock(pos, state.setValue(MULTIBLOCK_COMPLETE, true), 2);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.STONE_CRUCIBLE_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new StoneCrucibleBE(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide || !pState.getValue(MULTIBLOCK_COMPLETE))
			return null;
		return ITickable.makeTicker(FABETypes.STONE_CRUCIBLE_TYPE.get(), beType);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return state.getValue(MULTIBLOCK_COMPLETE) ? COMPLETE_SHAPE[state.getValue(FACING).get2DDataValue()] : INCOMPLETE_SHAPE[state.getValue(FACING).get2DDataValue() % 2];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MULTIBLOCK_COMPLETE, FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getCounterClockWise());
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos)
	{
		return pLevel.getBlockEntity(pPos, FABETypes.STONE_CRUCIBLE_TYPE.get()).map(StoneCrucibleBE::getMenuProvider).orElse(null);
	}
}
