package boblovespi.factoryautomation.common.block.logistics;

import boblovespi.factoryautomation.common.blockentity.logistics.SmallTankBE;
import boblovespi.factoryautomation.common.fluid.FAFluids;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.ItemHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
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
		if (!FluidUtil.interactWithFluidHandler(player, hand, level, pos, hitResult.getDirection()))
		{
			if (stack.is(Items.GLASS_BOTTLE))
			{
				FluidUtil.getFluidHandler(level, pos, hitResult.getDirection()).ifPresent(h -> {
					var fluid = h.getFluidInTank(0);
					if (player.getAbilities().instabuild)
						h.drain(250, IFluidHandler.FluidAction.EXECUTE);
					else if (!fluid.isEmpty() && fluid.getAmount() >= 250)
					{
						if (fluid.is(Fluids.WATER))
						{
							extractBottle(stack, level, player, hand, Items.POTION.getDefaultInstance());
							h.drain(250, IFluidHandler.FluidAction.EXECUTE);
						}
						if (fluid.is(Tags.Fluids.HONEY))
						{
							extractBottle(stack, level, player, hand, Items.HONEY_BOTTLE.getDefaultInstance());
							h.drain(250, IFluidHandler.FluidAction.EXECUTE);
						}
						if (fluid.is(FAFluids.PANCAKE_BATTER_SOURCE))
						{
							extractBottle(stack, level, player, hand, FAItems.PANCAKE_BATTER_BOTTLE.toStack());
							h.drain(250, IFluidHandler.FluidAction.EXECUTE);
						}
					}
				});
			}
		}
		return ItemInteractionResult.CONSUME;
	}

	private static void extractBottle(ItemStack stack, Level level, Player player, InteractionHand hand, ItemStack filledBottle)
	{
		if (stack.getCount() == 1)
			player.setItemInHand(hand, filledBottle);
		else
		{
			stack.shrink(1);
			ItemHelper.putItemsInInventoryOrDrop(player, filledBottle, level);
		}
	}
}
