package boblovespi.factoryautomation.common.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.Tags;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Form
{
	private static final Map<String, Form> LOOKUP_MAP = HashMap.newHashMap(12);

	public static final Form INGOT = new Form("ingot", Metal.UNITS_IN_INGOT, Tags.Items.INGOTS);
	public static final Form NUGGET = new Form("nugget", Metal.UNITS_IN_INGOT / 9, Tags.Items.NUGGETS);
	public static final Form BLOCK = new Form("block", Metal.UNITS_IN_INGOT * 9, Tags.Items.STORAGE_BLOCKS);
	public static final Form SHEET = new Form("sheet", Metal.UNITS_IN_INGOT, null);
	public static final Form ROD = new Form("rod", Metal.UNITS_IN_INGOT / 2, Tags.Items.RODS);
	public static final Form GEAR = new Form("gear", Metal.UNITS_IN_INGOT * 4, null);
	public static final Form NONE = new Form("none", Integer.MAX_VALUE, null);

	private final String name;
	private final int amount;
	@Nullable
	private final TagKey<Item> backingTag;

	public Form(String name, int amount, @Nullable TagKey<Item> backingTag)
	{
		this.name = name;
		this.amount = amount;
		this.backingTag = backingTag;
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
		return LOOKUP_MAP.values().stream().filter(f -> f != INGOT && f != BLOCK && f != NONE && f != GEAR).collect(Collectors.toList());
	}

	public static Collection<Form> most()
	{
		return LOOKUP_MAP.values().stream().filter(f -> f != BLOCK && f != NONE && f != GEAR).collect(Collectors.toList());
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
