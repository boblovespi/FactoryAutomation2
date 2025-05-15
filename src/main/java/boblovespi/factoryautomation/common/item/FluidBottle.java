package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStackSimple;

public class FluidBottle extends Item
{
	public FluidBottle(Properties properties, Fluid fluid)
	{
		super(properties.component(FactoryAutomation.FLUID_CONTENT_DC, SimpleFluidContent.copyOf(new FluidStack(fluid, 250))));
	}

	public IFluidHandlerItem makeHandler(ItemStack stack)
	{
		return new FluidHandlerItemStackSimple.SwapEmpty(FactoryAutomation.FLUID_CONTENT_DC, stack, Items.GLASS_BOTTLE.getDefaultInstance(), 250);
	}
}
