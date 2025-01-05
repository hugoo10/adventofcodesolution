package fr.kahlouch.coding._common.optimization.brut_force;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class MemoryHungryCompositionGenerator<T> {

    public List<Composition<T>> generateAllCompositions(List<T> items, long totalSpace) {
        return generateAllPossibilitiesRecursive(items, new Composition<>(Map.of()), totalSpace);
    }

    private List<Composition<T>> generateAllPossibilitiesRecursive(List<T> remainingItems, Composition<T> intermediateComposition, long remainingSpace) {
        final var list = new LinkedList<>(remainingItems);

        final var nextItem = list.poll();
        if (list.isEmpty()) {
            final var finalComposition = intermediateComposition.put(nextItem, remainingSpace);
            return List.of(finalComposition);
        }

        final var result = new ArrayList<Composition<T>>();
        for (long space = 0; space <= remainingSpace; ++space) {
            final var nextIntermediateComposition = intermediateComposition.put(nextItem, space);
            result.addAll(generateAllPossibilitiesRecursive(list, nextIntermediateComposition, remainingSpace - space));
        }
        return result;
    }
}
