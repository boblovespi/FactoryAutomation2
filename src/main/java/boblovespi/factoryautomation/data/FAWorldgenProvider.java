package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.ArabicaLeaves;
import boblovespi.factoryautomation.common.block.resource.Rock;
import boblovespi.factoryautomation.common.block.types.OreQualities;
import boblovespi.factoryautomation.common.worldgen.FAWorldgen;
import boblovespi.factoryautomation.common.worldgen.WaterOreFeature;
import net.minecraft.core.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

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
	private static final ResourceKey<ConfiguredFeature<?, ?>> SMALL_CASSITERITE_ORE_CF = configured("small_cassiterite_ore");
	private static final ResourceKey<ConfiguredFeature<?, ?>> SWAMP_LIMONITE_ORE_CF = configured("swamp_limonite_ore");
	private static final ResourceKey<ConfiguredFeature<?, ?>> CAVE_EVAPORITE_PATCH_CF = configured("cave_evaporite_patch");
	private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_GREEN_ONION_FLOWER_CF = configured("wild_green_onion_flower");
	private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_ARABICA_CF = configured("wild_arabica");
	private static final ResourceKey<ConfiguredFeature<?, ?>> WILD_TEA_SHRUB_CF = configured("wild_tea_shrub");
	private static final ResourceKey<ConfiguredFeature<?, ?>> MINT_PATCH_CF = configured("mint_patch");

	private static final ResourceKey<PlacedFeature> NORMAL_ROCK_PATCH_PF = placed("normal_rock_patch");
	private static final ResourceKey<PlacedFeature> DESERT_ROCK_PATCH_PF = placed("desert_rock_patch");
	private static final ResourceKey<PlacedFeature> MESA_ROCK_PATCH_PF = placed("mesa_rock_patch");
	private static final ResourceKey<PlacedFeature> SWAMP_ROCK_PATCH_PF = placed("swamp_rock_patch");
	private static final ResourceKey<PlacedFeature> NORMAL_FLINT_PATCH_PF = placed("normal_flint_patch");
	private static final ResourceKey<PlacedFeature> SMALL_CASSITERITE_ORE_PF = placed("small_cassiterite_ore");
	private static final ResourceKey<PlacedFeature> SWAMP_LIMONITE_ORE_PF = placed("swamp_limonite_ore");
	private static final ResourceKey<PlacedFeature> CAVE_EVAPORITE_PATCH_PF = placed("cave_evaporite_patch");
	private static final ResourceKey<PlacedFeature> WILD_GREEN_ONION_FLOWER_PF = placed("wild_green_onion_flower");
	private static final ResourceKey<PlacedFeature> WILD_ARABICA_PF = placed("wild_arabica");
	private static final ResourceKey<PlacedFeature> WILD_TEA_SHRUB_PF = placed("wild_tea_shrub");
	private static final ResourceKey<PlacedFeature> MINT_PATCH_PF = placed("mint_patch");

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

	public static RegistrySetBuilder registrySet(RegistrySetBuilder rsb)
	{
		// var rsb = new RegistrySetBuilder();
		rsb.add(Registries.CONFIGURED_FEATURE, b -> {
			var rockWL = SimpleWeightedRandomList.<BlockState>builder();
			for (int i = 0; i < 7; i++)
				rockWL.add(d(FABlocks.ROCKS.get(i)), i < 2 ? 7 : (i < 5 ? 5 : 2));
			b.register(ROCK_PATCH_CF, rockPatch(7, new WeightedStateProvider(rockWL)));
			b.register(DESERT_ROCK_PATCH_CF, rockPatch(4, b(FABlocks.ROCKS.get(Rock.Variants.SANDSTONE.ordinal()))));
			b.register(MESA_ROCK_PATCH_CF, rockPatch(4, b(FABlocks.ROCKS.get(Rock.Variants.TERRACOTTA.ordinal()))));
			b.register(SWAMP_ROCK_PATCH_CF, rockPatch(5, b(FABlocks.ROCKS.get(Rock.Variants.MOSSY_COBBLESTONE.ordinal()))));
			b.register(FLINT_PATCH_CF, rockPatch(2, b(FABlocks.FLINT_ROCK)));
			b.register(SMALL_CASSITERITE_ORE_CF, ore(5, d(FABlocks.CASSITERITE_ORE)));
			b.register(SWAMP_LIMONITE_ORE_CF, swampOre());
			b.register(CAVE_EVAPORITE_PATCH_CF, caveEvaporite());
			b.register(WILD_GREEN_ONION_FLOWER_CF, flower(32, b(FABlocks.WILD_GREEN_ONION)));
			b.register(WILD_ARABICA_CF,
					leafBush(16, BlockStateProvider.simple(FABlocks.ARABICA_LEAVES.get().defaultBlockState().setValue(ArabicaLeaves.AGE, 2))));
			b.register(WILD_TEA_SHRUB_CF, flower(24, b(FABlocks.WILD_TEA_SHRUB)));
			b.register(MINT_PATCH_CF, flower(64, randomHorizontalFacing(FABlocks.MINT_BUSH)));
		});
		rsb.add(Registries.PLACED_FEATURE, b -> {
			var configured = b.lookup(Registries.CONFIGURED_FEATURE);
			b.register(NORMAL_ROCK_PATCH_PF, placedRock(configured, ROCK_PATCH_CF, 2));
			b.register(DESERT_ROCK_PATCH_PF, placedRock(configured, DESERT_ROCK_PATCH_CF, 2));
			b.register(MESA_ROCK_PATCH_PF, placedRock(configured, MESA_ROCK_PATCH_CF, 2));
			b.register(SWAMP_ROCK_PATCH_PF, placedRock(configured, SWAMP_ROCK_PATCH_CF, 2));
			b.register(NORMAL_FLINT_PATCH_PF, placedRock(configured, FLINT_PATCH_CF, 1));
			b.register(SMALL_CASSITERITE_ORE_PF, placedOre(configured, SMALL_CASSITERITE_ORE_CF, 8, getHeightRange(32, 96)));
			b.register(SWAMP_LIMONITE_ORE_PF, placedSeafloor(configured, SWAMP_LIMONITE_ORE_CF, 17));
			b.register(CAVE_EVAPORITE_PATCH_PF, placedCaveEvaporite(configured, CAVE_EVAPORITE_PATCH_CF));
			b.register(WILD_GREEN_ONION_FLOWER_PF, placedFlower(configured, WILD_GREEN_ONION_FLOWER_CF, 32, 15, 4));
			b.register(WILD_ARABICA_PF, placedFlower(configured, WILD_ARABICA_CF, 16, 14, 5));
			b.register(WILD_TEA_SHRUB_PF, placedFlower(configured, WILD_TEA_SHRUB_CF, 40, 14, 5));
			b.register(MINT_PATCH_PF, placedFlower(configured, MINT_PATCH_CF, 40, 6, 20));
		});
		rsb.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, b -> {
			var biomes = b.lookup(Registries.BIOME);
			var features = b.lookup(Registries.PLACED_FEATURE);
			b.register(biome("is_overworld"),
					biomeModifier(biomes, features, BiomeTags.IS_OVERWORLD, GenerationStep.Decoration.UNDERGROUND_ORES, SMALL_CASSITERITE_ORE_PF, CAVE_EVAPORITE_PATCH_PF));
			b.register(biome("is_swamp"), biomeModifier(biomes, features, Tags.Biomes.IS_SWAMP, GenerationStep.Decoration.UNDERGROUND_ORES, SWAMP_LIMONITE_ORE_PF));

			b.register(biome("is_typical_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_TYPICAL_OVERWORLD, NORMAL_ROCK_PATCH_PF));
			b.register(biome("is_yellow_desert_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_YELLOW_DESERT_OVERWORLD, DESERT_ROCK_PATCH_PF));
			b.register(biome("is_red_desert_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_RED_DESERT_OVERWORLD, MESA_ROCK_PATCH_PF));
			b.register(biome("is_wet_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_WET_OVERWORLD, SWAMP_ROCK_PATCH_PF));
			b.register(biome("is_surface_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_SURFACE_OVERWORLD, NORMAL_FLINT_PATCH_PF));
			b.register(biome("is_temperate_grassy_overworld"), vegetalModifier(biomes, features, FATags.Biomes.IS_TEMPERATE_GRASSY_OVERWORLD, WILD_GREEN_ONION_FLOWER_PF));
			b.register(biome("is_forest_overworld"), vegetalModifier(biomes, features, BiomeTags.IS_FOREST, MINT_PATCH_PF));

			b.register(biome("is_bamboo_forest"), biomeModifier(biomes, features, Biomes.BAMBOO_JUNGLE, GenerationStep.Decoration.VEGETAL_DECORATION, WILD_TEA_SHRUB_PF));
			b.register(biome("is_savanna_plateau"), biomeModifier(biomes, features, Biomes.SAVANNA_PLATEAU, GenerationStep.Decoration.VEGETAL_DECORATION, WILD_ARABICA_PF));
		});
		return rsb;
	}

	private static HeightRangePlacement getHeightRange(int bottom, int top)
	{
		return HeightRangePlacement.triangle(VerticalAnchor.absolute(bottom), VerticalAnchor.absolute(top));
	}

	@SafeVarargs
	private static BiomeModifiers.AddFeaturesBiomeModifier vegetalModifier(HolderGetter<Biome> biomes, HolderGetter<PlacedFeature> features, TagKey<Biome> biome,
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

	@SafeVarargs
	private static BiomeModifiers.AddFeaturesBiomeModifier biomeModifier(HolderGetter<Biome> biomes, HolderGetter<PlacedFeature> features, ResourceKey<Biome> biome,
																		 GenerationStep.Decoration step, ResourceKey<PlacedFeature>... feature)
	{
		return new BiomeModifiers.AddFeaturesBiomeModifier(HolderSet.direct(biomes.getOrThrow(biome)), features(features, feature), step);
	}

	private static BlockStateProvider b(Supplier<? extends Block> block)
	{
		return BlockStateProvider.simple(d(block));
	}

	private static BlockStateProvider randomHorizontalFacing(Supplier<? extends Block> block)
	{
		var builder = new SimpleWeightedRandomList.Builder<BlockState>();
		Direction.Plane.HORIZONTAL.iterator().forEachRemaining(d -> builder.add(d(block).setValue(BlockStateProperties.HORIZONTAL_FACING, d)));
		return new WeightedStateProvider(builder);
	}

	private static BlockState d(Supplier<? extends Block> block)
	{
		return block.get().defaultBlockState();
	}

	private static ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>> rockPatch(int tries, BlockStateProvider placer)
	{
		return new ConfiguredFeature<>(Feature.RANDOM_PATCH,
				new RandomPatchConfiguration(tries, 6, 2, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(placer))));
	}

	private static ConfiguredFeature<OreConfiguration, Feature<OreConfiguration>> ore(int size, BlockState stoneOre)
	{
		return new ConfiguredFeature<>(Feature.ORE,
				new OreConfiguration(List.of(OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), stoneOre)), size));
	}

	private static ConfiguredFeature<WaterOreFeature.Config, Feature<WaterOreFeature.Config>> swampOre()
	{
		var builder = SimpleWeightedRandomList.<BlockState>builder();
		builder.add(d(FABlocks.LIMONITE_ORES.get(OreQualities.POOR)), 6);
		builder.add(d(FABlocks.LIMONITE_ORES.get(OreQualities.NORMAL)), 3);
		builder.add(d(FABlocks.LIMONITE_ORES.get(OreQualities.RICH)), 1);
		return new ConfiguredFeature<>(FAWorldgen.WATER_ORE_FEATURE.get(),
				new WaterOreFeature.Config(new WeightedStateProvider(builder), ConstantInt.of(12), ConstantFloat.of(0.8f)));
	}

	private static ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>> flower(int tries, BlockStateProvider placer)
	{
		return new ConfiguredFeature<>(Feature.FLOWER,
				new RandomPatchConfiguration(tries, 7, 3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(placer))));
	}

	private static ConfiguredFeature<RandomPatchConfiguration, Feature<RandomPatchConfiguration>> leafBush(int tries, BlockStateProvider placer)
	{
		return new ConfiguredFeature<>(Feature.FLOWER,
				new RandomPatchConfiguration(tries, 3, 5, PlacementUtils.filtered(Feature.BLOCK_COLUMN,
						new BlockColumnConfiguration(
								List.of(BlockColumnConfiguration.layer(UniformInt.of(1, 3), placer)),
								Direction.UP,
								BlockPredicate.ONLY_IN_AIR_PREDICATE,
								false
						),
						BlockPredicate.allOf(BlockPredicate.matchesTag(Direction.DOWN.getNormal(), BlockTags.DIRT),
								BlockPredicate.ONLY_IN_AIR_PREDICATE))));
	}

	private static ConfiguredFeature<DeltaFeatureConfiguration, Feature<DeltaFeatureConfiguration>> caveEvaporite()
	{
		return new ConfiguredFeature<>(FAWorldgen.EVAPORITE_FEATURE.get(),
				new DeltaFeatureConfiguration(d(FABlocks.HALITE),
						d(FABlocks.GYPSUM),
						UniformInt.of(4, 8),
						UniformInt.of(1, 3)));
	}

	private static PlacedFeature placedRock(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int count)
	{
		return new PlacedFeature(configured.getOrThrow(feature),
				List.of(CountPlacement.of(count), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.WORLD_SURFACE_WG), BiomeFilter.biome()));
	}

	private static PlacedFeature placedSeafloor(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int rarity)
	{
		return new PlacedFeature(configured.getOrThrow(feature),
				List.of(RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), HeightmapPlacement.onHeightmap(Heightmap.Types.OCEAN_FLOOR_WG), BiomeFilter.biome()));
	}

	private static PlacedFeature placedOre(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int count,
										   HeightRangePlacement heightRange)
	{
		return new PlacedFeature(configured.getOrThrow(feature), List.of(CountPlacement.of(count), InSquarePlacement.spread(), heightRange, BiomeFilter.biome()));
	}

	private static PlacedFeature placedFlower(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature, int rarity, int below, int above)
	{
		return new PlacedFeature(configured.getOrThrow(feature),
				List.of(NoiseThresholdCountPlacement.of(-0.8,
						below, above), RarityFilter.onAverageOnceEvery(rarity), InSquarePlacement.spread(), PlacementUtils.HEIGHTMAP, BiomeFilter.biome()));
	}

	private static PlacedFeature placedCaveEvaporite(HolderGetter<ConfiguredFeature<?, ?>> configured, ResourceKey<ConfiguredFeature<?, ?>> feature)
	{
		return new PlacedFeature(configured.getOrThrow(feature),
				List.of(
						HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(32), VerticalAnchor.absolute(50)),
						EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.ONLY_IN_AIR_PREDICATE, 32),
						NoiseBasedCountPlacement.of(10, 50, -0.1),
						CountPlacement.of(10),
						RandomOffsetPlacement.of(UniformInt.of(0, 15), UniformInt.of(-10, 10)),
						BiomeFilter.biome()
					   ));
	}

	/*            context,
            FLOWER_PLAINS,
            holder22,
            NoiseThresholdCountPlacement.of(-0.8, 15, 4),
            RarityFilter.onAverageOnceEvery(32),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );*/

	@SafeVarargs
	private static HolderSet<PlacedFeature> features(HolderGetter<PlacedFeature> featureLookup, ResourceKey<PlacedFeature>... features)
	{
		return HolderSet.direct(featureLookup::getOrThrow, features);
	}

	public FAWorldgenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, null/*registrySet()*/, Set.of(FactoryAutomation.MODID));
	}
}
