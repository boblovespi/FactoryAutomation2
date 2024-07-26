package boblovespi.factoryautomation.common.sound;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.SoundType;
import net.neoforged.neoforge.common.util.DeferredSoundType;

import java.util.function.Supplier;

public class FASoundTypes
{
	public static final SoundType ROCK = new DeferredSoundType(1, 1, FASounds.BREAK_ROCK, v(SoundEvents.STONE_STEP), FASounds.PLACE_ROCK, v(SoundEvents.STONE_HIT),
			v(SoundEvents.STONE_FALL));

	private static Supplier<SoundEvent> v(SoundEvent vanillaSound)
	{
		return () -> vanillaSound;
	}
}
