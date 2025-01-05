package fr.kahlouch.coding._common.optimization.sac_a_dos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

//TODO à réécrire
public class KnapsackResolverOld {
    private final Map<KnapsackItem, Integer> content;

    public KnapsackResolverOld(List<KnapsackItem> knapsackItems) {
        this.content = new TreeMap<>();
        for (KnapsackItem knapsackItem : knapsackItems) {
            this.content.put(knapsackItem, 0);
        }
    }

    public void resolveUnlimitedItems(double maxCost) {
        resolve(maxCost, true);
    }

    public void resolveMax1PerItem(double maxCost) {
        resolve(maxCost, false);
    }

    private void resolve(double maxCost, boolean unlimited) {
        final var items = new ArrayList<>(this.content.keySet());
        var totalCost = 0D;

        for (var i = 0; i < items.size(); ) {
            final var item = items.get(i);
            final var itemCost = item.weight();

            if (itemCost + totalCost > maxCost) {
                i++;
                continue;
            }
            this.content.compute(item, (toIncrement, count) -> count + 1);
            totalCost += itemCost;

            if (unlimited) {
                continue;
            }

            i++;
        }
    }

}
