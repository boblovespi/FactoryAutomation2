package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class SoybeanCrop extends CropBlock
{
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(1, 0.0, 1, 15, 2, 15),
			Block.box(1, 0.0, 1, 15, 4, 15),
			Block.box(1, 0.0, 1, 15, 7, 15),
			Block.box(1, 0.0, 1, 15, 10, 15),
			Block.box(0.0, 0.0, 0.0, 16.0, 13, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 16, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 16, 16.0),
			Block.box(0.0, 0.0, 0.0, 16.0, 16, 16.0)
	};

	public SoybeanCrop(Properties properties)
	{
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId()
	{
		return FAItems.SOYBEANS;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[state.getValue(AGE)];
	}
}
