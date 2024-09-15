package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.advancements.*;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class FAAdvancementProvider extends AdvancementProvider
{
	public FAAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper)
	{
		super(output, registries, existingFileHelper, List.of(new Generator()));
	}

	private static class Generator implements AdvancementGenerator
	{
		private Consumer<AdvancementHolder> saver;
		private ExistingFileHelper efh;

		@Override
		public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper)
		{
			this.saver = saver;
			this.efh = existingFileHelper;
			var cbBuilder = getBuilder(new ItemStack(FABlocks.CHOPPING_BLOCK), "stone_age", "root", 100, AdvancementType.TASK,
					ResourceLocation.withDefaultNamespace("textures/block/stone.png"));
			cbBuilder.addCriterion("has_chopping_block", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(FATags.Items.CHOPPING_BLOCKS)));
			var choppingBlock = cbBuilder.save(saver, FactoryAutomation.name("stone_age/root"), existingFileHelper);
			var flintPickaxe = task("flint_pickaxe", "stone_age", FAItems.FLINT_PICKAXE, choppingBlock);
			var stoneFoundry = task("stone_foundry", "stone_age", FAItems.STONE_CRUCIBLE, flintPickaxe);
			var copperIngot = goal("copper_ingot", "stone_age", () -> Items.COPPER_INGOT, stoneFoundry);
			var ciBuilder = getBuilder(new ItemStack(Items.COPPER_INGOT), "copper_age", "root", 0, AdvancementType.TASK,
					ResourceLocation.withDefaultNamespace("textures/block/copper_block.png"));
			ciBuilder.addCriterion("has_copper_ingot", has(() -> Items.COPPER_INGOT));
			var copperIngot2 = ciBuilder.save(saver, FactoryAutomation.name("copper_age/root"), existingFileHelper);
			var copperPickaxe = task("copper_pickaxe", "copper_age", FAItems.COPPER_PICKAXE, copperIngot2);
			var firebow = task("firebow", "copper_age", FAItems.FIREBOW, copperIngot2);
			var stoneWorkbench = task("stone_workbench", "copper_age", FAItems.STONE_WORKBENCH, copperPickaxe);
			var charcoal = task("charcoal", "copper_age", () -> Items.CHARCOAL, firebow);
			var copperHammer = task("copper_hammer", "copper_age", FAItems.COPPER_HAMMER, stoneWorkbench);
			var ironShard = goal("iron_shard", "copper_age", FAItems.IRON_SHARD, copperHammer);
		}

		private static Advancement.Builder getBuilder(ItemStack item, String cat, String name, int xp, AdvancementType type, @Nullable ResourceLocation background)
		{
			var builder = Advancement.Builder.advancement();
			builder.display(item,
					Component.translatable(FactoryAutomation.locString("advancements", cat + "." + name + ".title")),
					Component.translatable(FactoryAutomation.locString("advancements", cat + "." + name + ".description")),
					background, type, true, true, false);
			builder.rewards(AdvancementRewards.Builder.experience(xp));
			return builder;
		}

		private AdvancementHolder of(String name, String cat, Supplier<? extends Item> item, int xp, AdvancementHolder parent, AdvancementType type)
		{
			var builder = getBuilder(new ItemStack(item.get()), cat, name, xp, type, null);
			builder.addCriterion("has_item", has(item));
			builder.parent(parent);
			return builder.save(saver, FactoryAutomation.name(cat + "/" + name), efh);
		}

		private AdvancementHolder task(String name, String cat, Supplier<? extends Item> item, AdvancementHolder parent)
		{
			return of(name, cat, item, 40, parent, AdvancementType.TASK);
		}

		private AdvancementHolder goal(String name, String cat, Supplier<? extends Item> item, AdvancementHolder parent)
		{
			return of(name, cat, item, 100, parent, AdvancementType.GOAL);
		}

		private Criterion<InventoryChangeTrigger.TriggerInstance> has(Supplier<? extends Item> item)
		{
			return InventoryChangeTrigger.TriggerInstance.hasItems(item.get());
		}
	}
}
