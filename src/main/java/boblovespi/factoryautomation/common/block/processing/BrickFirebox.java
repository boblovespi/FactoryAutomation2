package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.BrickFireboxBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
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
import org.jetbrains.annotations.Nullable;

public class BrickFirebox extends Block implements EntityBlock
{
	public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
	public static final BooleanProperty LIT = BlockStateProperties.LIT;

	public BrickFirebox(Properties p)
	{
		super(p);
		registerDefaultState(defaultBlockState().setValue(FACING, Direction.NORTH).setValue(LIT, false));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new BrickFireboxBE(pPos, pState);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, LIT);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context)
	{
		return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_FIREBOX_TYPE.get(), beType);
	}

	@Override
	protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston)
	{
		if (!state.is(newState.getBlock()))
			level.getBlockEntity(pos, FABETypes.BRICK_FIREBOX_TYPE.get()).ifPresent(BrickFireboxBE::onDestroy);
		super.onRemove(state, level, pos, newState, movedByPiston);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random)
	{
		if (state.getValue(LIT))
		{
			var lowerX = pos.getX() + 0.5;
			var lowerY = pos.getY();
			var lowerZ = pos.getZ() + 0.5;
			if (random.nextDouble() < 0.1)
				level.playLocalSound(lowerX, lowerY, lowerZ, SoundEvents.BLASTFURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 0.8F, false);

			var direction = state.getValue(FACING);
			var axis = direction.getAxis();
			var d4 = random.nextDouble() * 0.6 - 0.3;
			var d5 = axis == Direction.Axis.X ? direction.getStepX() * 0.52 : d4;
			var d6 = random.nextDouble() * 6.0 / 16.0;
			var d7 = axis == Direction.Axis.Z ? direction.getStepZ() * 0.52 : d4;
			if (random.nextFloat() < 0.6f)
				level.addParticle(ParticleTypes.SMOKE, lowerX + d5, lowerY + d6, lowerZ + d7, 0.0, 0.0, 0.0);
			// level.addParticle(ParticleTypes.FLAME, lowerX + d5, lowerY + d6, lowerZ + d7, 0.0, 0.0, 0.0);
		}
	}
}
