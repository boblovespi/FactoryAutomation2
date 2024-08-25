package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FAParticleTypes;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class FAParticleDescriptionProvider extends ParticleDescriptionProvider
{
	protected FAParticleDescriptionProvider(PackOutput output, ExistingFileHelper fileHelper)
	{
		super(output, fileHelper);
	}

	@Override
	protected void addDescriptions()
	{
		sprite(FAParticleTypes.METAL_SPARK.get(), FactoryAutomation.name("metal_spark"));
	}
}
