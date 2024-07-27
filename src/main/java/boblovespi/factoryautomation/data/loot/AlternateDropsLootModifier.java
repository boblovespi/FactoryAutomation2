package boblovespi.factoryautomation.data.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AlternateDropsLootModifier extends LootModifier
{
	public static final MapCodec<AlternateDropsLootModifier> CODEC = RecordCodecBuilder.mapCodec(
			instance -> instance.group(
					IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions),
					ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("table").forGetter(a -> a.table),
					RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("replacable").forGetter(a -> a.replacable)
									  ).apply(instance, AlternateDropsLootModifier::new));

	private final ResourceKey<LootTable> table;
	private final HolderSet<Item> replacable;

	protected AlternateDropsLootModifier(LootItemCondition[] conditionsIn, ResourceKey<LootTable> table, HolderSet<Item> replacable)
	{
		super(conditionsIn);
		this.table = table;
		this.replacable = replacable;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		generatedLoot.removeIf(s -> replacable.contains(s.getItemHolder()));
		context.getResolver().get(Registries.LOOT_TABLE, this.table)
			   .ifPresent(extraTable -> extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), generatedLoot::add)));
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec()
	{
		return CODEC;
	}
}
