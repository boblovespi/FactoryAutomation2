package boblovespi.factoryautomation.mixin;

import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TieredItem.class)
public abstract class TieredItemMixin
{
	@Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Tier;getUses()I"))
	private static int changeTierDurability(Tier tier)
	{
		if (tier == Tiers.IRON)
			return 320;
		return tier.getUses();
	}
}
