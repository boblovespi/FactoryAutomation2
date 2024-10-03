package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.common.blockentity.processing.BrickCrucibleBE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BrickCrucible extends Block implements EntityBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = StoneCrucible.MULTIBLOCK_COMPLETE;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	private static final VoxelShape BOUNDING_BOX = Block.box(2, 0, 2, 14, 16, 14);

	public BrickCrucible(Properties p)
	{
		super(p);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false).setValue(FACING, Direction.NORTH));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new BrickCrucibleBE(pPos, pState);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		if (!level.isClientSide)
		{
			var be = level.getBlockEntity(pos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).orElseThrow();
			var castingVessel = level.getCapability(CastingCapability.BLOCK, pos.north().below(), Direction.UP);
				if (pHitResult.getDirection() == Direction.NORTH && castingVessel != null)
					be.pour(castingVessel);
				else
					player.openMenu(state.getMenuProvider(level, pos));
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState pState, Level pLevel, BlockPos pPos)
	{
		return pLevel.getBlockEntity(pPos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).map(IMenuProviderProvider::getMenuProvider).orElse(null);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_CRUCIBLE_TYPE.get(), beType);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return BOUNDING_BOX;
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
}
