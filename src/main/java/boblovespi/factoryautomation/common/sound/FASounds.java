package boblovespi.factoryautomation.common.sound;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class FASounds
{
	public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, FactoryAutomation.MODID);

	public static final DeferredHolder<SoundEvent, SoundEvent> BREAK_ROCK = register("block.rock.break");
	public static final DeferredHolder<SoundEvent, SoundEvent> PLACE_ROCK = register("block.rock.place");
	public static final DeferredHolder<SoundEvent, SoundEvent> MAKE_CHOPPING_BLOCK = register("block.chopping_block.make");
	public static final DeferredHolder<SoundEvent, SoundEvent> USE_CHOPPING_BLOCK = register("block.chopping_block.use");
	public static final DeferredHolder<SoundEvent, SoundEvent> USE_MILLSTONE = register("block.millstone.use");
	public static final DeferredHolder<SoundEvent, SoundEvent> BELLOWS_BLOWS = register("block.bellows.blow");

	private static DeferredHolder<SoundEvent, SoundEvent> register(String soundName)
	{
		return SOUND_EVENTS.register(soundName, () -> SoundEvent.createVariableRangeEvent(FactoryAutomation.name(soundName)));
	}
}
