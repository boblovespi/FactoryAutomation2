package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.Set;
import java.util.stream.Collectors;

public class FABlockLootTableProvider extends BlockLootSubProvider
{
	protected FABlockLootTableProvider(HolderLookup.Provider pRegistries)
	{
		super(Set.of(), FeatureFlags.DEFAULT_FLAGS, pRegistries);
	}

	@Override
	protected Iterable<Block> getKnownBlocks()
	{
		return FABlocks.BLOCKS.getEntries().stream().map(DeferredHolder::get).collect(Collectors.toList());
	}

	@Override
	protected void generate()
	{
		var enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
		var fortune = enchants.getOrThrow(Enchantments.FORTUNE);
		for (var rock : FABlocks.ROCKS)
			dropOther(rock.get(), FAItems.ROCK);
		dropOther(FABlocks.FLINT_ROCK.get(), Items.FLINT);
		dropSelf(FABlocks.GREEN_SAND.get());
		add(FABlocks.CHARCOAL_PILE.get(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(Items.CHARCOAL))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
						.apply(ApplyBonusCount.addUniformBonusCount(fortune))));
		FABlocks.CHOPPING_BLOCKS.values().forEach(b -> dropSelf(b.get()));
		dropSelf(FABlocks.LOG_PILE.get());
		dropSelf(FABlocks.STONE_CRUCIBLE.get());
		dropSelf(FABlocks.STONE_CASTING_VESSEL.get());
	}
}
