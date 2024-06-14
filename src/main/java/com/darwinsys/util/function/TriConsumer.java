package com.darwinsys.util.function;

import java.util.Objects;

/**
 * Very much like a java.util.function.BiConsumer.
 * But, strangely, with three consumables instead of two.
 */
public interface TriConsumer<T, U, V> {
  abstract void accept(T t, U u, V v);
  default TriConsumer<T, U, V> andThen(TriConsumer<? super T, ? super U, ? super V> after) {
        Objects.requireNonNull(after);

        return (q, r, s) -> {
            accept(q, r, s);
            after.accept(q, r, s);
        };
    }
}

