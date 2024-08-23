package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
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
						new LootItemCondition[] {LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("blocks/short_grass")).build(),
								MatchTool.toolMatches(ItemPredicate.Builder.item().of(FATags.Items.SILKS_GRASS)).build()},
						ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/short_grass")),
						HolderSet.direct(BuiltInRegistries.ITEM.wrapAsHolder(Items.WHEAT_SEEDS))
				)
		   );
		add("pigs_drop_tallow",
				new AddTableLootModifier(new LootItemCondition[] {LootTableIdCondition.builder(ResourceLocation.withDefaultNamespace("entities/pig")).build()},
						ResourceKey.create(Registries.LOOT_TABLE, FactoryAutomation.name("overrides/pig"))));
	}
}
