package boblovespi.factoryautomation.mixin;

import boblovespi.factoryautomation.common.potion.FAMobEffects;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Projectile.class)
public abstract class ProjectileMixin
{
	@ModifyArg(method = "shootFromRotation", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/Projectile;shoot(DDDFF)V"), index = 4)
	private float modifyInaccuracy(float inaccuracy, @Local(argsOnly = true) Entity shooter)
	{
		if (shooter instanceof LivingEntity le)
		{
			var focus = le.getEffect(FAMobEffects.FOCUSED);
			if (focus != null)
				return focus.getAmplifier() > 12 ? 0 : inaccuracy * (1.5f / (2 + focus.getAmplifier()) - 0.1f);
		}
		return inaccuracy;
	}
}
