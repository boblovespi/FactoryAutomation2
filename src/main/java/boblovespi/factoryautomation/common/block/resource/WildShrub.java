package boblovespi.factoryautomation.common.block.resource;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WildShrub extends BushBlock
{
	public static final MapCodec<WildShrub> CODEC = simpleCodec(WildShrub::new);
	private static final VoxelShape SHAPE = Block.box(2, 0, 2, 14, 13, 14);

	@Override
	public MapCodec<WildShrub> codec()
	{
		return CODEC;
	}

	public WildShrub(BlockBehaviour.Properties properties)
	{
		super(properties);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context)
	{
		return SHAPE;
	}

	@Override
	protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos)
	{
		return state.is(BlockTags.DIRT);
	}
}
