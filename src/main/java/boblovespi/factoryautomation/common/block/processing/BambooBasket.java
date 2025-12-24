package boblovespi.factoryautomation.common.block.processing;

import boblovespi.factoryautomation.common.blockentity.FABE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.blockentity.ITickable;
import boblovespi.factoryautomation.common.blockentity.processing.BambooBasketBE;
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

public class BambooBasket extends Block implements EntityBlock
{
	public static VoxelShape BOUNDING_BOX = Block.box(1, 0, 1, 15, 2, 15);

	public BambooBasket(Properties properties)
	{
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BambooBasketBE(pos, state);
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
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState pState, BlockEntityType<T> beType)
	{
		if (level.isClientSide)
			return null;
		return ITickable.makeTicker(FABETypes.BAMBOO_BASKET_TYPE.get(), beType);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.CONSUME;
		return level.getBlockEntity(pos, FABETypes.BAMBOO_BASKET_TYPE.get()).map(b ->
		{
			var hitPos = hitResult.getLocation().subtract(pos.getBottomCenter());
			var dist = Math.abs(hitPos.x) + Math.abs(hitPos.z);
			var index = 0;
			if (hitPos.x < 0)
				index |= 0b10;
			if (hitPos.z < 0)
				index |= 0b01;
			if (dist < 3 / 16f)
				index = 4;
			var success = b.takeOrPlace(stack, player, index);
			if (success)
				return ItemInteractionResult.SUCCESS;
			return ItemInteractionResult.CONSUME;
		}).orElse(ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION);
	}

	@Override
	protected void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston)
	{
		pLevel.getBlockEntity(pPos, FABETypes.BRICK_MAKER_FRAME_TYPE.get()).ifPresent(FABE::onDestroy);
		super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
	}
}
