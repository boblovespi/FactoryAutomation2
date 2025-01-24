package boblovespi.factoryautomation.common.util;

import java.util.Collection;
import java.util.List;

public enum StoneBlockForms
{
	BLOCK("block", 1),
	STAIRS("stairs", 1),
	SLAB("slab", 2),
	WALL("wall", 1);

	private final String name;
	private final int countWhenCut;

	StoneBlockForms(String name, int countWhenCut)
	{
		this.name = name;
		this.countWhenCut = countWhenCut;
	}

	public static Collection<StoneBlockForms> all()
	{
		return List.of(values());
	}

	public String getName()
	{
		return name;
	}

	public String getBrickName()
	{
		return this == BLOCK ? "s" : "_" + name;
	}

	public int getCountWhenCut()
	{
		return countWhenCut;
	}
}
