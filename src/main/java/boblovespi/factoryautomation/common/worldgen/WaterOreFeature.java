package boblovespi.factoryautomation.common.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.material.FluidState;

public class WaterOreFeature extends Feature<WaterOreFeature.Config>
{
	public static final Codec<Config> CODEC = RecordCodecBuilder.create(
			i -> i.group(WeightedStateProvider.CODEC.fieldOf("state_provider").forGetter(Config::stateProvider), IntProvider.CODEC.fieldOf("radius").forGetter(Config::radius),
					FloatProvider.CODEC.fieldOf("density").forGetter(Config::density)).apply(i, Config::new));

	public WaterOreFeature(Codec<Config> codec)
	{
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<Config> context)
	{
		var basePos = context.origin();
		var world = context.level();
		var random = context.random();
		var state = context.config().stateProvider.getState(random, basePos);
		var radius = context.config().radius.sample(random);
		var spawnChance = context.config().density.sample(random);

		for (int y = -10; y < 10; y++)
		{
			for (int x = -radius; x < radius; x++)
			{
				for (int z = -radius; z < radius; z++)
				{
					var pos = basePos.offset(x, y, z);
					if (pos.distSqr(basePos) <= radius * radius && world.isWaterAt(pos.above()) && world.isFluidAtPosition(pos, FluidState::isEmpty) &&
						random.nextFloat() <= spawnChance * (1 - (x * x + z * z) / (1f * radius * radius)))
						setBlock(world, pos, state);
				}
			}
		}
		return true;
	}

	public record Config(WeightedStateProvider stateProvider, IntProvider radius, FloatProvider density) implements FeatureConfiguration
	{

	}
}
