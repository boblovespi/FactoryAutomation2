package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.item.FAItems;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
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
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class TeaShrub extends BushBlock implements BonemealableBlock
{
	public static final int MAX_AGE = 5;
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(6, 0, 6, 10, 5, 10),
			Block.box(4, 0, 4, 12, 8, 12),
			Block.box(2, 0.0, 2, 14, 10, 14),
			Block.box(2, 0, 2, 14, 12, 14),
			Shapes.or(Block.box(5, 0, 5, 11, 5, 11), Block.box(0, 5, 0, 16, 16, 16)),
			Shapes.or(Block.box(5, 0, 7, 11, 5, 11), Block.box(0, 5, 0, 16, 16, 16))
	};
	private static final List<Vec3i> OFFSETS = List.of(new Vec3i(-1, 0, 1), new Vec3i(0, 0, 1), new Vec3i(1, 0, 1), new Vec3i(1, 0, 0));

	public TeaShrub(Properties properties)
	{
		super(properties);
	}

	@Override
	protected boolean isRandomlyTicking(BlockState state)
	{
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult)
	{
		if (state.getValue(AGE) == MAX_AGE)
		{
			var dropCount = 1 + level.random.nextInt(2);
			popResource(level, pos, new ItemStack(FAItems.TEA_LEAF.get(), dropCount));
			if (level.random.nextFloat() < 0.05f)
				popResource(level, pos, new ItemStack(FAItems.TEA_SEEDS.get()));
			level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, SoundSource.BLOCKS, 1.0F, 0.8F + level.random.nextFloat() * 0.4F); // TODO: change sound
			var blockState = state.setValue(AGE, MAX_AGE - 1);
			level.setBlock(pos, blockState, 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		else
			return InteractionResult.PASS;
	}

	@Override
	protected MapCodec<? extends BushBlock> codec()
	{
		return simpleCodec(TeaShrub::new);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return state.getValue(AGE) < MAX_AGE - 1 ? Shapes.empty() : Shapes.block();
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player)
	{
		return new ItemStack(FAItems.TEA_SEEDS.get());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
	{
		builder.add(AGE);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state)
	{
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state)
	{
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state)
	{
		if (state.getValue(AGE) < MAX_AGE - 1)
			grow(level, state, pos, random, 0);
		else if (state.getValue(AGE) != MAX_AGE)
		{
			var count = 0;
			for (var o : OFFSETS)
			{
				if (!level.getBlockState(pos.offset(o)).isAir())
					count++;
				if (!level.getBlockState(pos.subtract(o)).isAir())
					count++;
			}
			grow(level, state, pos, random, count);
		}
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
			var count = 0;
			if (hasARow)
				speed *= 2;
			for (var o : OFFSETS)
			{
				var posB = level.getBlockState(pos.offset(o));
				var negB = level.getBlockState(pos.subtract(o));
				if (hasARow && (posB.is(this) || negB.is(this)))
					speed /= 2;
				if (!posB.isAir())
					count++;
				if (!negB.isAir())
					count++;
			}

			if (random.nextInt(100) < speed)
				grow(level, state, pos, random, count);
		}
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.getBlock() instanceof FarmBlock;
	}

	private void grow(ServerLevel level, BlockState state, BlockPos pos, RandomSource random, int count)
	{
		var newAge = Math.min(MAX_AGE, state.getValue(AGE) + 1);
		if (random.nextDouble() < 0.5)
			return;
		if (newAge < MAX_AGE || count < 4)
		{
			level.setBlock(pos, state.setValue(AGE, newAge), 2);
			level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(state));
		}
	}
}
