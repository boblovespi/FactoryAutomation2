package boblovespi.factoryautomation.common.multiblock;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class MultiblockRegistry
{
	private static final Map<ResourceLocation, Multiblock> registry = HashMap.newHashMap(32);

	public static void register(Multiblock multiblock)
	{
		if (registry.containsKey(multiblock.getName()))
			throw new RuntimeException("A multiblock of the name " + multiblock.getName() + " has already been registered!");
		registry.put(multiblock.getName(), multiblock);
	}

	public static Multiblock get(ResourceLocation name)
	{
		return registry.get(name);
	}
}
