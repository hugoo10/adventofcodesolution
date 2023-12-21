package fr.kahlouch.coding._common.graph;


import fr.kahlouch.coding._common.graph.algorithm.VoyageurDeCommerce;

import java.util.Collections;
import java.util.List;

public class Graph {
    private final List<Node> nodes;

    public Graph(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Node> nodes() {
        return Collections.unmodifiableList(nodes);
    }

    public VoyageurDeCommerce voyageurDeCommerce() {
        return new VoyageurDeCommerce(this);
    }
}
