package boblovespi.factoryautomation.common.block.mechanical;

import boblovespi.factoryautomation.common.block.processing.StoneCrucible;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IClientTickable;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.mechanical.LargeWaterwheelBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class LargeWaterwheel extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty MULTIBLOCK_COMPLETE = StoneCrucible.MULTIBLOCK_COMPLETE;

	public LargeWaterwheel(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		if (!level.isClientSide)
		{
			var facing = state.getValue(FACING).getClockWise();
			if (!state.getValue(MULTIBLOCK_COMPLETE))
			{
				if (Multiblocks.LARGE_WATERWHEEL.isValid(level, pos, facing))
				{
					Multiblocks.LARGE_WATERWHEEL.build(level, pos, facing);
					level.setBlock(pos, state.setValue(MULTIBLOCK_COMPLETE, true), 2);
				}
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new LargeWaterwheelBE(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(FACING, MULTIBLOCK_COMPLETE);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected RenderShape getRenderShape(BlockState pState)
	{
		return RenderShape.ENTITYBLOCK_ANIMATED;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return IClientTickable.makeTicker(FABETypes.LARGE_WATERWHEEL_TYPE.get(), beType);
		else if (pState.getValue(MULTIBLOCK_COMPLETE))
			return ITickable.makeTicker(FABETypes.LARGE_WATERWHEEL_TYPE.get(), beType);
		else
			return null;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.LARGE_WATERWHEEL_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston)
	{
		// if (neighborBlock == Blocks.WATER)
		// level.getBlockEntity(pos, FABETypes.LARGE_WATERWHEEL_TYPE.get()).ifPresent(LargeWaterwheelBE::updateWater);
		// level.getBlockEntity(pos, FABETypes.LARGE_WATERWHEEL_TYPE.get()).ifPresent(LargeWaterwheelBE::updateInputs);
	}
}