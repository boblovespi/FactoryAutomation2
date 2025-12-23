package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class ArabicaStem extends BushBlock implements BonemealableBlock
{
	// age 0 = sprout, 1 = sapling, 2 = shrub, 3 = flowering / cherry
	public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(2, 0.0, 2, 14, 9, 14),
			Block.box(2, 0.0, 2, 14, 12, 14),
			Shapes.or(Block.box(7, 0, 7, 9, 3, 9), Block.box(0, 3, 0, 16, 16, 16)),
			Shapes.or(Block.box(7, 0, 7, 9, 3, 9), Block.box(0, 3, 0, 16, 16, 16))
	};
	private static final List<Vec3i> OFFSETS = List.of(new Vec3i(-1, 0, 1), new Vec3i(0, 0, 1), new Vec3i(1, 0, 1), new Vec3i(1, 0, 0));

	public ArabicaStem(Properties properties)
	{
		super(properties);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state)
	{
		return true;
	}

	@Override
	protected MapCodec<? extends BushBlock> codec()
	{
		return simpleCodec(ArabicaStem::new);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return state.getValue(AGE) < 2 ? Shapes.empty() : Shapes.block();
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
	{
		return new ItemStack(FAItems.COFFEE_CHERRY.get());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AGE);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state)
	{
		return true;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state)
	{
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state)
	{
		grow(level, state, pos, random);
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if (level.getRawBrightness(pos, 0) >= 9)
		{
			var speed = 5;
			var below = pos.below();
			if (level.getBlockState(below).isFertile(level, below))
				speed += 20;
			var hasARow = false;
			for (var o : OFFSETS)
			{
				if (level.getBlockState(pos.offset(o)).is(this) && level.getBlockState(pos.subtract(o)).is(this))
				{
					hasARow = true;
					break;
				}
			}
			if (hasARow)
			{
				speed *= 2;
				for (var o : OFFSETS)
					if (level.getBlockState(pos.offset(o)).is(this) || level.getBlockState(pos.subtract(o)).is(this))
						speed /= 2;
			}
			if (random.nextInt(100) < speed)
				grow(level, state, pos, random);
		}
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.getBlock() instanceof FarmBlock;
	}

	private void grow(ServerLevel level, BlockState state, BlockPos pos, RandomSource random)
	{
		var age = (int) state.getValue(AGE);
		if (random.nextDouble() < 0.5)
			return;
		if (age < 2)
		{
			level.setBlock(pos, state.setValue(AGE, age + 1), 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
		}
		else if (age == 2)
		{
			var h = 0;
			var newPos = pos;
			do
			{
				newPos = newPos.above();
				h++;
			} while (level.getBlockState(newPos).is(FABlocks.ARABICA_LEAVES) && h < 3);
			if (level.getBlockState(newPos).isAir())
			{
				level.setBlock(newPos, FABlocks.ARABICA_LEAVES.get().defaultBlockState().setValue(ArabicaLeaves.HEIGHT, h).setValue(ArabicaLeaves.PERSISTENT, false), 2);
				if (h == 3 || random.nextDouble() < 1d / (4 - h))
					level.setBlock(pos, state.setValue(AGE, 3), 2);
			}
			else
				level.setBlock(pos, state.setValue(AGE, 3), 2);
		}
		else if (age == 3)
		{
			var newPos = pos.above();
			for (var i = 0; i < 3; i++)
			{
				var leafState = level.getBlockState(newPos);
				if (!leafState.is(FABlocks.ARABICA_LEAVES) || leafState.getValue(ArabicaLeaves.PERSISTENT))
					return;
				var leafAge = Math.min(2, leafState.getValue(ArabicaLeaves.AGE) + 1);
				level.setBlock(newPos, leafState.setValue(ArabicaLeaves.AGE, leafAge), 2);
				newPos = newPos.above();
			}
		}
	}
}
