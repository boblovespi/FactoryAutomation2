package boblovespi.factoryautomation.common.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Form
{
	private static final Map<String, Form> LOOKUP_MAP = LinkedHashMap.newLinkedHashMap(12);

	public static final Form INGOT = new Form("ingot", Metal.UNITS_IN_INGOT, Tags.Items.INGOTS, true);
	public static final Form NUGGET = new Form("nugget", Metal.UNITS_IN_INGOT / 9, Tags.Items.NUGGETS, true);
	public static final Form BLOCK = new Form("block", Metal.UNITS_IN_INGOT * 9, Tags.Items.STORAGE_BLOCKS, true);
	public static final Form SHEET = new Form("sheet", Metal.UNITS_IN_INGOT, null, true);
	public static final Form ROD = new Form("rod", Metal.UNITS_IN_INGOT / 2, Tags.Items.RODS, true);
	public static final Form GEAR = new Form("gear", Metal.UNITS_IN_INGOT * 4, null, true);
	public static final Form PLATE_BLOCK = new Form("gear", Metal.UNITS_IN_INGOT * 6, null, true);
	public static final Form RAW_ORE = new Form("raw_ore", Metal.UNITS_IN_INGOT, Tags.Items.RAW_MATERIALS, false);
	public static final Form NONE = new Form("none", Integer.MAX_VALUE, null, false);

	private final String name;
	private final int amount;
	@Nullable
	private final TagKey<Item> backingTag;
	private final boolean castable;

	public Form(String name, int amount, @Nullable TagKey<Item> backingTag, boolean castable)
	{
		this.name = name;
		this.amount = amount;
		this.backingTag = backingTag;
		this.castable = castable;
		LOOKUP_MAP.put(name, this);
	}

	public static Form fromStack(ItemStack stack)
	{
		for (var form : LOOKUP_MAP.values())
		{
			if (form.backingTag == null)
				continue;
			if (stack.is(form.backingTag))
				return form;
		}
		return NONE;
	}

	public static Collection<Form> copper()
	{
		return LOOKUP_MAP.values().stream().filter(f -> f != INGOT && f != BLOCK && f != GEAR && f.castable).collect(Collectors.toList());
	}

	public static Collection<Form> most()
	{
		return LOOKUP_MAP.values().stream().filter(f -> f != GEAR && f.castable).collect(Collectors.toList());
	}

	public int amount()
	{
		return amount;
	}

	public float fractionOfBlock()
	{
		return (float) amount / BLOCK.amount();
	}

	@Nullable
	public TagKey<Item> getTag()
	{
		return backingTag;
	}

	public String getName()
	{
		return name;
	}
}
