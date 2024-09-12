package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.processing.ChoppingBlockBE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class ChoppingBlock extends Block implements EntityBlock
{
	private static final VoxelShape BOUNDING_BOX = Block.box(0, 0, 0, 16, 8, 16);

	public ChoppingBlock(Properties p)
	{
		super(p);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new ChoppingBlockBE(pPos, pState);
	}

	@Override
	protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext)
	{
		return BOUNDING_BOX;
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level level, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		level.getBlockEntity(pPos, FABETypes.CHOPPING_BLOCK_TYPE.get()).ifPresent(b -> b.takeOrPlace(pStack, pPlayer));
		return ItemInteractionResult.CONSUME;
	}

	@Override
	protected void attack(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer)
	{
		if (pLevel.isClientSide)
			return;
		pLevel.getBlockEntity(pPos, FABETypes.CHOPPING_BLOCK_TYPE.get()).ifPresent(b -> b.leftClick(pPlayer, pPlayer.getMainHandItem()));
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		pLevel.getBlockEntity(pPos, FABETypes.CHOPPING_BLOCK_TYPE.get()).ifPresent(ChoppingBlockBE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}
}
