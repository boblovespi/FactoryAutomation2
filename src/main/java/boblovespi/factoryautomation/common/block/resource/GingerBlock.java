package boblovespi.factoryautomation.common.block.resource;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GingerBlock extends FACropBlock
{
	private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[] {
			Block.box(6, 0.0, 6, 10, 5, 10),
			Block.box(3, 0.0, 3, 13, 10, 13),
			Block.box(3, 0.0, 0.0, 16.0, 14, 13),
			Block.box(2, 0.0, 0.0, 16.0, 16, 14)
	};

	public GingerBlock(Properties properties)
	{
		super(properties);
	}

	@Override
	protected ItemLike getBaseSeedId()
	{
		return FAItems.GINGER;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE_BY_AGE[getAge(state)];
	}
}
