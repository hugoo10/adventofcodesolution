package fr.kahlouch.coding._common.optimization.sac_a_dos;

public interface KnapsackItem {
    double weight();

    double value();

    boolean equals(Object obj);

    int hashCode();
}
