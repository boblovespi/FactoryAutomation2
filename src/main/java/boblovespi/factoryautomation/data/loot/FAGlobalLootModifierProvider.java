package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import net.neoforged.neoforge.common.loot.AddTableLootModifier;
import net.neoforged.neoforge.common.loot.LootTableIdCondition;

import java.util.concurrent.CompletableFuture;

public class FAGlobalLootModifierProvider extends GlobalLootModifierProvider
{
	public FAGlobalLootModifierProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
	{
		super(output, registries, FactoryAutomation.MODID);
	}

	@Override
	protected void start()
	{
		add("chopping_blade_silks_short_grass",
				new AlternateDropsLootModifier(
						new LootItemCondition[] {
								LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("blocks/short_grass")).build(),
								MatchTool.toolMatches(ItemPredicate.Builder.item().of(FATags.Items.SILKS_GRASS)).build()
						},
						ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/short_grass")),
						HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(Items.WHEAT_SEEDS))
				)
		   );
		add("pigs_drop_tallow",
				new AddTableLootModifier(new LootItemCondition[] {LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/pig")).build()},
						ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/pig"))));
		add("mobs_drop_ancient_iron",
				new ReplaceDropsLootModifier(
						new LootItemCondition[] {
								AnyOfCondition.anyOf(
										LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/iron_golem")),
										LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/zombie")),
										LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/zombie_villager")),
										LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/husk"))
													).build()
						},
						FAItems.ANCIENT_IRON_INGOT.get(),
						HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_INGOT))
				));
		add("chests_drop_weak_iron_ingot",
				new ReplaceDropsLootModifier(
						new LootItemCondition[] {
								AnyOfCondition.anyOf(
										LootTablePrefixCondition.builder(ResourceLocation.withDefaultNamespace("chests/")),
										LootTablePrefixCondition.builder(ResourceLocation.withDefaultNamespace("pots/"))
										).build(),
								LocationCheck.checkLocation(LocationPredicate.Builder.inDimension(Level.OVERWORLD)).build()
						},
						FAItems.WEAK_IRON_INGOT.get(),
						HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_INGOT))
				));
		add("chests_drop_weak_iron_nugget",
				new ReplaceDropsLootModifier(
						new LootItemCondition[] {
								AnyOfCondition.anyOf(
										LootTablePrefixCondition.builder(ResourceLocation.withDefaultNamespace("chests/")),
										LootTablePrefixCondition.builder(ResourceLocation.withDefaultNamespace("pots/"))
								).build(),
								LocationCheck.checkLocation(LocationPredicate.Builder.inDimension(Level.OVERWORLD)).build()
						},
						FAItems.WEAK_IRON_NUGGET.get(),
						HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(Items.IRON_NUGGET))
				));
	}
}
