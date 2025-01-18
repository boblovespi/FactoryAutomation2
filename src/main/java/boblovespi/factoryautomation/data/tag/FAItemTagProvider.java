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

		tag(Tags.Items.FOODS_BREAD).add(FAItems.TOASTED_BREAD.get());
		tag(Tags.Items.TOOLS_IGNITER).add(FAItems.FIREBOW.get());

		tag(Tags.Items.INGOTS).addTags(FATags.Items.TIN_INGOT, FATags.Items.BRONZE_INGOT, FATags.Items.STEEL_INGOT);
		tag(Tags.Items.NUGGETS).addTags(FATags.Items.COPPER_NUGGET, FATags.Items.TIN_NUGGET, FATags.Items.BRONZE_NUGGET, FATags.Items.STEEL_NUGGET);
		tag(Tags.Items.RODS).addTags(FATags.Items.COPPER_ROD, FATags.Items.TIN_ROD, FATags.Items.IRON_ROD, FATags.Items.BRONZE_ROD, FATags.Items.STEEL_ROD);
		tag(FATags.Items.GEARS).addTags(FATags.Items.COPPER_GEAR, FATags.Items.TIN_GEAR, FATags.Items.IRON_GEAR, FATags.Items.BRONZE_GEAR, FATags.Items.STEEL_GEAR)
							   .addTags(FATags.Items.WOOD_GEAR);
		tag(Tags.Items.RAW_MATERIALS).addTags(FATags.Items.RAW_TIN);

		copy(FATags.Blocks.TIN_BLOCK, FATags.Items.TIN_BLOCK);
		copy(FATags.Blocks.BRONZE_BLOCK, FATags.Items.BRONZE_BLOCK);
		copy(FATags.Blocks.STEEL_BLOCK, FATags.Items.STEEL_BLOCK);

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
		tag(FATags.Items.BRONZE_INGOT).add(FAItems.BRONZE_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.BRONZE_NUGGET).add(FAItems.BRONZE_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.BRONZE_SHEET).add(FAItems.BRONZE_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.BRONZE_ROD).add(FAItems.BRONZE_THINGS.get(Form.ROD).get());
		tag(FATags.Items.BRONZE_GEAR).add(FAItems.GEARS.get(GearMaterial.BRONZE).get());
		tag(FATags.Items.STEEL_INGOT).add(FAItems.STEEL_THINGS.get(Form.INGOT).get());
		tag(FATags.Items.STEEL_NUGGET).add(FAItems.STEEL_THINGS.get(Form.NUGGET).get());
		tag(FATags.Items.STEEL_SHEET).add(FAItems.STEEL_THINGS.get(Form.SHEET).get());
		tag(FATags.Items.STEEL_ROD).add(FAItems.STEEL_THINGS.get(Form.ROD).get());
		tag(FATags.Items.STEEL_GEAR).add(FAItems.GEARS.get(GearMaterial.STEEL).get());

		tag(FATags.Items.WOOD_GEAR).add(FAItems.GEARS.get(GearMaterial.WOOD).get());

		tag(FATags.Items.RAW_TIN).add(FAItems.RAW_CASSITERITE.get());

		tag(FATags.Items.SHEETS).addTags(FATags.Items.COPPER_SHEET, FATags.Items.TIN_SHEET, FATags.Items.IRON_SHEET, FATags.Items.BRONZE_SHEET, FATags.Items.STEEL_SHEET);

		tag(FATags.Items.SILKS_GRASS).add(Items.SHEARS, FAItems.CHOPPING_BLADE.get());
		tag(FATags.Items.GOOD_AXES).add(Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);

		copy(FATags.Blocks.CHOPPING_BLOCKS, FATags.Items.CHOPPING_BLOCKS);

		tag(FATags.Items.IRON_MELTABLE).addTags(Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON, FATags.Items.IRON_SHEET, FATags.Items.IRON_ROD, FATags.Items.IRON_GEAR)
									   .addTags(Tags.Items.RAW_MATERIALS_IRON)
									   .add(FAItems.IRON_SHARD.get(), FAItems.IRON_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.COPPER_MELTABLE).addTags(Tags.Items.INGOTS_COPPER, Tags.Items.RAW_MATERIALS_COPPER, FATags.Items.COPPER_NUGGET, FATags.Items.COPPER_SHEET)
										 .addTags(FATags.Items.COPPER_ROD, FATags.Items.COPPER_GEAR)
										 .add(FAItems.COPPER_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.TIN_MELTABLE).addTags(FATags.Items.TIN_INGOT, FATags.Items.TIN_NUGGET, FATags.Items.TIN_BLOCK, FATags.Items.TIN_SHEET, FATags.Items.TIN_ROD)
									  .addTags(FATags.Items.TIN_GEAR, FATags.Items.RAW_TIN)
									  .add(FAItems.TIN_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.BRONZE_MELTABLE).addTags(FATags.Items.BRONZE_INGOT, FATags.Items.BRONZE_NUGGET, FATags.Items.BRONZE_BLOCK, FATags.Items.BRONZE_SHEET)
										 .addTags(FATags.Items.BRONZE_ROD, FATags.Items.BRONZE_GEAR)
										 .add(FAItems.BRONZE_THINGS.get(Form.PLATE_BLOCK).get());
		tag(FATags.Items.STEEL_MELTABLE).addTags(FATags.Items.STEEL_INGOT, FATags.Items.STEEL_NUGGET, FATags.Items.STEEL_BLOCK, FATags.Items.STEEL_SHEET)
										.addTags(FATags.Items.STEEL_ROD, FATags.Items.STEEL_GEAR)
										.add(FAItems.STEEL_THINGS.get(Form.PLATE_BLOCK).get());

		tag(FATags.Items.SHARDS).add(FAItems.IRON_SHARD.get());

		tag(FATags.Items.WHEAT_DUST).add(FAItems.WHEAT_FLOUR.get());
		tag(FATags.Items.CALCIUM_CARBONATE_DUST).add(FAItems.CALCITE_DUST.get());
		tag(FATags.Items.QUICKLIME_DUST).add(FAItems.QUICKLIME.get());
		tag(Tags.Items.BRICKS).add(FAItems.MUD_BRICK.get(), FAItems.DRIED_BRICK.get());
		tag(Tags.Items.DUSTS).addTags(FATags.Items.WHEAT_DUST, FATags.Items.CALCIUM_CARBONATE_DUST, FATags.Items.QUICKLIME_DUST);
	}
}
