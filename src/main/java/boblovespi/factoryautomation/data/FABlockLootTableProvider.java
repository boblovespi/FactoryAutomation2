package boblovespi.factoryautomation.data;

import boblovespi.factoryautomation.common.block.FABlocks;
import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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
		for (var rock : FABlocks.ROCKS)
			dropOther(rock.get(), FAItems.ROCK);
		dropOther(FABlocks.FLINT_ROCK.get(), Items.FLINT);
	}
}
