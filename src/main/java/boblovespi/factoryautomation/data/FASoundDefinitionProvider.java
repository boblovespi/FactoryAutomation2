package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.sound.FASounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;

public class FASoundDefinitionProvider extends SoundDefinitionsProvider
{
	protected FASoundDefinitionProvider(PackOutput output, ExistingFileHelper helper)
	{
		super(output, FactoryAutomation.MODID, helper);
	}

	@Override
	public void registerSounds()
	{
		add(FASounds.BREAK_ROCK, SoundDefinition.definition().with(sound(FactoryAutomation.name("break/rock_1")), sound(FactoryAutomation.name("break/rock_2")))
												.subtitle("subtitles.block.generic.break").replace(true));
		add(FASounds.PLACE_ROCK, SoundDefinition.definition().with(sound(FactoryAutomation.name("break/rock_1")), sound(FactoryAutomation.name("break/rock_2")))
												.subtitle("subtitles.block.generic.place").replace(true));
	}
}
