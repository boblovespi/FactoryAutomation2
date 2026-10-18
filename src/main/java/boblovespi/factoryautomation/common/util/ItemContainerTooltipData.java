package boblovespi.factoryautomation.common.util;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

import java.util.ArrayList;
import java.util.List;

public record ItemContainerTooltipData(List<ItemStack> contents) implements TooltipComponent
{
	public static ItemContainerTooltipData fromContents(ItemContainerContents contents)
	{
		var list = new ArrayList<ItemStack>();
		for (var item : contents.nonEmptyItems())
			list.stream().filter(i -> ItemStack.isSameItemSameComponents(i, item)).findFirst().ifPresentOrElse(i -> i.grow(item.getCount()), () -> list.add(item.copy()));
		return new ItemContainerTooltipData(list);
	}
}
