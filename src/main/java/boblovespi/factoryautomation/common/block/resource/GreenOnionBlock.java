package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public class GreenOnionBlock extends BeetrootBlock
{
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(0.0, 0.0, 0.0, 16.0, 3, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 12, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 15, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 16, 16.0)
	};

	public GreenOnionBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId()
	{
		return FAItems.GREEN_ONION;
	}

	@Override
	protected int getBonemealAgeIncrease(Level level)
	{
		return 1;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[getAge(state)];
	}

	@Override
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random)
	{
		if (!level.isAreaLoaded(pos, 1))
			return;
		if (level.getRawBrightness(pos, 0) >= 9)
		{
			int age = getAge(state);
			if (age == 2)
				super.randomTick(state, level, pos, random);
			else if (age < getMaxAge())
			{
				float growthSpeed = getGrowthSpeed(state, level, pos);
				if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int) (12f / growthSpeed) + 1) == 0))
				{
					level.setBlock(pos, getStateForAge(age + 1), 2);
					CommonHooks.fireCropGrowPost(level, pos, state);
				}
			}
		}
	}
}
