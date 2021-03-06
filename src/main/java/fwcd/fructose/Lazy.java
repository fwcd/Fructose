package fwcd.fructose;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Stores a lazily loaded reference to an object.
 */
public class Lazy<T> {
	private Supplier<T> getter;
	private T value;

	private Lazy(T value) {
		getter = null;
		this.value = Objects.requireNonNull(value);
	}

	public Lazy(Supplier<T> getter) {
		this.getter = Objects.requireNonNull(getter);
	}

	public static <T> Lazy<T> of(Supplier<T> getter) {
		return new Lazy<>(getter);
	}

	public static <T> Lazy<T> ofConstant(T value) {
		return new Lazy<>(value);
	}

	public boolean hasBeenInitialized() {
		return value != null;
	}
	
	public <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
		if (value == null) {
			return of(() -> mapper.apply(get()));
		} else {
			return ofConstant(mapper.apply(value));
		}
	}
	
	public <R> Lazy<R> flatMap(Function<? super T, ? extends Lazy<R>> mapper) {
		if (value == null) {
			return of(() -> mapper.apply(get()).get());
		} else {
			return mapper.apply(value);
		}
	}

	public T get() {
		if (value == null) {
			value = getter.get();
			getter = null;
		}

		return value;
	}
}
