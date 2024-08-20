package boblovespi.factoryautomation.common.worldgen;

import boblovespi.factoryautomation.FactoryAutomation;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class FAWorldgen
{
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, FactoryAutomation.MODID);

	public static final DeferredHolder<Feature<?>, Feature<WaterOreFeature.Config>> WATER_ORE_FEATURE = register("water_ore", WaterOreFeature::new, WaterOreFeature.CODEC);

	private static <T extends FeatureConfiguration> DeferredHolder<Feature<?>, Feature<T>> register(String name, Function<Codec<T>, Feature<T>> supplier, Codec<T> codec)
	{
		return FEATURES.register(name, () -> supplier.apply(codec));
	}
}
