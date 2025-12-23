package boblovespi.factoryautomation.mixin;

import boblovespi.factoryautomation.common.block.FABlocks;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FarmBlock.class)
public abstract class FarmBlockMixin
{
	@Inject(method = "canSurvive", at = @At("RETURN"), cancellable = true)
	private void givenCanSurvive(BlockState state, LevelReader level, BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) BlockState state1)
	{
		if (!cir.getReturnValue() && state1.is(FABlocks.ARABICA_STEM))
			cir.setReturnValue(true);
	}
}
