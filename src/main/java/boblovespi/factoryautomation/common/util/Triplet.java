package boblovespi.factoryautomation.common.util;

import java.util.function.BiFunction;
import java.util.function.Function;

public record Triplet<T, U, V>(T fst, U snd, V thd)
{
	public static <O, T, U, V> Function<O, Triplet<T, U, V>> apply3(Function<O, T> f1, Function<O, U> f2, Function<O, V> f3)
	{
		return o -> new Triplet<>(f1.apply(o), f2.apply(o), f3.apply(o));
	}

	public <R> R match(TripletFunction<T, U, V, R> f)
	{
		return f.apply(fst, snd, thd);
	}

	@FunctionalInterface
	public interface TripletFunction<T, U, V, R>
	{
		R apply(T t, U u, V v);

		default BiFunction<U, V, R> partial(T t)
		{
			return (u, v) -> apply(t, u, v);
		}
	}
}
