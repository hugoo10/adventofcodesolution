package fr.kahlouch.coding._common.graph.collector;

import fr.kahlouch.coding._common.graph.Connection;
import fr.kahlouch.coding._common.graph.Graph;
import fr.kahlouch.coding._common.graph.Node;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.UNORDERED;

public enum SymmetricGraphCollector implements Collector<Connection, Map<String, Node>, Graph> {
    INSTANCE;
    @Override
    public Supplier<Map<String, Node>> supplier() {
        return TreeMap::new;
    }

    @Override
    public BiConsumer<Map<String, Node>, Connection> accumulator() {
        return (map, connection) -> {
            map.putIfAbsent(connection.from(), new Node(connection.from()));
            map.putIfAbsent(connection.to(), new Node(connection.to()));
            map.get(connection.from()).addLinkedNode(map.get(connection.to()), connection.weight());
            map.get(connection.to()).addLinkedNode(map.get(connection.from()), connection.weight());
        };
    }

    @Override
    public BinaryOperator<Map<String, Node>> combiner() {
        return (map1, map2) -> {
            final var combined = new TreeMap<>(map1);
            map2.forEach((id, node) -> {
                combined.computeIfPresent(id, (key, n) -> {
                    node.linkedNodes().forEach(n::addLinkedNode);
                    return n;
                });
                combined.putIfAbsent(id, node);
            });
            return combined;
        };
    }

    @Override
    public Function<Map<String, Node>, Graph> finisher() {
        return map -> new Graph(List.copyOf(map.values()));
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(UNORDERED);
    }
}
