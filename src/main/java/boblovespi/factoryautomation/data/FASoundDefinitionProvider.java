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
														 .subtitle("subtitles.block.chopping_block.make").replace(true));
		add(FASounds.USE_CHOPPING_BLOCK,
				SoundDefinition.definition().with(sound(FactoryAutomation.name("use/chopping_block_1")), sound(FactoryAutomation.name("use/chopping_block_2")))
							   .subtitle("subtitles.block.chopping_block.use").replace(true));
		add(FASounds.USE_MILLSTONE, SoundDefinition.definition().with(sound(FactoryAutomation.name("use/millstone"))).subtitle("subtitles.block.millstone.use").replace(true));
		add(FASounds.BELLOWS_BLOWS, SoundDefinition.definition()
												   .with(sound("mob/enderdragon/wings1"), sound("mob/enderdragon/wings2"), sound("mob/enderdragon/wings3"),
														   sound("mob/enderdragon/wings4"), sound("mob/enderdragon/wings5"), sound("mob/enderdragon/wings6"))
												   .subtitle("subtitles.block.bellows.blow")
												   .replace(true));
	}
}
