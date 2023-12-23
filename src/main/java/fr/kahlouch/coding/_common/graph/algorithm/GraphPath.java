package fr.kahlouch.coding._common.graph.algorithm;

import fr.kahlouch.coding._common.graph.LinkedNode;
import fr.kahlouch.coding._common.graph.Node;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class GraphPath {
    public static final BinaryOperator<GraphPath> SHORTEST = (a, b) -> Double.compare(a.weight(), b.weight()) <= 0 ? a : b;
    public static final BinaryOperator<GraphPath> LONGEST = (a, b) -> Double.compare(a.weight(), b.weight()) >= 0 ? a : b;
    private List<Node> path;
    private List<Double> weights;

    private double weight;
    private double inverseWeight;

    public GraphPath(Node startingNode) {
        this.path = new ArrayList<>();
        this.path.add(startingNode);
        this.weights = new ArrayList<>();
        this.weight = 0;
        this.inverseWeight = 0;
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
        subPath.weight = this.weight + linkedNode.weight();
        subPath.inverseWeight = this.inverseWeight + Node.weightBetweenNodes(linkedNode.node(), last());
        return Optional.of(subPath);
    }

    public int size() {
        return this.path.size();
    }

    public Node last() {
        return this.path.getLast();
    }

    public Node first() {
        return this.path.getFirst();
    }

    public double weight() {
        return this.weight;
    }

    public double inverseWeight() {
        return this.inverseWeight;
    }


    public double cyclicWeight() {
        return weight() + weightBetweenLastAndFirst() + inverseWeight() + weightBetweenFirstAndLast();
    }

    @Override
    public String toString() {
        return this.path.stream().map(Node::toString).collect(Collectors.joining(" -> "));
    }

    public double weightBetweenLastAndFirst() {
        return Node.weightBetweenNodes(last(), first());
    }

    public double weightBetweenFirstAndLast() {
        return Node.weightBetweenNodes(first(), last());
    }
}
