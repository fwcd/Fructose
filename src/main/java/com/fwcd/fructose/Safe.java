package com.fwcd.fructose;

import java.util.Optional;

import com.fwcd.fructose.function.ThrowingSupplier;

/**
 * Provides static methods for performing "safe" operations
 * (like wrapping nullables into optionals or optionally casting).
 * "Safety" mostly refers to null-safety or exception-safety.
 */
public final class Safe {
	private Safe() {}

	public static <T> Optional<T> arrayGet(T[] array, int index) {
		if (index >= 0 && index < array.length) {
			return Optional.ofNullable(array[index]);
		} else {
			return Optional.empty();
		}
	}

	public static <T> Result<T, Throwable> attempt(ThrowingSupplier<T, Throwable> supplier) {
		try {
			return Result.of(supplier.get());
		} catch (Throwable e) {
			return Result.ofFailure(e);
		}
	}

	public static <T> Optional<T> cast(Object value, Class<T> resultingType) {
		try {
			return Optional.ofNullable(resultingType.cast(value));
		} catch (ClassCastException e) {
			return Optional.empty();
		}
	}
}
