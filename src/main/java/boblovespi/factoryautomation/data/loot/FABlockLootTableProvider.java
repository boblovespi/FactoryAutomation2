package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.block.resource.ArabicaLeaves;
import boblovespi.factoryautomation.common.block.resource.ArabicaStem;
import boblovespi.factoryautomation.common.block.resource.TeaShrub;
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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LimitCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.Set;
import java.util.stream.Collectors;

import static net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties;
import static net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.hasBlockStateProperties;

public class FABlockLootTableProvider extends BlockLootSubProvider
{
	private static final float[] NORMAL_LEAVES_STICK_CHANCES = new float[]{0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F};
	@SuppressWarnings({"NotNullFieldNotInitialized", "FieldCanBeLocal"})
	private HolderLookup.RegistryLookup<Enchantment> enchants;
	@SuppressWarnings("NotNullFieldNotInitialized")
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

		dropSelf(FABlocks.CHERT.get());
		FABlocks.ANDESITE_BRICKS.values().forEach(b -> dropSelf(b.get()));
		FABlocks.GRANITE_BRICKS.values().forEach(b -> dropSelf(b.get()));
		FABlocks.DIORITE_BRICKS.values().forEach(b -> dropSelf(b.get()));

		for (var rock : FABlocks.ROCKS)
			dropOther(rock.get(), FAItems.ROCK);
		dropOther(FABlocks.FLINT_ROCK.get(), Items.FLINT);

