package fr.kahlouch.coding._common.optimization.sac_a_dos;

import java.util.ArrayList;
import java.util.List;

public final class Knapsack {
    private final List<KnapsackItem> knapsackItems;

    public Knapsack(List<KnapsackItem> knapsackItems) {
        this.knapsackItems = List.copyOf(knapsackItems);
    }

    public Knapsack add(KnapsackItem knapsackItem) {
        final var items = new ArrayList<KnapsackItem>(this.knapsackItems);
        items.add(knapsackItem);
        return new Knapsack(items);
    }

    public int size() {
        return this.knapsackItems.size();
    }
}
