package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.item.FAItems;
import boblovespi.factoryautomation.common.util.Form;
import boblovespi.factoryautomation.common.util.GearMaterial;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class FAItemTagProvider extends ItemTagsProvider
{

	public FAItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags,
							 @Nullable ExistingFileHelper existingFileHelper)
	{
		super(output, lookupProvider, blockTags, FactoryAutomation.MODID, existingFileHelper);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(ItemTags.SHOVELS).add(FAItems.FLINT_SHOVEL.get(), FAItems.COPPER_SHOVEL.get());
		tag(ItemTags.PICKAXES).add(FAItems.FLINT_PICKAXE.get(), FAItems.COPPER_PICKAXE.get());
		tag(ItemTags.AXES).add(FAItems.CHOPPING_BLADE.get(), FAItems.FLINT_AXE.get(), FAItems.COPPER_AXE.get());
		tag(ItemTags.HOES).add(FAItems.FLINT_HOE.get(), FAItems.COPPER_HOE.get());
		tag(ItemTags.SWORDS).add(FAItems.FLINT_SWORD.get(), FAItems.COPPER_SWORD.get());
		tag(FATags.Items.HAMMERS).add(FAItems.COPPER_HAMMER.get(), FAItems.IRON_HAMMER.get());
		tag(FATags.Items.WRENCHES).add(FAItems.IRON_WRENCH.get());

		tag(ItemTags.DURABILITY_ENCHANTABLE).addTags(FATags.Items.HAMMERS, FATags.Items.FA_GEARS, FATags.Items.WRENCHES).add(FAItems.FIREBOW.get());
		tag(ItemTags.MINING_ENCHANTABLE).addTags(FATags.Items.HAMMERS);
		tag(ItemTags.MINING_LOOT_ENCHANTABLE).addTags(FATags.Items.HAMMERS);
		tag(ItemTags.WEAPON_ENCHANTABLE).addTags(FATags.Items.HAMMERS);

		tag(Tags.Items.FOODS_BREAD).add(FAItems.TOASTED_BREAD.get());
		tag(Tags.Items.TOOLS_IGNITER).add(FAItems.FIREBOW.get());

		tag(Tags.Items.INGOTS).addTags(FATags.Items.TIN_INGOT, FATags.Items.LEAD_INGOT, FATags.Items.BRONZE_INGOT, FATags.Items.NICKEL_INGOT, FATags.Items.SILVER_INGOT,
				FATags.Items.MAGMATIC_BRASS_INGOT, FATags.Items.PIG_IRON_INGOT, FATags.Items.STEEL_INGOT, FATags.Items.ALUMINUM_INGOT, FATags.Items.ALUMINUM_BRONZE_INGOT,
				FATags.Items.CHROMIUM_INGOT)
				.add(FAItems.ANCIENT_IRON_INGOT.get(), FAItems.WEAK_IRON_INGOT.get());
		tag(Tags.Items.NUGGETS).addTags(FATags.Items.COPPER_NUGGET, FATags.Items.TIN_NUGGET, FATags.Items.LEAD_NUGGET, FATags.Items.BRONZE_NUGGET, FATags.Items.NICKEL_NUGGET,
				FATags.Items.SILVER_NUGGET, FATags.Items.MAGMATIC_BRASS_NUGGET, FATags.Items.PIG_IRON_NUGGET, FATags.Items.STEEL_NUGGET, FATags.Items.ALUMINUM_NUGGET,
				FATags.Items.ALUMINUM_BRONZE_NUGGET, FATags.Items.CHROMIUM_NUGGET).add(FAItems.ANCIENT_IRON_NUGGET.get(), FAItems.WEAK_IRON_NUGGET.get());
		tag(Tags.Items.RODS).addTags(FATags.Items.COPPER_ROD, FATags.Items.TIN_ROD, FATags.Items.IRON_ROD, FATags.Items.LEAD_ROD, FATags.Items.BRONZE_ROD, FATags.Items.NICKEL_ROD,
				FATags.Items.SILVER_ROD, FATags.Items.MAGMATIC_BRASS_ROD, FATags.Items.PIG_IRON_ROD, FATags.Items.STEEL_ROD, FATags.Items.ALUMINUM_ROD,
				FATags.Items.ALUMINUM_BRONZE_ROD, FATags.Items.CHROMIUM_ROD);
		tag(FATags.Items.SHEETS).addTags(FATags.Items.COPPER_SHEET, FATags.Items.TIN_SHEET, FATags.Items.LEAD_SHEET, FATags.Items.IRON_SHEET, FATags.Items.BRONZE_SHEET,
				FATags.Items.NICKEL_SHEET, FATags.Items.SILVER_SHEET, FATags.Items.MAGMATIC_BRASS_SHEET, FATags.Items.ALUMINUM_SHEET, FATags.Items.ALUMINUM_BRONZE_SHEET,
				FATags.Items.PIG_IRON_SHEET, FATags.Items.STEEL_SHEET, FATags.Items.CHROMIUM_SHEET);
		tag(FATags.Items.FA_GEARS).add(FAItems.GEARS.get(GearMaterial.COPPER).get(), FAItems.GEARS.get(GearMaterial.IRON).get(), FAItems.GEARS.get(GearMaterial.BRONZE).get())
								  .add(FAItems.GEARS.get(GearMaterial.STEEL).get())
								  .add(FAItems.GEARS.get(GearMaterial.WOOD).get(), FAItems.GEARS.get(GearMaterial.STONE).get());
		tag(FATags.Items.GEARS).addTags(FATags.Items.COPPER_GEAR, FATags.Items.TIN_GEAR, FATags.Items.IRON_GEAR, FATags.Items.LEAD_GEAR, FATags.Items.BRONZE_GEAR,
									   FATags.Items.NICKEL_GEAR, FATags.Items.SILVER_GEAR, FATags.Items.MAGMATIC_BRASS_GEAR, FATags.Items.PIG_IRON_GEAR, FATags.Items.STEEL_GEAR,
									   FATags.Items.ALUMINUM_GEAR, FATags.Items.ALUMINUM_BRONZE_GEAR, FATags.Items.CHROMIUM_GEAR)
							   .addTags(FATags.Items.WOOD_GEAR)
							   .add(FAItems.GEARS.get(GearMaterial.STONE).get());
		tag(Tags.Items.RAW_MATERIALS).addTags(FATags.Items.RAW_TIN);

		copy(FATags.Blocks.TIN_BLOCK, FATags.Items.TIN_BLOCK);
		copy(FATags.Blocks.LEAD_BLOCK, FATags.Items.LEAD_BLOCK);
		copy(FATags.Blocks.BRONZE_BLOCK, FATags.Items.BRONZE_BLOCK);
		copy(FATags.Blocks.NICKEL_BLOCK, FATags.Items.NICKEL_BLOCK);
		copy(FATags.Blocks.SILVER_BLOCK, FATags.Items.SILVER_BLOCK);
		copy(FATags.Blocks.MAGMATIC_BRASS_BLOCK, FATags.Items.MAGMATIC_BRASS_BLOCK);
		copy(FATags.Blocks.PIG_IRON_BLOCK, FATags.Items.PIG_IRON_BLOCK);
		copy(FATags.Blocks.STEEL_BLOCK, FATags.Items.STEEL_BLOCK);
		copy(FATags.Blocks.ALUMINUM_BLOCK, FATags.Items.ALUMINUM_BLOCK);
		copy(FATags.Blocks.ALUMINUM_BRONZE_BLOCK, FATags.Items.ALUMINUM_BRONZE_BLOCK);
		copy(FATags.Blocks.CHROMIUM_BLOCK, FATags.Items.CHROMIUM_BLOCK);

		tag(FATags.Items.COPPER_NUGGET).add(FAItems.COPPER_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.COPPER_SHEET).add(FAItems.COPPER_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.COPPER_ROD).add(FAItems.COPPER_THINGS.get(Form.ROD).get());
		tag(FATags.Items.COPPER_GEAR).add(FAItems.GEARS.get(GearMaterial.COPPER).get());
		tag(FATags.Items.TIN_INGOT).add(FAItems.TIN_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.TIN_NUGGET).add(FAItems.TIN_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.TIN_SHEET).add(FAItems.TIN_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.TIN_ROD).add(FAItems.TIN_THINGS.get(Form.ROD).get());
		tag(FATags.Items.TIN_GEAR);
		tag(FATags.Items.IRON_SHEET).add(FAItems.IRON_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.IRON_ROD).add(FAItems.IRON_THINGS.get(Form.ROD).get());
		tag(FATags.Items.IRON_GEAR).add(FAItems.GEARS.get(GearMaterial.IRON).get());
		tag(FATags.Items.LEAD_INGOT).add(FAItems.LEAD_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.LEAD_NUGGET).add(FAItems.LEAD_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.LEAD_SHEET).add(FAItems.LEAD_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.LEAD_ROD).add(FAItems.LEAD_THINGS.get(Form.ROD).get());
		tag(FATags.Items.LEAD_GEAR);
		tag(FATags.Items.BRONZE_INGOT).add(FAItems.BRONZE_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.BRONZE_NUGGET).add(FAItems.BRONZE_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.BRONZE_SHEET).add(FAItems.BRONZE_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.BRONZE_ROD).add(FAItems.BRONZE_THINGS.get(Form.ROD).get());
		tag(FATags.Items.BRONZE_GEAR).add(FAItems.GEARS.get(GearMaterial.BRONZE).get());
		tag(FATags.Items.NICKEL_INGOT).add(FAItems.NICKEL_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.NICKEL_NUGGET).add(FAItems.NICKEL_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.NICKEL_SHEET).add(FAItems.NICKEL_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.NICKEL_ROD).add(FAItems.NICKEL_THINGS.get(Form.ROD).get());
		tag(FATags.Items.NICKEL_GEAR);
		tag(FATags.Items.SILVER_INGOT).add(FAItems.SILVER_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.SILVER_NUGGET).add(FAItems.SILVER_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.SILVER_SHEET).add(FAItems.SILVER_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.SILVER_ROD).add(FAItems.SILVER_THINGS.get(Form.ROD).get());
		tag(FATags.Items.SILVER_GEAR);
		tag(FATags.Items.MAGMATIC_BRASS_INGOT).add(FAItems.MAGMATIC_BRASS_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.MAGMATIC_BRASS_NUGGET).add(FAItems.MAGMATIC_BRASS_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.MAGMATIC_BRASS_SHEET).add(FAItems.MAGMATIC_BRASS_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.MAGMATIC_BRASS_ROD).add(FAItems.MAGMATIC_BRASS_THINGS.get(Form.ROD).get());
		tag(FATags.Items.MAGMATIC_BRASS_GEAR);
		tag(FATags.Items.PIG_IRON_INGOT).add(FAItems.PIG_IRON_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.PIG_IRON_NUGGET).add(FAItems.PIG_IRON_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.PIG_IRON_SHEET).add(FAItems.PIG_IRON_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.PIG_IRON_ROD).add(FAItems.PIG_IRON_THINGS.get(Form.ROD).get());
		tag(FATags.Items.PIG_IRON_GEAR);
		tag(FATags.Items.STEEL_INGOT).add(FAItems.STEEL_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.STEEL_NUGGET).add(FAItems.STEEL_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.STEEL_SHEET).add(FAItems.STEEL_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.STEEL_ROD).add(FAItems.STEEL_THINGS.get(Form.ROD).get());
		tag(FATags.Items.STEEL_GEAR).add(FAItems.GEARS.get(GearMaterial.STEEL).get());
		tag(FATags.Items.ALUMINUM_INGOT).add(FAItems.ALUMINUM_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.ALUMINUM_NUGGET).add(FAItems.ALUMINUM_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.ALUMINUM_SHEET).add(FAItems.ALUMINUM_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.ALUMINUM_ROD).add(FAItems.ALUMINUM_THINGS.get(Form.ROD).get());
		tag(FATags.Items.ALUMINUM_GEAR);
		tag(FATags.Items.ALUMINUM_BRONZE_INGOT).add(FAItems.ALUMINUM_BRONZE_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.ALUMINUM_BRONZE_NUGGET).add(FAItems.ALUMINUM_BRONZE_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.ALUMINUM_BRONZE_SHEET).add(FAItems.ALUMINUM_BRONZE_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.ALUMINUM_BRONZE_ROD).add(FAItems.ALUMINUM_BRONZE_THINGS.get(Form.ROD).get());
		tag(FATags.Items.ALUMINUM_BRONZE_GEAR);
		tag(FATags.Items.CHROMIUM_INGOT).add(FAItems.CHROMIUM_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.CHROMIUM_NUGGET).add(FAItems.CHROMIUM_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.CHROMIUM_SHEET).add(FAItems.CHROMIUM_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.CHROMIUM_ROD).add(FAItems.CHROMIUM_THINGS.get(Form.ROD).get());
		tag(FATags.Items.CHROMIUM_GEAR);

		tag(FATags.Items.WOOD_GEAR).add(FAItems.GEARS.get(GearMaterial.WOOD).get());

		tag(FATags.Items.RAW_TIN).add(FAItems.RAW_CASSITERITE.get());

		tag(FATags.Items.SILKS_GRASS).add(Items.SHEARS, FAItems.CHOPPING_BLADE.get());
		tag(FATags.Items.GOOD_AXES).add(Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);

		copy(FATags.Blocks.CHOPPING_BLOCKS, FATags.Items.CHOPPING_BLOCKS);
		tag(FATags.Items.STONE_FOUNDRY_PONDER).add(FAItems.STONE_CRUCIBLE.get(), FAItems.STONE_CASTING_VESSEL.get(), Items.FURNACE);

		tag(FATags.Items.COPPER_MELTABLE).addTags(Tags.Items.INGOTS_COPPER, Tags.Items.RAW_MATERIALS_COPPER, FATags.Items.COPPER_NUGGET, FATags.Items.COPPER_SHEET)
										 .addTags(FATags.Items.COPPER_ROD, FATags.Items.COPPER_GEAR)
										 .add(FAItems.COPPER_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.TIN_MELTABLE).addTags(FATags.Items.TIN_INGOT, FATags.Items.TIN_NUGGET, FATags.Items.TIN_BLOCK, FATags.Items.TIN_SHEET, FATags.Items.TIN_ROD)
									  .addTags(FATags.Items.TIN_GEAR, FATags.Items.RAW_TIN)
									  .add(FAItems.TIN_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.IRON_MELTABLE).addTags(Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON, FATags.Items.IRON_SHEET, FATags.Items.IRON_ROD, FATags.Items.IRON_GEAR)
									   .addTags(Tags.Items.RAW_MATERIALS_IRON)
									   .add(FAItems.IRON_SHARD.get(), FAItems.IRON_THINGS.get(Form.PLATE_BLOCK).get())
									   .add(FAItems.ANCIENT_IRON_INGOT.get(), FAItems.WEAK_IRON_INGOT.get(), FAItems.ANCIENT_IRON_NUGGET.get(), FAItems.WEAK_IRON_NUGGET.get())
									   .add(FAItems.ANCIENT_IRON_BLOCK.get(), FAItems.WEAK_IRON_BLOCK.get());
		tag(FATags.Items.LEAD_MELTABLE).addTags(FATags.Items.LEAD_INGOT, FATags.Items.LEAD_NUGGET, FATags.Items.LEAD_BLOCK, FATags.Items.LEAD_SHEET, FATags.Items.LEAD_ROD)
									   .addTags(FATags.Items.LEAD_GEAR)
									   .add(FAItems.LEAD_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.BRONZE_MELTABLE).addTags(FATags.Items.BRONZE_INGOT, FATags.Items.BRONZE_NUGGET, FATags.Items.BRONZE_BLOCK, FATags.Items.BRONZE_SHEET)
										 .addTags(FATags.Items.BRONZE_ROD, FATags.Items.BRONZE_GEAR)
										 .add(FAItems.BRONZE_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.NICKEL_MELTABLE).addTags(FATags.Items.NICKEL_INGOT, FATags.Items.NICKEL_NUGGET, FATags.Items.NICKEL_BLOCK, FATags.Items.NICKEL_SHEET,
												 FATags.Items.NICKEL_ROD)
										 .addTags(FATags.Items.NICKEL_GEAR).add(FAItems.NICKEL_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.SILVER_MELTABLE).addTags(FATags.Items.SILVER_INGOT, FATags.Items.SILVER_NUGGET, FATags.Items.SILVER_BLOCK, FATags.Items.SILVER_SHEET,
												 FATags.Items.SILVER_ROD)
										 .addTags(FATags.Items.SILVER_GEAR)
										 .add(FAItems.SILVER_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.MAGMATIC_BRASS_MELTABLE).addTags(FATags.Items.MAGMATIC_BRASS_INGOT, FATags.Items.MAGMATIC_BRASS_NUGGET, FATags.Items.MAGMATIC_BRASS_BLOCK,
														 FATags.Items.MAGMATIC_BRASS_SHEET, FATags.Items.MAGMATIC_BRASS_ROD)
												 .addTags(FATags.Items.MAGMATIC_BRASS_GEAR)
												 .add(FAItems.MAGMATIC_BRASS_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.PIG_IRON_MELTABLE).addTags(FATags.Items.PIG_IRON_INGOT, FATags.Items.PIG_IRON_NUGGET, FATags.Items.PIG_IRON_BLOCK, FATags.Items.PIG_IRON_SHEET,
												   FATags.Items.PIG_IRON_ROD)
										   .addTags(FATags.Items.PIG_IRON_GEAR)
										   .add(FAItems.PIG_IRON_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.STEEL_MELTABLE).addTags(FATags.Items.STEEL_INGOT, FATags.Items.STEEL_NUGGET, FATags.Items.STEEL_BLOCK, FATags.Items.STEEL_SHEET)
										.addTags(FATags.Items.STEEL_ROD, FATags.Items.STEEL_GEAR)
										.add(FAItems.STEEL_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.ALUMINUM_MELTABLE).addTags(FATags.Items.ALUMINUM_INGOT, FATags.Items.ALUMINUM_NUGGET, FATags.Items.ALUMINUM_BLOCK, FATags.Items.ALUMINUM_SHEET,
												   FATags.Items.ALUMINUM_ROD)
										   .addTags(FATags.Items.ALUMINUM_GEAR)
										   .add(FAItems.ALUMINUM_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.ALUMINUM_BRONZE_MELTABLE).addTags(FATags.Items.ALUMINUM_BRONZE_INGOT, FATags.Items.ALUMINUM_BRONZE_NUGGET, FATags.Items.ALUMINUM_BRONZE_BLOCK,
														  FATags.Items.ALUMINUM_BRONZE_SHEET, FATags.Items.ALUMINUM_BRONZE_ROD)
												  .addTags(FATags.Items.ALUMINUM_BRONZE_GEAR)
												  .add(FAItems.ALUMINUM_BRONZE_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.CHROMIUM_MELTABLE).addTags(FATags.Items.CHROMIUM_INGOT, FATags.Items.CHROMIUM_NUGGET, FATags.Items.CHROMIUM_BLOCK, FATags.Items.CHROMIUM_SHEET,
												   FATags.Items.CHROMIUM_ROD)
										   .addTags(FATags.Items.CHROMIUM_GEAR)
										   .add(FAItems.CHROMIUM_THINGS.get(Form.PLATE_BLOCK).get());

		tag(FATags.Items.SHARDS).add(FAItems.IRON_SHARD.get());

		tag(FATags.Items.WHEAT_DUST).add(FAItems.WHEAT_FLOUR.get());
		tag(FATags.Items.CALCIUM_CARBONATE_DUST).add(FAItems.CALCITE_DUST.get());
		tag(FATags.Items.QUICKLIME_DUST).add(FAItems.QUICKLIME.get());
		tag(Tags.Items.BRICKS).add(FAItems.MUD_BRICK.get(), FAItems.DRIED_BRICK.get());
		tag(Tags.Items.DUSTS).addTags(FATags.Items.WHEAT_DUST, FATags.Items.CALCIUM_CARBONATE_DUST, FATags.Items.QUICKLIME_DUST);
	}
}
