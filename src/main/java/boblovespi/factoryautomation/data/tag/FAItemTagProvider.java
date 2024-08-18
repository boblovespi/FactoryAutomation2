package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.FATags;
import boblovespi.factoryautomation.common.item.FAItems;
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

		tag(FATags.Items.SILKS_GRASS).add(Items.SHEARS, FAItems.CHOPPING_BLADE.get());
		tag(FATags.Items.GOOD_AXES).add(Items.IRON_AXE, Items.DIAMOND_AXE, Items.NETHERITE_AXE);

		copy(FATags.Blocks.CHOPPING_BLOCKS, FATags.Items.CHOPPING_BLOCKS);

		tag(FATags.Items.IRON_MELTABLE).addTags(Tags.Items.INGOTS_IRON, Tags.Items.NUGGETS_IRON);

		tag(FATags.Items.COPPER_MELTABLE).addTags(Tags.Items.INGOTS_COPPER, Tags.Items.RAW_MATERIALS_COPPER);
	}
}