		dropOre(FABlocks.CASSITERITE_ORE, FAItems.RAW_CASSITERITE, 1);
		FABlocks.LIMONITE_ORES.forEach((k, v) -> dropOre(v, FAItems.RAW_LIMONITE, k.getCount()));
		dropSelf(FABlocks.RAW_CASSITERITE_BLOCK.get());
		dropSelf(FABlocks.RAW_LIMONITE_BLOCK.get());
		add(FABlocks.HALITE.get(), p -> createSilkTouchDispatchTable(p, applyExplosionDecay(p, LootItem.lootTableItem(FAItems.SALT)
																									   .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 4.0F)))
																									   .apply(ApplyBonusCount.addUniformBonusCount(fortune))
																									   .apply(LimitCount.limitCount(IntRange.range(1, 4))))));
		dropSelf(FABlocks.GYPSUM.get());
		dropSelf(FABlocks.ANCIENT_IRON_BLOCK.get());
		dropSelf(FABlocks.WEAK_IRON_BLOCK.get());
		dropSelf(FABlocks.IRON_SAND.get());
		dropSelf(FABlocks.IRON_SAND_CHARCOAL_MIX.get());
		dropSelf(FABlocks.MINT_BUSH.get());
		dropSelf(FABlocks.WILD_GREEN_ONION.get());
		add(FABlocks.GREEN_ONIONS.get(), applyExplosionDecay(FAItems.GREEN_ONION, LootTable.lootTable().withPool(LootPool.lootPool().add(
				LootItem.lootTableItem(FAItems.GREEN_ONION)
						.apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 4)))
						.apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune, 0.5714286F, 3))
						.when(hasBlockStateProperties(FABlocks.GREEN_ONIONS.get())
									  .setProperties(properties().hasProperty(BlockStateProperties.AGE_3, 2))
									  .or(hasBlockStateProperties(FABlocks.GREEN_ONIONS.get()).setProperties(
											  properties().hasProperty(BlockStateProperties.AGE_3, 3))))
						.otherwise(LootItem.lootTableItem(FAItems.GREEN_ONION))))).withPool(
				LootPool.lootPool().add(LootItem.lootTableItem(FAItems.WILD_GREEN_ONION).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1))))
						.when(hasBlockStateProperties(FABlocks.GREEN_ONIONS.get())
									  .setProperties(properties().hasProperty(BlockStateProperties.AGE_3, 3)))));
		add(FABlocks.GINGER.get(), applyExplosionDecay(FABlocks.GINGER,
				LootTable.lootTable()
						 .withPool(LootPool.lootPool().add(LootItem.lootTableItem(FAItems.GINGER)))
						 .withPool(
								 LootPool.lootPool()
										 .when(hasBlockStateProperties(FABlocks.GINGER.get())
													   .setProperties(
															   properties().hasProperty(BlockStateProperties.AGE_3, 3)))
										 .add(LootItem.lootTableItem(FAItems.GINGER).apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune, 0.5714286F, 3))))));
		add(FABlocks.SOYBEAN.get(), b -> applyExplosionDecay(b,
				LootTable.lootTable()
						 .withPool(LootPool.lootPool().add(LootItem.lootTableItem(FAItems.SOYBEANS)))
						 .withPool(
								 LootPool.lootPool()
										 .when(hasBlockStateProperties(b)
													   .setProperties(
															   properties().hasProperty(BlockStateProperties.AGE_7, 7)))
										 .add(LootItem.lootTableItem(FAItems.SOYBEANS).apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune, 4 / 7f, 3))))
						 .withPool(
								 LootPool.lootPool()
										 .when(hasBlockStateProperties(b)
													   .setProperties(
															   properties().hasProperty(BlockStateProperties.AGE_7, 6)))
										 .add(LootItem.lootTableItem(FAItems.EDAMAME).apply(ApplyBonusCount.addBonusBinomialDistributionCount(fortune, 4 / 7f, 2))))));
		add(FABlocks.ARABICA_LEAVES.get(), b -> createSilkTouchOrShearsDispatchTable(b, getStickDrops(b))
														.withPool(LootPool.lootPool()
																		  .setRolls(ConstantValue.exactly(1))
																		  .when(hasBlockStateProperties(b).setProperties(properties().hasProperty(ArabicaLeaves.AGE, 2)))
																		  .add(LootItem.lootTableItem(FAItems.COFFEE_CHERRY))
																		  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))));
		add(FABlocks.ARABICA_STEM.get(), b -> LootTable.lootTable().withPool(LootPool.lootPool()
																					 .setRolls(ConstantValue.exactly(1))
																					 .when(hasBlockStateProperties(b)
																								   .setProperties(properties().hasProperty(ArabicaStem.AGE, 2))
																								   .or(hasBlockStateProperties(b).setProperties(
																										   properties().hasProperty(ArabicaStem.AGE, 3))))
																					 .add(getStickDrops(b))));
		add(FABlocks.WILD_TEA_SHRUB.get(), b -> createSilkTouchOrShearsDispatchTable(b,
				LootItem.lootTableItem(FAItems.TEA_SEEDS).apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))));
		add(FABlocks.TEA_SHRUB.get(), b -> LootTable.lootTable()
													.withPool(LootPool.lootPool()
																	  .setRolls(ConstantValue.exactly(1))
																	  .when(hasBlockStateProperties(b)
																					.setProperties(properties().hasProperty(TeaShrub.AGE, 0))
																					.or(hasBlockStateProperties(b).setProperties(properties().hasProperty(TeaShrub.AGE, 1)))
																					.or(hasBlockStateProperties(b).setProperties(properties().hasProperty(TeaShrub.AGE, 2)))
																					.invert())
																	  .add(getStickDrops(b)))
													.withPool(LootPool.lootPool()
																	  .setRolls(ConstantValue.exactly(1))
																	  .add(LootItem.lootTableItem(FAItems.TEA_SEEDS))));

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
		dropSelf(FABlocks.DRIED_BRICKS.get());
		dropSelf(FABlocks.TIN_BLOCK.get());
		dropSelf(FABlocks.LEAD_BLOCK.get());
		dropSelf(FABlocks.BRONZE_BLOCK.get());
		dropSelf(FABlocks.NICKEL_BLOCK.get());
		dropSelf(FABlocks.SILVER_BLOCK.get());
		dropSelf(FABlocks.MAGMATIC_BRASS_BLOCK.get());
		dropSelf(FABlocks.PIG_IRON_BLOCK.get());
		dropSelf(FABlocks.STEEL_BLOCK.get());
		dropSelf(FABlocks.ALUMINUM_BLOCK.get());
		dropSelf(FABlocks.ALUMINUM_BRONZE_BLOCK.get());
		dropSelf(FABlocks.CHROMIUM_BLOCK.get());
		dropSelf(FABlocks.COPPER_PLATE_BLOCK.get());
		dropSelf(FABlocks.TIN_PLATE_BLOCK.get());
		dropSelf(FABlocks.LEAD_PLATE_BLOCK.get());
		dropSelf(FABlocks.IRON_PLATE_BLOCK.get());
		dropSelf(FABlocks.BRONZE_PLATE_BLOCK.get());
		dropSelf(FABlocks.NICKEL_PLATE_BLOCK.get());
		dropSelf(FABlocks.SILVER_PLATE_BLOCK.get());
		dropSelf(FABlocks.MAGMATIC_BRASS_PLATE_BLOCK.get());
		dropSelf(FABlocks.PIG_IRON_PLATE_BLOCK.get());
		dropSelf(FABlocks.STEEL_PLATE_BLOCK.get());
		dropSelf(FABlocks.ALUMINUM_PLATE_BLOCK.get());
		dropSelf(FABlocks.ALUMINUM_BRONZE_PLATE_BLOCK.get());
		dropSelf(FABlocks.CHROMIUM_PLATE_BLOCK.get());
		dropSelf(FABlocks.COPPER_SPACE_FRAME.get());
		dropSelf(FABlocks.TIN_SPACE_FRAME.get());
		dropSelf(FABlocks.LEAD_SPACE_FRAME.get());
		dropSelf(FABlocks.IRON_SPACE_FRAME.get());
		dropSelf(FABlocks.BRONZE_SPACE_FRAME.get());
		dropSelf(FABlocks.NICKEL_SPACE_FRAME.get());
		dropSelf(FABlocks.SILVER_SPACE_FRAME.get());
		dropSelf(FABlocks.MAGMATIC_BRASS_SPACE_FRAME.get());
		dropSelf(FABlocks.PIG_IRON_SPACE_FRAME.get());
		dropSelf(FABlocks.STEEL_SPACE_FRAME.get());
		dropSelf(FABlocks.ALUMINUM_SPACE_FRAME.get());
		dropSelf(FABlocks.ALUMINUM_BRONZE_SPACE_FRAME.get());
		dropSelf(FABlocks.CHROMIUM_SPACE_FRAME.get());
		dropSelf(FABlocks.BRICK_TILES.get());
		FABlocks.CHOPPING_BLOCKS.values().forEach(b -> dropSelf(b.get()));
		dropSelf(FABlocks.LOG_PILE.get());
		dropSelf(FABlocks.LIMONITE_CHARCOAL_MIX.get());
		dropSelf(FABlocks.STONE_CRUCIBLE.get());
		dropSelf(FABlocks.STONE_CASTING_VESSEL.get());
		dropSelf(FABlocks.STONE_WORKBENCH.get());
		dropSelf(FABlocks.BRICK_MAKER_FRAME.get());
		dropSelf(FABlocks.BRICK_CRUCIBLE.get());
		dropSelf(FABlocks.BRICK_CASTING_VESSEL.get());
		dropSelf(FABlocks.WOOD_POWER_SHAFT.get());
		dropSelf(FABlocks.WOOD_GEARBOX.get());
		dropSelf(FABlocks.WOOD_SPLITTER.get());
		dropSelf(FABlocks.WOOD_JOINER.get());
		dropSelf(FABlocks.WOOD_BEVEL_GEAR.get());
		dropSelf(FABlocks.IRON_POWER_SHAFT.get());
		dropSelf(FABlocks.IRON_GEARBOX.get());
		dropSelf(FABlocks.IRON_SPLITTER.get());
		dropSelf(FABlocks.IRON_JOINER.get());
		dropSelf(FABlocks.IRON_BEVEL_GEAR.get());
		dropSelf(FABlocks.MILLSTONE.get());
		dropSelf(FABlocks.BRICK_FIREBOX.get());
		dropSelf(FABlocks.PAPER_BELLOWS.get());
		dropSelf(FABlocks.LEATHER_BELLOWS.get());
		dropSelf(FABlocks.HAND_CRANK.get());
		dropSelf(FABlocks.SMALL_WATERWHEEL.get());
		dropSelf(FABlocks.LARGE_WATERWHEEL.get());
		dropSelf(FABlocks.HORSE_ENGINE.get());
		dropSelf(FABlocks.TRIP_HAMMER.get());
		dropSelf(FABlocks.TUMBLING_BARREL.get());
		dropSelf(FABlocks.FRYING_PAN.get());
		dropSelf(FABlocks.BAMBOO_BASKET.get());
		dropSelf(FABlocks.WOODEN_TANK.get());
		dropSelf(FABlocks.COPPER_PIPE.get());
		dropSelf(FABlocks.SCREW_PUMP.get());
		dropSelf(FABlocks.BRICK_KILN.get());
		dropSelf(FABlocks.STEAM_OVEN.get());
	}

	private LootPoolSingletonContainer.Builder<?> getStickDrops(Block b)
	{
		return applyExplosionDecay(b, LootItem.lootTableItem(Items.STICK)
											  .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
											  .when(BonusLevelTableCondition.bonusLevelFlatChance(fortune, NORMAL_LEAVES_STICK_CHANCES)));
	}

	private void dropOre(DeferredBlock<Block> ore, DeferredItem<Item> rawOre, int count)
	{
		add(ore.get(), this.createSilkTouchDispatchTable(ore.get(), this.applyExplosionDecay(ore.get(),
				LootItem.lootTableItem(rawOre.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(count))).apply(ApplyBonusCount.addOreBonusCount(fortune)))));
	}
}
