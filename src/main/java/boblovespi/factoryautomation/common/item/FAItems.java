package boblovespi.factoryautomation.common.item;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.tool.ToolTiers;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.BiFunction;

public class FAItems
{
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FactoryAutomation.MODID);

	// Resources

	public static final DeferredItem<BlockItem> ROCK = ITEMS.registerSimpleBlockItem("rock", FABlocks.COBBLESTONE_ROCK);

	// Tools

	public static final DeferredItem<ShovelItem> FLINT_SHOVEL = tieredTool("flint_shovel", ShovelItem::new, ToolTiers.FLINT_TIER, new Item.Properties(), 1.5f, -3f);
	public static final DeferredItem<PickaxeItem> FLINT_PICKAXE = tieredTool("flint_pickaxe", PickaxeItem::new, ToolTiers.FLINT_TIER, new Item.Properties(), 1, -2.8f);
	public static final DeferredItem<AxeItem> FLINT_AXE = tieredTool("flint_axe", AxeItem::new, ToolTiers.FLINT_TIER, new Item.Properties(), 6, -3.2f);
	public static final DeferredItem<HoeItem> FLINT_HOE = tieredTool("flint_hoe", HoeItem::new, ToolTiers.FLINT_TIER, new Item.Properties(), 1, -3f);
	public static final DeferredItem<SwordItem> FLINT_SWORD = tieredTool("flint_sword", SwordItem::new, ToolTiers.FLINT_TIER, new Item.Properties(), 3, -2.4f);

	private static <T extends TieredItem> DeferredItem<T> tieredTool(String name, BiFunction<Tier, Item.Properties, T> constructor, Tier tier, Item.Properties properties, float damage, float as)
	{
		return ITEMS.registerItem(name, p -> constructor.apply(tier, p), properties.attributes(SwordItem.createAttributes(tier, damage, as)));
	}
}
