package boblovespi.factoryautomation.common.block.logistics;

import boblovespi.factoryautomation.common.blockentity.logistics.SmallTankBE;
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
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.Nullable;

public class SmallTank extends Block implements EntityBlock
{
	public final int capacity;

	public SmallTank(Properties properties, int capacity)
	{
		super(properties);
		this.capacity = capacity;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new SmallTankBE(pos, state);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult)
	{
		if (level.isClientSide)
			return ItemInteractionResult.SUCCESS;
		FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection());
		return ItemInteractionResult.CONSUME;
	}
}
