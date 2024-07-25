package boblovespi.factoryautomation.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class FALootTableProvider extends LootTableProvider
{
	public FALootTableProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pRegistries)
	{
		super(pOutput, Set.of(), List.of(new LootTableProvider.SubProviderEntry(FABlockLootTableProvider::new, LootContextParamSets.BLOCK)), pRegistries);
	}
}
