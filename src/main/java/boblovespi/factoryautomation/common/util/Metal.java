package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.FATags;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Metal
{
	private static final Map<String, Metal> LOOKUP_MAP = HashMap.newHashMap(12);
	private static final List<Metal> ID_MAP = new ArrayList<>(12);
	private static int nextId = 0;

	static final int UNITS_IN_INGOT = 2 * 9;

	public static final Metal IRON = new Metal("iron", 0xFFEAEEF2, 1538, 447, 7870, FATags.Items.IRON_MELTABLE);
	public static final Metal GOLD = new Metal("gold", 0xFFFAF437, 10000, 0, 0, FATags.Items.GOLD_MELTABLE);
	public static final Metal COPPER = new Metal("copper", 0xFFFF973D, 1048, 385, 8933, FATags.Items.COPPER_MELTABLE);
	public static final Metal TIN = new Metal("tin", 0xFFF7E8E8, 232, 227, 7310, FATags.Items.TIN_MELTABLE);
	public static final Metal BRONZE = new Metal("bronze", 0xFFFFB201, 950, 0, 0, FATags.Items.BRONZE_MELTABLE);
	public static final Metal STEEL = new Metal("steel", 0xFF000000, 10000, 0, 0, FATags.Items.STEEL_MELTABLE);
	public static final Metal UNKNOWN = new Metal("unknown", 0x00000000, 10000, 0, 0, null);

	private final String name;
	private final int color;
	private final int meltTemp;
	@Nullable
	private final TagKey<Item> meltables;
	private final int id;
	private final float massHeatCapacity;
	private final float density;

	public Metal(String name, int color, int meltTemp, float massHeatCapacity, float density, @Nullable TagKey<Item> meltables)
	{
		this.name = name;
		this.color = color;
		this.meltTemp = meltTemp + 273;
		this.meltables = meltables;
		this.massHeatCapacity = massHeatCapacity;
		this.density = density;
		LOOKUP_MAP.put(name, this);
		ID_MAP.add(this);
		this.id = nextId;
		nextId++;
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

	public static Metal fromId(int id)
	{
		if (id >= nextId)
			return UNKNOWN;
		return ID_MAP.get(id);
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

	public float massHeatCapacity()
	{
		return massHeatCapacity;
	}

	public float density()
	{
		return density;
	}

	public int id()
	{
		return id;
	}

	public int color()
	{
		return color;
	}
}
