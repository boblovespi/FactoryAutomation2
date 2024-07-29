package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.types.WoodTypes;
import boblovespi.factoryautomation.common.item.tool.Tools;
import boblovespi.factoryautomation.common.sound.FASounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = FactoryAutomation.MODID)
public class BlockEventHandler
{
	@SubscribeEvent
	public static void onRightClickBlockEvent(PlayerInteractEvent.RightClickBlock event)
	{
		var stack = event.getItemStack();
		var level = event.getLevel();
		var pos = event.getHitVec().getBlockPos();
		var state = level.getBlockState(pos);
		var isSneaking = event.getEntity().isSecondaryUseActive();

		if (stack.canPerformAction(Tools.MAKE_CHOPPING_BLOCK) && isSneaking)
		{
			if (state.is(FATags.Blocks.CHOPPING_BLOCK_LOGS))
			{
				var newState = FABlocks.CHOPPING_BLOCKS.get(WoodTypes.fromLog(state.getBlock())).get().defaultBlockState();
				level.setBlockAndUpdate(pos, newState);
				level.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(event.getEntity(), newState));
				level.playSound(event.getEntity(), pos, FASounds.MAKE_CHOPPING_BLOCK.get(), SoundSource.BLOCKS);
				event.setCanceled(true);
			}
		}
	}
}
