package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

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
		output.accept(key("pig"), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.PIG_TALLOW)).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
						.apply(EnchantedCountIncreaseFunction.lootingMultiplier(provider, UniformGenerator.between(0, 1)))));
	}

	private ResourceKey<LootTable> key(String name)
	{
		return ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/" + name));
	}

	private LootTable.Builder rollExactly(ItemLike item)
	{
		return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(item)));
	}
}
