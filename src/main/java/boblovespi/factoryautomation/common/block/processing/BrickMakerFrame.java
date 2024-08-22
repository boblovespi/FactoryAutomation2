package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.BrickMakerFrameBE;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BrickMakerFrame extends Block implements EntityBlock
{
	public static VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 6, 16);

	public BrickMakerFrame(Properties p)
	{
		super(p);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new BrickMakerFrameBE(pPos, pState);
	}
}
