package fr.kahlouch.coding._common.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public record Grid2DCollector(String separator) implements Collector<String, List<List<String>>, Grid2D<String>> {


    @Override
    public Supplier<List<List<String>>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<List<String>>, String> accumulator() {
        return (grid, string) -> {
            grid.add(Arrays.asList(string.split(this.separator)));
        };
    }

    @Override
    public BinaryOperator<List<List<String>>> combiner() {
        return (g1, g2) -> {
            throw new RuntimeException("Not happening");
        };
    }

    @Override
    public Function<List<List<String>>, Grid2D<String>> finisher() {
        return Grid2D::new;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of();
    }
}
