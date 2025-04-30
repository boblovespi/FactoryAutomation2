package boblovespi.factoryautomation.data.loot;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ReplaceDropsLootModifier extends LootModifier
{
	public static final MapCodec<ReplaceDropsLootModifier> CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(IGlobalLootModifier.LOOT_CONDITIONS_CODEC.fieldOf("conditions").forGetter(glm -> glm.conditions),
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("replacer").forGetter(a -> a.replacer),
					RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("replacable").forGetter(a -> a.replacable)).apply(i, ReplaceDropsLootModifier::new));

	private final Item replacer;
	private final HolderSet<Item> replacable;

	protected ReplaceDropsLootModifier(LootItemCondition[] conditionsIn, Item replacer, HolderSet<Item> replacable)
	{
		super(conditionsIn);
		this.replacer = replacer;
		this.replacable = replacable;
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		generatedLoot.replaceAll(s -> replacable.contains(s.getItemHolder()) ? new ItemStack(replacer, s.getCount()) : s);
		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec()
	{
		return CODEC;
	}
}
