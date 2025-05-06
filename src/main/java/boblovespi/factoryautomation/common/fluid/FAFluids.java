package boblovespi.factoryautomation.common.fluid;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class FAFluids
{
	public static final DeferredRegister<FluidType> FLUID_TYPES = DeferredRegister.create(NeoForgeRegistries.FLUID_TYPES, FactoryAutomation.MODID);
	public static final DeferredRegister<Fluid> FLUIDS = DeferredRegister.create(BuiltInRegistries.FLUID, FactoryAutomation.MODID);

	public static final DeferredHolder<FluidType, FluidType> PANCAKE_BATTER_TYPE = type("pancake_batter");

	public static final DeferredHolder<Fluid, FlowingFluid> PANCAKE_BATTER_SOURCE = source(FAFluids::pancakeBatterProperties);

	public static final DeferredHolder<Fluid, FlowingFluid> PANCAKE_BATTER_FLOWING = flowing(FAFluids::pancakeBatterProperties);

	private static DeferredHolder<FluidType, FluidType> type(String name)
	{
		return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.factoryautomation." + name)));
	}

	private static DeferredHolder<Fluid, FlowingFluid> source(Supplier<BaseFlowingFluid.Properties> properties)
	{
		return FLUIDS.register("pancake_batter", () -> new BaseFlowingFluid.Source(properties.get()));
	}

	private static DeferredHolder<Fluid, FlowingFluid> flowing(Supplier<BaseFlowingFluid.Properties> properties)
	{
		return FLUIDS.register("pancake_batter_flowing", () -> new BaseFlowingFluid.Flowing(properties.get()));
	}

	private static BaseFlowingFluid.Properties pancakeBatterProperties()
	{
		return new BaseFlowingFluid.Properties(PANCAKE_BATTER_TYPE, PANCAKE_BATTER_SOURCE, PANCAKE_BATTER_FLOWING);
	}
}
