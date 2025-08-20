package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.recipe.Workbench;
import boblovespi.factoryautomation.common.util.FuelInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class FADataMapProvider extends DataMapProvider
{
	public FADataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
	{
		super(packOutput, lookupProvider);
	}

	@Override
	protected void gather()
	{
		builder(Workbench.PART_DATA).add(FAItems.SCREW, new Workbench.Part(FactoryAutomation.name("screw"), 1), false)
									.add(FAItems.BUSHING, new Workbench.Part(FactoryAutomation.name("bearing"), 1), false);
		builder(Workbench.TOOL_DATA).add(FAItems.COPPER_HAMMER, new Workbench.Tool(FactoryAutomation.name("hammer"), 1), false)
									.add(FAItems.IRON_HAMMER, new Workbench.Tool(FactoryAutomation.name("hammer"), 2), false)
									.add(FAItems.IRON_WRENCH, new Workbench.Tool(FactoryAutomation.name("wrench"), 1), false)
									.add(FAItems.BRONZE_HAMMER, new Workbench.Tool(FactoryAutomation.name("hammer"), 3), false)
									.add(FAItems.BRONZE_WRENCH, new Workbench.Tool(FactoryAutomation.name("wrench"), 2), false);
		builder(FuelInfo.FUEL_DATA).add(holder(Items.COAL), new FuelInfo(1600, 2172 + 273, 4_300_000_000f), false)
								   .add(holder(Items.CHARCOAL), new FuelInfo(1600, 2012 + 273, 4_300_000_000f), false)
								   .add(holder(FAItems.COAL_COKE), new FuelInfo(2400, 2232 + 273, 4_300_000_000f * 2), false);
		builder(NeoForgeDataMaps.FURNACE_FUELS).add(holder(FAItems.COAL_COKE), new FurnaceFuel(2400), false);
	}

	private Holder<Item> holder(ItemLike item)
	{
		return BuiltInRegistries.ITEM.wrapAsHolder(item.asItem());
	}
}
