package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ArabicaLeaves extends Block implements SimpleWaterloggedBlock, BonemealableBlock
{
	public static final IntegerProperty HEIGHT = IntegerProperty.create("height", 1, 3);
	public static final IntegerProperty AGE = BlockStateProperties.AGE_2;
	public static final BooleanProperty PERSISTENT = BlockStateProperties.PERSISTENT;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	public ArabicaLeaves(Properties properties)
	{
		super(properties);
		registerDefaultState(defaultBlockState().setValue(PERSISTENT, true).setValue(WATERLOGGED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(HEIGHT, AGE, PERSISTENT, WATERLOGGED);
	}

	@Override
	protected FluidState getFluidState(BlockState state)
	{
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		var age = state.getValue(AGE);
		if (age == 2)
		{
			var dropCount = 1 + level.random.nextInt(2);
			popResource(level, pos, new ItemStack(FAItems.COFFEE_CHERRY.get(), dropCount));
			level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F); // TODO: change sound
			var blockState = state.setValue(AGE, 0);
			level.setBlock(pos, blockState, 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		else
			return InteractionResult.PASS;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (level.isRainingAt(pos.above()) && random.nextInt(15) == 1)
		{
			var blockpos = pos.below();
			var blockstate = level.getBlockState(blockpos);
			if (!blockstate.canOcclude() || !blockstate.isFaceSturdy(level, blockpos, Direction.UP))
				ParticleUtils.spawnParticleBelow(level, pos, random, ParticleTypes.DRIPPING_WATER);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		var fluidstate = context.getLevel().getFluidState(context.getClickedPos());
		return defaultBlockState().setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
	}

	@Override
	protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter reader, BlockPos pos)
	{
		return Shapes.empty();
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state)
	{
		return !state.getValue(PERSISTENT);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state)
	{
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state)
	{
		var height = state.getValue(HEIGHT);
		var stemPos = pos.below(height);
		var stem = level.getBlockState(stemPos);
		if (stem.getBlock() instanceof BonemealableBlock bb)
			bb.performBonemeal(level, random, stemPos, stem);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
	{
		if (state.getValue(PERSISTENT))
			return true;
		var below = level.getBlockState(pos.below());
		return (below.is(this) || below.is(FABlocks.ARABICA_STEM)) && level.getBlockState(pos.below(state.getValue(HEIGHT))).is(FABlocks.ARABICA_STEM);
	}

	@Override
	protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos)
	{
		if (!state.canSurvive(level, pos))
			return Blocks.AIR.defaultBlockState();
		else
		{
			if (state.getValue(WATERLOGGED))
				level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
			return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
		}
	}
}
