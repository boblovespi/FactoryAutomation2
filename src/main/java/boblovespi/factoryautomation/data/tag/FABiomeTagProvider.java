package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class FABiomeTagProvider extends BiomeTagsProvider
{
	public FABiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper efh)
	{
		super(output, lookupProvider, FactoryAutomation.MODID, efh);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(FATags.Biomes.IS_TYPICAL_OVERWORLD).addTags(BiomeTags.IS_FOREST, BiomeTags.IS_HILL, Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_MOUNTAIN);
		tag(FATags.Biomes.IS_YELLOW_DESERT_OVERWORLD).add(Biomes.DESERT);
		tag(FATags.Biomes.IS_RED_DESERT_OVERWORLD).addTags(BiomeTags.IS_BADLANDS, BiomeTags.IS_SAVANNA);
		tag(FATags.Biomes.IS_WET_OVERWORLD).addTags(BiomeTags.IS_JUNGLE, BiomeTags.IS_TAIGA, Tags.Biomes.IS_SWAMP);
		tag(FATags.Biomes.IS_SURFACE_OVERWORLD).addTags(FATags.Biomes.IS_TYPICAL_OVERWORLD, FATags.Biomes.IS_YELLOW_DESERT_OVERWORLD, FATags.Biomes.IS_RED_DESERT_OVERWORLD,
				FATags.Biomes.IS_WET_OVERWORLD);
	}
}
