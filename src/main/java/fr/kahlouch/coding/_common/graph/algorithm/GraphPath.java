package fr.kahlouch.coding._common.graph.algorithm;

import fr.kahlouch.coding._common.graph.LinkedNode;
import fr.kahlouch.coding._common.graph.Node;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GraphPath {
    private List<Node> path;
    private List<Double> weights;

    public GraphPath(Node startingNode) {
        this.path = new ArrayList<>();
        this.path.add(startingNode);
        this.weights = new ArrayList<>();
    }

    public Optional<GraphPath> next(LinkedNode linkedNode) {
        if (path.contains(linkedNode.node())) {
            return Optional.empty();
        }
        final var subPath = new GraphPath();
        subPath.path = new ArrayList<>(this.path);
        subPath.weights = new ArrayList<>(this.weights);
        subPath.path.add(linkedNode.node());
        subPath.weights.add(linkedNode.weight());
        return Optional.of(subPath);
    }

    public int size() {
        return this.path.size();
    }

    public Node last() {
        return this.path.getLast();
    }

    public double weight() {
        return this.weights.stream().mapToDouble(i -> i).sum();
    }

}
