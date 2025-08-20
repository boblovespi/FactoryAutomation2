package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.BrickKilnBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import org.jetbrains.annotations.Nullable;

public class BrickKiln extends Block implements EntityBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = StoneCrucible.MULTIBLOCK_COMPLETE;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BrickKiln(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false).setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BrickKilnBE(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MULTIBLOCK_COMPLETE, FACING, LIT);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult pHitResult)
	{
		if (!level.isClientSide)
		{
			var facing = state.getValue(FACING);
			if (state.getValue(MULTIBLOCK_COMPLETE))
				player.openMenu(state.getMenuProvider(level, pos));
			else if (Multiblocks.BRICK_KILN.isValid(level, pos, facing))
			{
				Multiblocks.BRICK_KILN.build(level, pos, facing);
				level.setBlock(pos, state.setValue(MULTIBLOCK_COMPLETE, true), 2);
			}
			return InteractionResult.CONSUME;
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Nullable
	@Override
	protected MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos)
	{
		return level.getBlockEntity(pos, FABETypes.BRICK_KILN_TYPE.get()).map(IMenuProviderProvider::getMenuProvider).orElse(null);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
			level.getBlockEntity(pos, FABETypes.BRICK_KILN_TYPE.get()).ifPresent(BrickKilnBE::onDestroy);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide || !pState.getValue(MULTIBLOCK_COMPLETE))
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_KILN_TYPE.get(), beType);
	}
}
