package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class Metal
{
	private static final Map<String, Metal> LOOKUP_MAP = HashMap.newHashMap(12);

	static final int UNITS_IN_INGOT = 2 * 9;

	public static final Metal IRON = new Metal("iron", 1538, FATags.Items.IRON_MELTABLE);
	public static final Metal GOLD = new Metal("gold", 10000, FATags.Items.GOLD_MELTABLE);
	public static final Metal COPPER = new Metal("copper", 1048, FATags.Items.COPPER_MELTABLE);
	public static final Metal TIN = new Metal("tin", 232, FATags.Items.TIN_MELTABLE);
	public static final Metal BRONZE = new Metal("bronze", 950, FATags.Items.BRONZE_MELTABLE);
	public static final Metal STEEL = new Metal("steel", 10000, FATags.Items.STEEL_MELTABLE);
	public static final Metal UNKNOWN = new Metal("unknown", 10000, null);

	private final String name;
	private final int meltTemp;
	@Nullable
	private final TagKey<Item> meltables;

	public Metal(String name, int meltTemp, @Nullable TagKey<Item> meltables)
	{
		this.name = name;
		this.meltTemp = meltTemp;
		this.meltables = meltables;
		LOOKUP_MAP.put(name, this);
	}

	public static Metal fromStack(ItemStack stack)
	{
		for (var metal : LOOKUP_MAP.values())
		{
			if (metal.meltables == null)
				continue;
			if (stack.is(metal.meltables))
				return metal;
		}
		return UNKNOWN;
	}

	public static Metal fromName(String name)
	{
		return LOOKUP_MAP.getOrDefault(name, UNKNOWN);
	}

	public static Item itemForMetalAndForm(Metal metal, Form form)
	{
		if (metal.meltables == null || form.getTag() == null)
			return Items.AIR;
		var first = BuiltInRegistries.ITEM.getOrCreateTag(form.getTag()).stream().filter(k -> k.is(metal.meltables)).findFirst();
		return first.map(Holder::value).orElse(Items.AIR);
	}

	public String getName()
	{
		return name;
	}

	public int meltTemp()
	{
		return meltTemp;
	}
}
