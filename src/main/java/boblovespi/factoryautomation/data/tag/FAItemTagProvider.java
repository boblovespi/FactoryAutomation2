package boblovespi.factoryautomation.data.tag;

import boblovespi.factoryautomation.FactoryAutomation;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
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

	@Override
	protected void addTags(HolderLookup.Provider pProvider)
	{
		tag(ItemTags.SHOVELS).add(FAItems.FLINT_SHOVEL.get());
		tag(ItemTags.PICKAXES).add(FAItems.FLINT_PICKAXE.get());
		tag(ItemTags.AXES).add(FAItems.FLINT_AXE.get());
		tag(ItemTags.HOES).add(FAItems.FLINT_HOE.get());
		tag(ItemTags.SWORDS).add(FAItems.FLINT_SWORD.get());
	}
}
