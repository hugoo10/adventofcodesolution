package fr.kahlouch.coding._common.optimization.sac_a_dos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class KnapsackResolver {

    public List<Knapsack> generateAllFullKnapsacks(List<? extends KnapsackItem> items, double weightCapacity) {
        final var sortedItems = items.stream()
                .sorted(Comparator.comparing(KnapsackItem::weight))
                .toList();

        return generateAllPossibilitiesRecursive(sortedItems, new Knapsack(List.of()), weightCapacity);
    }

    private List<Knapsack> generateAllPossibilitiesRecursive(List<? extends KnapsackItem> remainingItems, Knapsack intermediateKnapsack, double remainingCapacity) {
        if (remainingCapacity == 0D) {
            return List.of(intermediateKnapsack);
        }

        final var list = new LinkedList<>(remainingItems);
        final var nextItem = list.poll();


        if (nextItem == null || nextItem.weight() > remainingCapacity) {
            return List.of();
        }

        final var result = new ArrayList<Knapsack>();
        final var whenAdded = generateAllPossibilitiesRecursive(list, intermediateKnapsack.add(nextItem), remainingCapacity - nextItem.weight());
        final var whenNotAdded = generateAllPossibilitiesRecursive(list, intermediateKnapsack, remainingCapacity);

        result.addAll(whenAdded);
        result.addAll(whenNotAdded);
        return result;
    }
}
