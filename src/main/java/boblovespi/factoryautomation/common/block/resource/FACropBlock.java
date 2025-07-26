package boblovespi.factoryautomation.common.block.resource;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BeetrootBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

public abstract class FACropBlock extends BeetrootBlock
{
	public FACropBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected abstract ItemLike getBaseSeedId();

	@Override
	protected int getBonemealAgeIncrease(Level level)
	{
		return 1;
	}

	@Override
	protected abstract VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context);

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
