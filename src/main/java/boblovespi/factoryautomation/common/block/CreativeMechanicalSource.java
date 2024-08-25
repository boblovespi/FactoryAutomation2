package boblovespi.factoryautomation.common.block;

import boblovespi.factoryautomation.common.blockentity.CreativeMechanicalSourceBE;
import boblovespi.factoryautomation.common.blockentity.FABETypes;
import boblovespi.factoryautomation.common.item.tool.Tools;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class CreativeMechanicalSource extends Block implements EntityBlock
{
	public CreativeMechanicalSource(Properties p_49795_)
	{
		super(p_49795_);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState)
	{
		return new CreativeMechanicalSourceBE(pPos, pState);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult)
	{
		if (stack.canPerformAction(Tools.WRENCH))
		{
			if (level.isClientSide)
				return ItemInteractionResult.SUCCESS;
			var mainHand = pHand == InteractionHand.MAIN_HAND;
			var val = 10 * ((float) pHitResult.getLocation().y - pos.getY() - 0.5f);
			if (mainHand)
				level.getBlockEntity(pos, FABETypes.CREATIVE_MECHANICAL_SOURCE_TYPE.get()).ifPresent(s -> s.changeSpeed(val));
			else
				level.getBlockEntity(pos, FABETypes.CREATIVE_MECHANICAL_SOURCE_TYPE.get()).ifPresent(s -> s.changeTorque(val));
			return ItemInteractionResult.CONSUME;
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}
}
