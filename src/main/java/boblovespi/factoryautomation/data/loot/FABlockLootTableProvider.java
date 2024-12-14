package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Set;
import java.util.stream.Collectors;

public class FABlockLootTableProvider extends BlockLootSubProvider
{
	private HolderLookup.RegistryLookup<Enchantment> enchants;
	private Holder.Reference<Enchantment> fortune;

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
		enchants = registries.lookupOrThrow(Registries.ENCHANTMENT);
		fortune = enchants.getOrThrow(Enchantments.FORTUNE);
		for (var rock : FABlocks.ROCKS)
			dropOther(rock.get(), FAItems.ROCK);
		dropOther(FABlocks.FLINT_ROCK.get(), Items.FLINT);

		dropOre(FABlocks.CASSITERITE_ORE, FAItems.RAW_CASSITERITE, 1);
		FABlocks.LIMONITE_ORES.forEach((k, v) -> dropOre(v, FAItems.RAW_LIMONITE, k.getCount()));
		dropSelf(FABlocks.RAW_CASSITERITE_BLOCK.get());
		dropSelf(FABlocks.RAW_LIMONITE_BLOCK.get());

		dropSelf(FABlocks.GREEN_SAND.get());
		add(FABlocks.CHARCOAL_PILE.get(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(Items.CHARCOAL))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(7, 10)))
						.apply(ApplyBonusCount.addUniformBonusCount(fortune))));
		add(FABlocks.IRON_BLOOM.get(), LootTable.lootTable().withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.IRON_SHARD))
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.apply(ApplyBonusCount.addUniformBonusCount(fortune))).withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.SLAG))
						.apply(ApplyBonusCount.addUniformBonusCount(fortune))));
		dropSelf(FABlocks.TIN_BLOCK.get());
		dropSelf(FABlocks.BRONZE_BLOCK.get());
		dropSelf(FABlocks.STEEL_BLOCK.get());
		dropSelf(FABlocks.COPPER_PLATE_BLOCK.get());
		dropSelf(FABlocks.TIN_PLATE_BLOCK.get());
		dropSelf(FABlocks.IRON_PLATE_BLOCK.get());
		dropSelf(FABlocks.BRONZE_PLATE_BLOCK.get());
		dropSelf(FABlocks.STEEL_PLATE_BLOCK.get());
		dropSelf(FABlocks.COPPER_SPACE_FRAME.get());
		dropSelf(FABlocks.TIN_SPACE_FRAME.get());
		dropSelf(FABlocks.IRON_SPACE_FRAME.get());
		dropSelf(FABlocks.BRONZE_SPACE_FRAME.get());
		dropSelf(FABlocks.STEEL_SPACE_FRAME.get());
		FABlocks.CHOPPING_BLOCKS.values().forEach(b -> dropSelf(b.get()));
		dropSelf(FABlocks.LOG_PILE.get());
		dropSelf(FABlocks.LIMONITE_CHARCOAL_MIX.get());
		dropSelf(FABlocks.STONE_CRUCIBLE.get());
		dropSelf(FABlocks.STONE_CASTING_VESSEL.get());
		dropSelf(FABlocks.STONE_WORKBENCH.get());
		dropSelf(FABlocks.BRICK_MAKER_FRAME.get());
		dropSelf(FABlocks.BRICK_CRUCIBLE.get());
		dropSelf(FABlocks.WOOD_POWER_SHAFT.get());
		dropSelf(FABlocks.MILLSTONE.get());
		dropSelf(FABlocks.HAND_CRANK.get());
	}

	private void dropOre(DeferredBlock<Block> ore, DeferredItem<Item> rawOre, int count)
	{
		add(ore.get(), this.createSilkTouchDispatchTable(ore.get(), this.applyExplosionDecay(ore.get(),
				LootItem.lootTableItem(rawOre.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))).apply(ApplyBonusCount.addOreBonusCount(fortune)))));
	}
}
