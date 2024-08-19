package boblovespi.factoryautomation.common.block.types;

import java.util.Collection;
import java.util.List;

public enum OreQualities
{
	POOR("poor", 1),
	NORMAL("normal", 2),
	RICH("rich", 3);

	private final String name;
	private final int count;

	OreQualities(String name, int count)
	{
		this.name = name;
		this.count = count;
	}

	public static Collection<OreQualities> ore()
	{
		return List.of(POOR, NORMAL, RICH);
	}

	public String getName()
	{
		return name;
	}

	public int getCount()
	{
		return count;
	}
}
