package boblovespi.factoryautomation.mixin;

import boblovespi.factoryautomation.common.FAAttachmentTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin
{
	@Shadow private boolean isDestroying;

	@Shadow @Final private Minecraft minecraft;

	// @Inject(method = "stopDestroyBlock", at = @At("HEAD"))
	// private void onStopDestroyingBlock(CallbackInfo ci)
	// {
	// 	if (isDestroying)
	// 	{
	// 		FactoryAutomation.LOGGER.info("on stop destroying block");
	// 		minecraft.player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).isMining = false;
	// 	}
	// }
	//
	// @Inject(method = "continueDestroyBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;startPrediction(Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/client/multiplayer/prediction/PredictiveAction;)V", ordinal = 1))
	// private void onContinueDestroyingBlock(BlockPos posBlock, Direction directionFacing, CallbackInfoReturnable<Boolean> cir)
	// {
	// 	FactoryAutomation.LOGGER.info("on continue destroying block");
	// 	minecraft.player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).isMining = false;
	// }

	@Inject(method = "destroyBlock", at = @At(value = "RETURN"))
	private void onDestroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir)
	{
		if (cir.getReturnValue())
			minecraft.player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).breakBlock();
	}
}
