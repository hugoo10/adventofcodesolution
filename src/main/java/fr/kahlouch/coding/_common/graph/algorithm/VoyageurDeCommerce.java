package fr.kahlouch.coding._common.graph.algorithm;

import fr.kahlouch.coding._common.graph.Graph;

import java.util.function.BinaryOperator;
import java.util.stream.Stream;

public record VoyageurDeCommerce(Graph graph) {

    public GraphPath exact(BinaryOperator<GraphPath> chooser) {
        return graph.nodes().stream()
                .map(GraphPath::new)
                .flatMap(this::buildPropositions)
                .reduce(chooser)
                .get();
    }

    private Stream<GraphPath> buildPropositions(GraphPath graphPath) {
        final var lastNode = graphPath.last();
        return lastNode.linkedNodes().stream()
                .map(graphPath::next)
                .flatMap(graphPathOpt -> {
                    if (graphPathOpt.isPresent()) {
                        return this.buildPropositions(graphPathOpt.get());
                    }
                    if (graphPath.size() < graph.size()) {
                        return Stream.of();
                    }
                    return Stream.of(graphPath);
                })
                .distinct();
    }
}
