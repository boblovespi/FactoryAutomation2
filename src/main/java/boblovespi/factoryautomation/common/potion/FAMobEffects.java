package boblovespi.factoryautomation.common.potion;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FAMobEffects
{
	public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, FactoryAutomation.MODID);

	public static final DeferredHolder<MobEffect, ?> FOCUSED = MOB_EFFECTS.register("focused", () -> new FocusedEffect(MobEffectCategory.BENEFICIAL, 0x2718A3));
}