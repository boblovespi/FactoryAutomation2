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

	public static final DeferredHolder<Fluid, FlowingFluid> PANCAKE_BATTER_SOURCE = source(FAFluids::pancakeBatterProperties, "pancake_batter");
	public static final DeferredHolder<Fluid, FlowingFluid> PANCAKE_BATTER_FLOWING = flowing(FAFluids::pancakeBatterProperties, "pancake_batter_flowing");

	public static final DeferredHolder<FluidType, FluidType> TANNIN_TYPE = type("tannin");

	public static final DeferredHolder<Fluid, FlowingFluid> TANNIN_SOURCE = source(FAFluids::tanninProperties, "tannin");
	public static final DeferredHolder<Fluid, FlowingFluid> TANNIN_FLOWING = flowing(FAFluids::tanninProperties, "tannin_flowing");

	public static final DeferredHolder<FluidType, FluidType> LIMEWATER_TYPE = type("limewater");

	public static final DeferredHolder<Fluid, FlowingFluid> LIMEWATER_SOURCE = source(FAFluids::limewaterProperties, "limewater");
	public static final DeferredHolder<Fluid, FlowingFluid> LIMEWATER_FLOWING = flowing(FAFluids::limewaterProperties, "limewater_flowing");

	private static DeferredHolder<FluidType, FluidType> type(String name)
	{
		return FLUID_TYPES.register(name, () -> new FluidType(FluidType.Properties.create().descriptionId("fluid.factoryautomation." + name)));
	}

	private static DeferredHolder<Fluid, FlowingFluid> source(Supplier<BaseFlowingFluid.Properties> properties, String name)
	{
		return FLUIDS.register(name, () -> new BaseFlowingFluid.Source(properties.get()));
	}

	private static DeferredHolder<Fluid, FlowingFluid> flowing(Supplier<BaseFlowingFluid.Properties> properties, String name)
	{
		return FLUIDS.register(name, () -> new BaseFlowingFluid.Flowing(properties.get()));
	}

	private static BaseFlowingFluid.Properties pancakeBatterProperties()
	{
		return new BaseFlowingFluid.Properties(PANCAKE_BATTER_TYPE, PANCAKE_BATTER_SOURCE, PANCAKE_BATTER_FLOWING);
	}

	private static BaseFlowingFluid.Properties tanninProperties()
	{
		return new BaseFlowingFluid.Properties(TANNIN_TYPE, TANNIN_SOURCE, TANNIN_FLOWING);
	}

	private static BaseFlowingFluid.Properties limewaterProperties()
	{
		return new BaseFlowingFluid.Properties(LIMEWATER_TYPE, LIMEWATER_SOURCE, LIMEWATER_FLOWING);
	}
}
