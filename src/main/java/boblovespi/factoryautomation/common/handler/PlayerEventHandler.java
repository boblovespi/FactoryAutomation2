package boblovespi.factoryautomation.common.handler;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

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
					player.hurt(level.damageSources().generic(), 1);
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


}
