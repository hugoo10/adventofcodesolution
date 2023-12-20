package fr.kahlouch.coding._common.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node {
    private final String id;
    private final List<LinkedNode> linkedNodes;

    public Node(String id) {
        this.id = id;
        this.linkedNodes = new ArrayList<>();
    }

    public void addLinkedNode(Node node, double weight) {
        addLinkedNode(new LinkedNode(node, weight));
    }

    public void addLinkedNode(LinkedNode linkedNode) {
        this.linkedNodes.add(linkedNode);
    }

    public List<LinkedNode> linkedNodes() {
        return Collections.unmodifiableList(linkedNodes);
    }
}
