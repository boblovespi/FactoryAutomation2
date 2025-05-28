package boblovespi.factoryautomation.common.graph;

import boblovespi.factoryautomation.common.util.IMaybeSerializable;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

public class DirectionMap<T extends INBTSerializable<CompoundTag>> implements IMaybeSerializable<CompoundTag>, Map<Direction, T>
{
	private final EnumMap<Direction, T> map;

	public DirectionMap(EnumMap<Direction, T> map)
	{
		this.map = map;
	}

	public DirectionMap()
	{
		map = new EnumMap<>(Direction.class);
	}

	public static <T extends INBTSerializable<CompoundTag>> BiFunction<CompoundTag, HolderLookup.Provider, DirectionMap<T>> loader(BiFunction<CompoundTag, HolderLookup.Provider, T> innerLoader)
	{
		return (CompoundTag tag, HolderLookup.Provider provider) -> {
			var map = new DirectionMap<T>();
			for (var key : tag.getAllKeys())
				map.put(Objects.requireNonNull(Direction.byName(key)), innerLoader.apply(tag.getCompound(key), provider));
			return map;
		};
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		var tag = new CompoundTag();
		for (var entry : map.entrySet())
			tag.put(entry.getKey().getSerializedName(), entry.getValue().serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		for (var entry : map.entrySet())
			entry.getValue().deserializeNBT(provider, nbt.getCompound(entry.getKey().getSerializedName()));
	}

	@Override
	public int size()
	{
		return map.size();
	}

	@Override
	public boolean isEmpty()
	{
		return map.isEmpty();
	}

	@Override
	public boolean containsKey(Object key)
	{
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value)
	{
		return map.containsValue(value);
	}

	@Override
	public T get(Object key)
	{
		return map.get(key);
	}

	@Nullable
	@Override
	public T put(Direction key, T value)
	{
		return map.put(key, value);
	}

	@Override
	public T remove(Object key)
	{
		return map.remove(key);
	}

	@Override
	public void putAll(@NotNull Map<? extends Direction, ? extends T> m)
	{
		map.putAll(m);
	}

	@Override
	public void clear()
	{
		map.clear();
	}

	@NotNull
	@Override
	public Set<Direction> keySet()
	{
		return map.keySet();
	}

	@NotNull
	@Override
	public Collection<T> values()
	{
		return map.values();
	}

	@NotNull
	@Override
	public Set<Entry<Direction, T>> entrySet()
	{
		return map.entrySet();
	}

	@Override
	public boolean shouldSerialize()
	{
		return !map.isEmpty();
	}
}
