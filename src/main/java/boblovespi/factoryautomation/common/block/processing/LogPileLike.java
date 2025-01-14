package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.recipe.LogPileFiringRecipe;
import boblovespi.factoryautomation.common.recipe.RecipeThings;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
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
	private final boolean hasRecipes;

	public LogPileLike(Properties properties, int processTime, boolean hasRecipes)
	{
		super(properties);
		this.processTime = processTime;
		this.hasRecipes = hasRecipes;
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
		if (activated && hasRecipes)
		{
			for (var dir : Direction.values())
			{
				var offset = pos.relative(dir);
				var targetBlock = level.getBlockState(offset).getBlock();
				var input = new LogPileFiringRecipe.Input(new ItemStack(targetBlock), this);
				var recipe = level.getRecipeManager()
								  .getRecipeFor(RecipeThings.LOG_PILE_FIRING_TYPE.get(), input, level);
				recipe.ifPresent(r -> {
					var foundPile = false;
					var out = r.value().assemble(input, level.registryAccess());
					var outState = ((BlockItem) out.getItem()).getBlock().defaultBlockState();
					for (var i = 4; i >= 0; i--)
					{
						var next = offset.relative(dir, i);
						var state2 = level.getBlockState(next);
						if (foundPile)
						{
							if (state2.is(targetBlock) && isSurrounded(level, next, targetBlock, outState.getBlock()))
								level.setBlockAndUpdate(next, outState);
						}
						else if (state2.is(this) && state2.getValue(ACTIVATED))
							foundPile = true;
					}
				});
			}
		}
		if (activated)
			level.setBlockAndUpdate(pos, getResultState());
	}

	private boolean isSurrounded(ServerLevel level, BlockPos pos, Block target, Block output)
	{
		for (var dir : Direction.values())
		{
			var offset = pos.relative(dir);
			var state = level.getBlockState(offset);
			if (!state.is(this) && !state.is(target) && !state.is(output) && !isValidSurroundingBlock(level, state, offset, dir.getOpposite()))
				return false;
		}
		return true;
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
