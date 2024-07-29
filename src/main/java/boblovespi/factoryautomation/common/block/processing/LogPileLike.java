package boblovespi.factoryautomation.common.block.processing;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public abstract class LogPileLike extends Block
{
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	private final int processTime;

	public LogPileLike(Properties properties, int processTime)
	{
		super(properties);
		this.processTime = processTime;
		registerDefaultState(defaultBlockState().setValue(ACTIVATED, false));
	}

	public abstract BlockState getResultState();

	public abstract boolean isValidSurroundingBlock(Level level, BlockState state, BlockPos pos, Direction face);

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return state.getValue(ACTIVATED) ? 20 : 8;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction)
	{
		return state.getValue(ACTIVATED) ? 120 : 20;
	}

	@Override
	public boolean isBurning(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.getValue(ACTIVATED);
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		var activated = state.getValue(ACTIVATED);
		if (activated)
		{
			level.setBlockAndUpdate(pos, getResultState());
			// TODO: add map for various firing conversions, add logic for various firing conversions
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder)
	{
		pBuilder.add(ACTIVATED);
	}

	@Override
	protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean piston)
	{
		var neighborState = level.getBlockState(neighborPos);
		if (!state.getValue(ACTIVATED))
		{
			if (neighborState.is(Blocks.FIRE) || (neighborState.is(this) && neighborState.getValue(ACTIVATED)))
			{
				level.setBlockAndUpdate(pos, state.setValue(ACTIVATED, true));
				level.scheduleTick(pos, this, processTime);
			}
		}
		else
		{
			var sidesOnFire = false;
			var isSurrounded = true;
			for (Direction face : Direction.values())
			{
				var offset = pos.relative(face);
				var state1 = level.getBlockState(offset);
				if (state1.isAir())
				{
					level.setBlockAndUpdate(offset, Blocks.FIRE.defaultBlockState());
					sidesOnFire = true;
				}
				else if (state1.is(Blocks.FIRE))
					sidesOnFire = true;
				else if (!isValidSurroundingBlock(level, state1, offset, face.getOpposite()) && !state1.is(this))
					isSurrounded = false;
			}
			if (!sidesOnFire && !isSurrounded)
				level.setBlockAndUpdate(pos, Blocks.FIRE.defaultBlockState());
		}
	}

	@Override
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource r)
	{
		if (!state.getValue(ACTIVATED))
			return;
		double x = pos.getX() + r.nextDouble();
		double y = pos.getY() + r.nextDouble();
		double z = pos.getZ() + r.nextDouble();
		world.addParticle(ParticleTypes.LAVA, x, y, z, r.nextDouble() / 20, r.nextDouble() / 20, r.nextDouble() / 20);
		world.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, r.nextDouble() / 20, 0.05, r.nextDouble() / 20);
		world.addParticle(ParticleTypes.SMOKE, x, y + 1.5, z, r.nextDouble() / 20, 0.05, r.nextDouble() / 20);
	}
}
