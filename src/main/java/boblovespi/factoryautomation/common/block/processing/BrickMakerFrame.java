package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.*;
import boblovespi.factoryautomation.common.blockentity.processing.BrickMakerFrameBE;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
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

	@Override
	public VoxelShape getOcclusionShape(BlockState state, BlockGetter levelIn, BlockPos pos)
	{
		return Shapes.empty();
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new BrickMakerFrameBE(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return null;
		return ITickable.makeTicker(FABETypes.BRICK_MAKER_FRAME_TYPE.get(), beType);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack pStack, BlockState pState, Level level, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		var hitPoint = pHitResult.getLocation().x - pHitResult.getBlockPos().getX();
		level.getBlockEntity(pPos, FABETypes.BRICK_MAKER_FRAME_TYPE.get()).ifPresent(b -> b.takeOrPlace(pStack, pPlayer, hitPoint > 0.5 ? 1 : 0));
		return ItemInteractionResult.CONSUME;
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		pLevel.getBlockEntity(pPos, FABETypes.BRICK_MAKER_FRAME_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}
}
