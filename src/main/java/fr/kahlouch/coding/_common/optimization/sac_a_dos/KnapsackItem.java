package fr.kahlouch.coding._common.optimization.sac_a_dos;

public interface KnapsackItem extends Comparable<KnapsackItem> {
    double cost();

    double value();

    boolean equals(Object obj);

    int hashCode();
}
