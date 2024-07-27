package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.FactoryAutomation;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class OverridesLootTableProvider implements LootTableSubProvider
{
	private final HolderLookup.Provider provider;

	public OverridesLootTableProvider(HolderLookup.Provider provider)
	{
		this.provider = provider;
	}

	@Override
	public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output)
	{
		output.accept(key("short_grass"), rollExactly(Blocks.SHORT_GRASS));
	}

	private ResourceKey<LootTable> key(String name)
	{
		return ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/"+name));
	}

	private LootTable.Builder rollExactly(ItemLike item)
	{
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(item)));
	}
}
