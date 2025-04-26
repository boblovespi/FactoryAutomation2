package boblovespi.factoryautomation.client;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.blockentity.processing.StoneCastingVesselBE;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = FactoryAutomation.MODID, value = Dist.CLIENT)
public class ClientGameHandler
{
	@SubscribeEvent
	public static void onClientTick(ClientTickEvent.Pre event)
	{
		PartialTickHelper.tick();
	}

	public static final ResourceLocation THERMO_MOLTEN = FactoryAutomation.name("textures/gui/hud/thermometer/thermo_molten.png");
	public static final ResourceLocation THERMO_HOT = FactoryAutomation.name("textures/gui/hud/thermometer/thermo_hot.png");
	public static final ResourceLocation THERMO_WARM = FactoryAutomation.name("textures/gui/hud/thermometer/thermo_warm.png");
	public static final ResourceLocation THERMO_MODERATE = FactoryAutomation.name("textures/gui/hud/thermometer/thermo_moderate.png");
	public static final ResourceLocation THERMO_COLD = FactoryAutomation.name("textures/gui/hud/thermometer/thermo_cold.png");
	@SubscribeEvent
	public static void asd(RenderGuiEvent.Post event){
		Minecraft mc = Minecraft.getInstance();
		Player player = mc.player;
		HitResult result = mc.hitResult;

		if(result instanceof BlockHitResult bhr){
			BlockPos hitpos = bhr.getBlockPos();
			BlockState stateAt = player.level().getBlockState(hitpos);

			if(stateAt.getBlock() == FABlocks.STONE_CASTING_VESSEL.get()){
				BlockEntity blockEntity = player.level().getBlockEntity(hitpos);
				if(blockEntity instanceof StoneCastingVesselBE vesselBE){
					if(!vesselBE.isEmpty()){
						Window res = mc.getWindow();
						GuiGraphics guiGraphics = event.getGuiGraphics();
						PoseStack matrix = event.getGuiGraphics().pose();

						RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
						ResourceLocation texture = THERMO_COLD;
						float temp = vesselBE.getTemp() + 300;
						if(temp > 313) texture = THERMO_MODERATE;
						if(temp > 500) texture = THERMO_WARM;
						if(temp > 690) texture = THERMO_HOT;
						if(temp > 900) texture = THERMO_MOLTEN;
						matrix.pushPose();
						int x = (res.getGuiScaledWidth() - 15) / 2;
						int y = (res.getGuiScaledHeight() - 15) / 2;
						guiGraphics.blit(texture, x-8, y, 0, 0, 16, 16, 16, 16);

						matrix.popPose();
						RenderSystem.defaultBlendFunc();
					}
				}
			}
		}
	}
}
