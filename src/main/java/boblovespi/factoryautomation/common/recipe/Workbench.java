package boblovespi.factoryautomation.common.recipe;

import boblovespi.factoryautomation.FactoryAutomation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public class Workbench
{
	public static final Codec<Part> PART_CODEC = RecordCodecBuilder.create(i -> i.group(ResourceLocation.CODEC.fieldOf("name").forGetter(Part::name),
			Codec.INT.fieldOf("tier").forGetter(Part::tier)).apply(i, Part::new));
	public static final Codec<Tool> TOOL_CODEC = RecordCodecBuilder.create(i -> i.group(ResourceLocation.CODEC.fieldOf("name").forGetter(Tool::name),
			Codec.INT.fieldOf("tier").forGetter(Tool::tier)).apply(i, Tool::new));

	public static final DataMapType<Item, Part> PART_DATA = DataMapType.builder(FactoryAutomation.name("workbench_part"), Registries.ITEM, PART_CODEC).synced(PART_CODEC, true).build();
	public static final DataMapType<Item, Tool> TOOL_DATA = DataMapType.builder(FactoryAutomation.name("workbench_tool"), Registries.ITEM, TOOL_CODEC).synced(TOOL_CODEC, true).build();

	public record Part(ResourceLocation name, int tier)
	{

	}

	public record Tool(ResourceLocation name, int tier)
	{

	}
}
