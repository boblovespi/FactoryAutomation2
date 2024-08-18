package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.Rock;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class FAWorldgenProvider extends DatapackBuiltinEntriesProvider
{
	private static final ResourceKey<ConfiguredFeature<?, ?>> ROCK_PATCH_CF = configured("rock_patch");
	private static final ResourceKey<ConfiguredFeature<?, ?>> DESERT_ROCK_PATCH_CF = configured("desert_rock_patch");
	private static final ResourceKey<ConfiguredFeature<?, ?>> MESA_ROCK_PATCH_CF = configured("mesa_rock_patch");
	private static final ResourceKey<ConfiguredFeature<?, ?>> SWAMP_ROCK_PATCH_CF = configured("swamp_rock_patch");
	private static final ResourceKey<ConfiguredFeature<?, ?>> FLINT_PATCH_CF = configured("flint_patch");

	private static final ResourceKey<PlacedFeature> NORMAL_ROCK_PATCH_PF = placed("normal_rock_patch");
	private static final ResourceKey<PlacedFeature> DESERT_ROCK_PATCH_PF = placed("desert_rock_patch");
	private static final ResourceKey<PlacedFeature> MESA_ROCK_PATCH_PF = placed("mesa_rock_patch");
	private static final ResourceKey<PlacedFeature> SWAMP_ROCK_PATCH_PF = placed("swamp_rock_patch");
	private static final ResourceKey<PlacedFeature> NORMAL_FLINT_PATCH_PF = placed("normal_flint_patch");

	private static ResourceKey<ConfiguredFeature<?, ?>> configured(String name)
	{
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, FactoryAutomation.name(name));
	}

	private static ResourceKey<PlacedFeature> placed(String name)
	{
		return ResourceKey.create(Registries.PLACED_FEATURE, FactoryAutomation.name(name));
	}

	private static ResourceKey<BiomeModifier> biome(String name)
	{
		return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, FactoryAutomation.name(name));
	}

	private static RegistrySetBuilder registrySet()
	{
		var rsb = new RegistrySetBuilder();
		rsb.add(Registries.CONFIGURED_FEATURE, b -> {
			var rockWL = SimpleWeightedRandomList.<BlockState>builder();
			for (int i = 0; i < 7; i++)
				rockWL.add(d(FABlocks.ROCKS.get(i)), i < 2 ? 7 : (i < 5 ? 5 : 2));
			b.register(ROCK_PATCH_CF, rockPatch(7, new WeightedStateProvider(rockWL)));
			b.register(DESERT_ROCK_PATCH_CF, rockPatch(4, b(FABlocks.ROCKS.get(Rock.Variants.SANDSTONE.ordinal()))));
			b.register(MESA_ROCK_PATCH_CF, rockPatch(4, b(FABlocks.ROCKS.get(Rock.Variants.TERRACOTTA.ordinal()))));
			b.register(SWAMP_ROCK_PATCH_CF, rockPatch(5, b(FABlocks.ROCKS.get(Rock.Variants.MOSSY_COBBLESTONE.ordinal()))));
			b.register(FLINT_PATCH_CF, rockPatch(2, b(FABlocks.FLINT_ROCK)));
		});
		rsb.add(Registries.PLACED_FEATURE, b -> {
			var configured = b.lookup(Registries.CONFIGURED_FEATURE);
			b.register(NORMAL_ROCK_PATCH_PF, placedRock(configured, ROCK_PATCH_CF, 2));
			b.register(DESERT_ROCK_PATCH_PF, placedRock(configured, DESERT_ROCK_PATCH_CF, 2));
			b.register(MESA_ROCK_PATCH_PF, placedRock(configured, MESA_ROCK_PATCH_CF, 2));
			b.register(SWAMP_ROCK_PATCH_PF, placedRock(configured, SWAMP_ROCK_PATCH_CF, 2));
			b.register(NORMAL_FLINT_PATCH_PF, placedRock(configured, FLINT_PATCH_CF, 1));
		});
		rsb.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, b -> {
			var biomes = b.lookup(Registries.BIOME);
			var features = b.lookup(Registries.PLACED_FEATURE);
			b.register(biome("is_typical_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_TYPICAL_OVERWORLD, NORMAL_ROCK_PATCH_PF));
			b.register(biome("is_yellow_desert_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_YELLOW_DESERT_OVERWORLD, DESERT_ROCK_PATCH_PF));
			b.register(biome("is_red_desert_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_RED_DESERT_OVERWORLD, MESA_ROCK_PATCH_PF));
			b.register(biome("is_wet_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_WET_OVERWORLD, SWAMP_ROCK_PATCH_PF));
			b.register(biome("is_surface_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_SURFACE_OVERWORLD, NORMAL_FLINT_PATCH_PF));
		});
		return rsb;
	}

	@SafeVarargs
	private static BiomeModifiers.@NotNull AddFeaturesBiomeModifier vegetalModifier(HolderGetter<Biome> biomes, HolderGetter<PlacedFeature> features, TagKey<Biome> biome,
																					ResourceKey<PlacedFeature>... feature)
	{
		return biomeModifier(biomes, features, biome, GenerationStep.Decoration.VEGETAL_DECORATION, feature);
	}

	@SafeVarargs
	private static BiomeModifiers.AddFeaturesBiomeModifier biomeModifier(HolderGetter<Biome> biomes, HolderGetter<PlacedFeature> features, TagKey<Biome> biome,
																		 GenerationStep.Decoration step, ResourceKey<PlacedFeature>... feature)
	{
		return new BiomeModifiers.AddFeaturesBiomeModifier(biomes.getOrThrow(biome), features(features, feature), step);
	}

	private static BlockStateProvider b(Supplier<? extends Block> block)
	{
		return BlockStateProvider.simple(d(block));
	}

	private static BlockState d(Supplier<? extends Block> block)
	{
		return block.get().defaultBlockState();
	}

	private static ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>> rockPatch(int tries, BlockStateProvider placer)
	{
		return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(tries, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(placer))));
	}

	private static PlacedFeature placedRock(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int count)
	{
		return new PlacedFeature(configured.getOrThrow(feature),
				List.of(CountPlacement.of(count), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome()));
	}

	@SafeVarargs
	private static HolderSet<PlacedFeature> features(HolderGetter<PlacedFeature> featureLookup, ResourceKey<PlacedFeature>... features)
	{
		return HolderSet.direct(featureLookup::getOrThrow, features);
	}

	public FAWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, registrySet(), Set.of(FactoryAutomation.MODID));
	}
}
