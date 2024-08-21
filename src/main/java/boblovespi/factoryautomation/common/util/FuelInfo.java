package boblovespi.factoryautomation.common.util;

import boblovespi.factoryautomation.FactoryAutomation;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public record FuelInfo(int time, float temp, float energy)
{
	public static final Codec<FuelInfo> CODEC = RecordCodecBuilder.create(
			i -> i.group(Codec.INT.fieldOf("time").forGetter(FuelInfo::time), Codec.FLOAT.fieldOf("temp").forGetter(FuelInfo::temp),
					Codec.FLOAT.fieldOf("energy").forGetter(FuelInfo::energy)).apply(i, FuelInfo::new));
	public static final DataMapType<Item, FuelInfo> FUEL_DATA = DataMapType.builder(FactoryAutomation.name("fuel"), Registries.ITEM, CODEC).synced(CODEC, true).build();
}
