package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FAAttachmentTypes;
import boblovespi.factoryautomation.common.FADamageTypes;
import boblovespi.factoryautomation.common.potion.FAMobEffects;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = FactoryAutomation.MODID)
public class PlayerEventHandler
{
	@SubscribeEvent
	public static void onLeftClickBlockEvent(PlayerInteractEvent.LeftClickBlock event)
	{
		var level = event.getLevel();
		var pos = event.getPos();
		var state = level.getBlockState(pos);
		var player = event.getEntity();
		var tool = event.getItemStack();

		if (state.is(BlockTags.LOGS))
		{
			if (!tool.canPerformAction(ItemAbilities.AXE_DIG))
			{
				if (!level.isClientSide)
					player.hurt(FADamageTypes.punchingWood(level.registryAccess()), 1);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerHarvestEvent(PlayerEvent.HarvestCheck event)
	{
		var level = event.getLevel();
		var pos = event.getPos();
		var state = level.getBlockState(pos);
		var player = event.getEntity();
		var tool = player.getMainHandItem();

		if (state.is(BlockTags.LOGS))
		{
			if (!tool.canPerformAction(ItemAbilities.AXE_DIG))
			{
				event.setCanHarvest(false);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerBreakSpeedEvent(PlayerEvent.BreakSpeed event)
	{
		var state = event.getState();
		var player = event.getEntity();
		var tool = player.getMainHandItem();
		var focused = player.getEffect(FAMobEffects.FOCUSED);
		if (focused != null)
		{
			var amplifier = focused.getAmplifier();
			var newSpeed = event.getOriginalSpeed() * (1 + (amplifier + 1) * player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).getBlockBrokenCount(amplifier) / 20f);
			// FactoryAutomation.LOGGER.info("speed/ns: {}; {}", event.getOriginalSpeed(), newSpeed);
			event.setNewSpeed(newSpeed);
		}
	}
}
