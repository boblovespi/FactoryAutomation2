package boblovespi.factoryautomation.common.potion;

import boblovespi.factoryautomation.common.FAAttachmentTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class FocusedEffect extends MobEffect
{
	protected FocusedEffect(MobEffectCategory category, int color)
	{
		super(category, color);
	}

	@Override
	public boolean applyEffectTick(LivingEntity livingEntity, int amplifier)
	{
		if (livingEntity instanceof Player player)
			player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).tick();
		return true;
	}

	@Override
	public boolean shouldApplyEffectTickThisTick(int duration, int amplifier)
	{
		return true;
	}

	@Override
	public void onEffectAdded(LivingEntity livingEntity, int amplifier)
	{
		super.onEffectAdded(livingEntity, amplifier);
		if (livingEntity instanceof Player player)
			player.getData(FAAttachmentTypes.FOCUSED_PLAYER_DATA).reset();
	}
}
