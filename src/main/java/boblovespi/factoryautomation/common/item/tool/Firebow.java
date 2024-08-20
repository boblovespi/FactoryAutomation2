package boblovespi.factoryautomation.common.item.tool;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public class Firebow extends Item
{
	public Firebow(Properties pProperties)
	{
		super(pProperties.durability(15));
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if (context.getPlayer() != null)
			context.getPlayer().startUsingItem(context.getHand());
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		player.startUsingItem(hand);
		return InteractionResultHolder.pass(player.getItemInHand(hand));
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity living)
	{
		if (!(living instanceof Player player))
			return stack;
		var rayTrace = getPlayerPOVHitResult(world, player, ClipContext.Fluid.NONE);

		if (rayTrace.getType() != HitResult.Type.BLOCK)
			return stack;
		var pos = rayTrace.getBlockPos();
		var facing = rayTrace.getDirection();
		var ctx = new UseOnContext(player, player.getUsedItemHand(), rayTrace);
		var newState = world.getBlockState(pos).getToolModifiedState(ctx, ItemAbilities.FIRESTARTER_LIGHT, false);
		if (newState != null)
		{
			if (player.mayUseItemAt(pos, facing, stack))
			{
				world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
				world.setBlock(pos, newState, 11);
				world.gameEvent(player, GameEvent.BLOCK_CHANGE, pos);
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
			}
		}
		else
		{
			pos = pos.relative(facing);
			if (player.mayUseItemAt(pos, facing, stack))
			{
				if (BaseFireBlock.canBePlacedAt(world, pos, facing.getOpposite()))
				{
					world.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
					world.setBlock(pos, BaseFireBlock.getState(world, pos), 11);
					world.gameEvent(player, GameEvent.BLOCK_PLACE, pos);
				}

				if (player instanceof ServerPlayer)
					CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
				stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(player.getUsedItemHand()));
			}
		}
		return stack;
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ItemAbility itemAbility)
	{
		return itemAbility == ItemAbilities.FIRESTARTER_LIGHT;
	}

	@Override
	public int getUseDuration(ItemStack pStack, LivingEntity pEntity)
	{
		return 60;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack pStack)
	{
		return UseAnim.BOW;
	}
}
