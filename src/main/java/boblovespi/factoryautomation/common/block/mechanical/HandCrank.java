package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.mechanical.HandCrankBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class HandCrank extends Block implements EntityBlock
{
	public static final BooleanProperty HANGING = BlockStateProperties.HANGING;
	public static final VoxelShape SHAPE = Shapes.or(Block.box(7, 0, 7, 9, 14, 9), Block.box(0, 14, 0, 16, 16, 16));
	public static final VoxelShape HANGING_SHAPE = Shapes.or(Block.box(7, 2, 7, 9, 16, 9), Block.box(0, 0, 0, 16, 2, 16));
	public static final VoxelShape COLLISON_SHAPE = Block.box(7, 0, 7, 9, 16,9);

	public HandCrank(Properties p)
	{
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new HandCrankBE(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(HANGING);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return pState.getValue(HANGING) ? HANGING_SHAPE : SHAPE;
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return COLLISON_SHAPE;
	}

	@Override
	protected VoxelShape getOcclusionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos)
	{
		return COLLISON_SHAPE;
	}

	@Override
	protected RenderShape getRenderShape(BlockState pState)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext pContext)
	{
		return defaultBlockState().setValue(HANGING, pContext.getClickedFace() == Direction.DOWN);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState pState, Level level, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return InteractionResult.SUCCESS;
		level.getBlockEntity(pPos, FABETypes.HANDCRANK_TYPE.get()).ifPresent(HandCrankBE::setRunning);
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.HANDCRANK_TYPE.get(), beType);
		else
			return ITickable.makeTicker(FABETypes.HANDCRANK_TYPE.get(), beType);
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.HANDCRANK_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		level.getBlockEntity(pos, FABETypes.HANDCRANK_TYPE.get()).ifPresent(HandCrankBE::updateInputs);
	}
}
