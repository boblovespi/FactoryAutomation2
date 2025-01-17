package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.common.item.FAItems;
import net.minecraft.world.item.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

public class GearMaterial
{
	private static final Map<String, GearMaterial> LOOKUP_MAP = HashMap.newHashMap(12);
	private static final List<GearMaterial> ID_MAP = new ArrayList<>(12);
	private static int nextId = 0;

	public static final GearMaterial WOOD = new GearMaterial("wood", 1, 40);
	public static final GearMaterial STONE = new GearMaterial("stone", 2, 50);
	public static final GearMaterial COPPER = new GearMaterial("copper", 1, 200);
	public static final GearMaterial IRON = new GearMaterial("iron", 2, 600);
	// public static final GearMaterial GOLD = new GearMaterial("gold", 3, 400);
	public static final GearMaterial BRONZE = new GearMaterial("bronze", 5, 600);
	// public static final GearMaterial DIAMOND = new GearMaterial("diamond", 4, 900);
	// public static final GearMaterial MAGMATIC_BRASS = new GearMaterial("magmatic_brass", 8, 900);
	public static final GearMaterial STEEL = new GearMaterial("steel", 16, 1200);
	// public static final GearMaterial ALUMINUM = new GearMaterial("aluminum", 6, 900);
	// public static final GearMaterial ALUMINUM_BRONZE = new GearMaterial("aluminum_bronze", 7, 900);
	public static final GearMaterial NONE = new GearMaterial("none", 0, 0);

	private final String name;
	private final int scaleFactor;
	private final int durability;
	private final int id;

	public GearMaterial(String name, int scaleFactor, int durability)
	{
		this.name = name;
		this.scaleFactor = scaleFactor;
		this.durability = durability;
		LOOKUP_MAP.put(name, this);
		ID_MAP.add(this);
		this.id = nextId;
		nextId++;
	}

	public static Collection<GearMaterial> all()
	{
		return ID_MAP.stream().filter(g -> g != NONE).collect(Collectors.toList());
	}

	public static GearMaterial fromStack(ItemStack stack)
	{
		return ID_MAP.stream().filter(gear -> gear != NONE && stack.is(FAItems.GEARS.get(gear).get())).findFirst().orElse(NONE);
	}

	public String getName()
	{
		return name;
	}

	public int getDurability()
	{
		return durability;
	}

	public float getScaleFactor()
	{
		return scaleFactor;
	}
}
