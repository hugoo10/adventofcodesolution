package fr.kahlouch.coding._common.optimization.brut_force;

import java.util.HashMap;
import java.util.Map;

/**
 * Immutable
 *
 * @param <T>
 */
public final class Composition<T> {
    private final Map<T, Long> value;

    public Composition(Map<T, Long> value) {
        this.value = Map.copyOf(value);
    }

    public Composition<T> put(T key, long value) {
        final var map = new HashMap<>(this.value);
        map.put(key, value);
        return new Composition<>(map);
    }

    public Map<T, Long> value() {
        return value;
    }
}
