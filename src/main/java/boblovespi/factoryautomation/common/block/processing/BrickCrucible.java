package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.api.capability.CastingCapability;
import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.IMenuProviderProvider;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.BrickCrucibleBE;
import boblovespi.factoryautomation.common.multiblock.Multiblocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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

public class BrickCrucible extends Block implements EntityBlock
{
	public static final BooleanProperty MULTIBLOCK_COMPLETE = StoneCrucible.MULTIBLOCK_COMPLETE;
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;
	private static final VoxelShape BASE_SHAPE = Block.box(2, 2, 2, 14, 16, 14);
	private static final VoxelShape BASE_COMPLETE_SHAPE = Block.box(2, 0, 2, 14, 15, 14);
	private static final VoxelShape[] INCOMPLETE_SHAPE = new VoxelShape[] {
			Shapes.or(BASE_SHAPE, Block.box(0, 0, 6, 2, 16, 10), Block.box(14, 0, 6, 16, 16, 10)),
			Shapes.or(BASE_SHAPE, Block.box(6, 0, 0, 10, 16, 2), Block.box(6, 0, 14, 10, 16, 16))
	};
	private static final VoxelShape[] COMPLETE_SHAPE = new VoxelShape[] {
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(0, 0, 6, 2, 16, 10), Block.box(12, 0, 5, 16, 16, 11)),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(6, 0, 0, 10, 16, 2), Block.box(5, 0, 12, 11, 16, 16)),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(0, 0, 5, 4, 16, 11), Block.box(14, 0, 6, 16, 16, 10)),
			Shapes.or(BASE_COMPLETE_SHAPE, Block.box(5, 0, 0, 11, 16, 4), Block.box(6, 0, 14, 10, 16, 16))
	};

	public BrickCrucible(Properties p)
	{
		super(p);
		registerDefaultState(defaultBlockState().setValue(MULTIBLOCK_COMPLETE, false).setValue(FACING, Direction.NORTH).setValue(LIT, false));
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
			var facing = state.getValue(FACING);
			var be = level.getBlockEntity(pos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).orElseThrow();
			var castingVessel = level.getCapability(CastingCapability.BLOCK, pos.relative(facing).below(), Direction.UP);
			if (state.getValue(MULTIBLOCK_COMPLETE))
			{
				if (pHitResult.getDirection() == facing && castingVessel != null)
					be.pour(castingVessel);
				else
					player.openMenu(state.getMenuProvider(level, pos));
			}
			else if (Multiblocks.BRICK_CRUCIBLE.isValid(level, pos, facing))
			{
				Multiblocks.BRICK_CRUCIBLE.build(level, pos, facing);
				level.setBlock(pos, state.setValue(MULTIBLOCK_COMPLETE, true), 2);
			}
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
		if (level.isClientSide || !pState.getValue(MULTIBLOCK_COMPLETE))
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_CRUCIBLE_TYPE.get(), beType);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter levelIn, BlockPos pos, CollisionContext context)
	{
		return state.getValue(MULTIBLOCK_COMPLETE) ? COMPLETE_SHAPE[state.getValue(FACING).get2DDataValue()] : INCOMPLETE_SHAPE[state.getValue(FACING).get2DDataValue() % 2];
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(MULTIBLOCK_COMPLETE, FACING, LIT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		if (!pState.is(pNewState.getBlock()))
			pLevel.getBlockEntity(pPos, FABETypes.BRICK_CRUCIBLE_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (state.getValue(LIT))
		{
			var d0 = pos.getX() + 0.5;
			var d1 = pos.getY() - 1;
			var d2 = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1)
				level.playLocalSound(d0, d1, d2, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.8F, false);

			var direction = state.getValue(FACING).getClockWise();
			var direction$axis = direction.getAxis();
			var d4 = random.nextDouble() * 0.6 - 0.3;
			var d5 = direction$axis == Direction.Axis.X ? direction.getStepX() * 0.52 : d4;
			var d6 = random.nextDouble() * 6.0 / 16.0;
			var d7 = direction$axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : d4;
			level.addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
			// level.addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, 0.0, 0.0, 0.0);
		}
	}
}
