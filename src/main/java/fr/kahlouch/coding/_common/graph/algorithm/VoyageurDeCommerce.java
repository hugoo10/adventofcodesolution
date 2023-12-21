package fr.kahlouch.coding._common.graph.algorithm;

import fr.kahlouch.coding._common.graph.Graph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public record VoyageurDeCommerce(Graph graph) {
    public GraphPath exact() {
        final var start = graph.nodes().stream().map(GraphPath::new).toList();
        return buildAllPropositions(start).stream()
                .min(Comparator.comparing(GraphPath::weight))
                .get();
    }

    public GraphPath exactInverse() {
        final var start = graph.nodes().stream().map(GraphPath::new).toList();
        return buildAllPropositions(start).stream()
                .max(Comparator.comparing(GraphPath::weight))
                .get();
    }

    private List<GraphPath> buildAllPropositions(List<GraphPath> from) {
        if (from.stream().allMatch(path -> path.size() >= graph.nodes().size())) {
            return from;
        }
        final var subPaths = new ArrayList<GraphPath>();
        from.forEach(path ->
                path.last().linkedNodes().stream()
                        .map(path::next)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(subPaths::add)
        );
        return buildAllPropositions(subPaths);
    }

    public GraphPath approche() {
        return null;
    }
}
