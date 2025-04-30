package boblovespi.factoryautomation.data.loot;

import boblovespi.factoryautomation.FactoryAutomation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class LootTablePrefixCondition implements LootItemCondition
{
	public static final MapCodec<LootTablePrefixCondition> CODEC = RecordCodecBuilder.mapCodec(
			builder -> builder.group(ResourceLocation.CODEC.fieldOf("loot_table_prefix").forGetter(idCondition -> idCondition.targetPrefix))
							  .apply(builder, LootTablePrefixCondition::new));

	private final ResourceLocation targetPrefix;

	private LootTablePrefixCondition(ResourceLocation targetLootTableIdPrefix)
	{
		targetPrefix = targetLootTableIdPrefix;
	}

	@Override
	public LootItemConditionType getType()
	{
		return FactoryAutomation.LOOT_TABLE_PREFIX_CONDITION.get();
	}

	@Override
	public boolean test(LootContext lootContext)
	{
		return lootContext.getQueriedLootTableId().toString().startsWith(targetPrefix.toString());
	}

	public static LootTablePrefixCondition.Builder builder(ResourceLocation targetPrefix)
	{
		return new LootTablePrefixCondition.Builder(targetPrefix);
	}

	public static class Builder implements LootItemCondition.Builder
	{
		private final ResourceLocation targetPrefix;

		public Builder(ResourceLocation targetPrefix)
		{
			this.targetPrefix = targetPrefix;
		}

		@Override
		public LootItemCondition build()
		{
			return new LootTablePrefixCondition(targetPrefix);
		}
	}
}
