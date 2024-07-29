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
		add(FASounds.MAKE_CHOPPING_BLOCK, SoundDefinition.definition().with(sound("dig/wood1"), sound("dig/wood2"), sound("dig/wood3"), sound("dig/wood4"))
														 .subtitle(FactoryAutomation.locString("subtitles.block", "chopping_block.make")).replace(true));
		add(FASounds.USE_CHOPPING_BLOCK,
				SoundDefinition.definition().with(sound(FactoryAutomation.name("use/chopping_block_1")), sound(FactoryAutomation.name("use/chopping_block_2")))
							   .subtitle(FactoryAutomation.locString("subtitles.block", "chopping_block.use")).replace(true));
	}
}
