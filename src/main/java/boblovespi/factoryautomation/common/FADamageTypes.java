package boblovespi.factoryautomation.common;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;

public class FADamageTypes
{
	public static final ResourceKey<DamageType> METAL_TOO_HOT = ResourceKey.create(Registries.DAMAGE_TYPE, FactoryAutomation.name("metal_too_hot"));
	public static final ResourceKey<DamageType> PUNCHING_WOOD = ResourceKey.create(Registries.DAMAGE_TYPE, FactoryAutomation.name("punching_wood"));

	public static DamageSource metalTooHot(RegistryAccess registryAccess)
	{
		return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(METAL_TOO_HOT));
	}
	public static DamageSource punchingWood(RegistryAccess registryAccess)
	{
		return new DamageSource(registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(PUNCHING_WOOD));
	}
}
