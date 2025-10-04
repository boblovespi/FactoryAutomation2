package boblovespi.factoryautomation.common.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;

public class EvaporiteFeature extends Feature<DeltaFeatureConfiguration>
{
	public EvaporiteFeature(Codec<DeltaFeatureConfiguration> codec)
	{
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<DeltaFeatureConfiguration> context)
	{
		var placed = false;
		var random = context.random();
		var level = context.level();
		var config = context.config();
		var origin = context.origin();
		var rimX = config.rimSize().sample(random);
		var rimY = config.rimSize().sample(random);
		var hasRim = rimX != 0 && rimY != 0;
		var xRadius = config.size().sample(random);
		var zRadius = config.size().sample(random);
		var maxRadius = Math.max(xRadius, zRadius);

		for (var pos : BlockPos.withinManhattan(origin, xRadius, 0, zRadius))
		{
			if (pos.distManhattan(origin) > maxRadius)
				break;

			if (isClear(level, pos, config))
			{
				if (hasRim)
				{
					placed = true;
					setBlock(level, pos, config.rim());
				}
				var contentsPos = pos.offset(rimX, 0, rimY);
				if (isClear(level, contentsPos, config))
				{
					placed = true;
					setBlock(level, contentsPos, config.contents());
				}
			}
		}

		return placed;
	}

	private static boolean isClear(LevelAccessor level, BlockPos pos, DeltaFeatureConfiguration config)
	{
		var block = level.getBlockState(pos);
		if (block.is(config.contents().getBlock()))
			return false;
		else
		{
			for (var direction : Direction.values())
			{
				var isAir = level.getBlockState(pos.relative(direction)).isAir();
				if (isAir && direction != Direction.UP)
					return false;
				else if (!isAir && direction == Direction.UP)
				{
					// return false;
				}
			}

			return true;
		}
	}
}
