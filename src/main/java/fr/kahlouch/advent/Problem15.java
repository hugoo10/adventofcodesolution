package fr.kahlouch.advent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.stream.Stream;

public class Problem15 extends Problem {
    private Graph graph;

    @Override

    public void setupData() {
        this.graph = Graph.fromInput(this.input);
    }

    @Override
    public Object rule1() {
        var depart = graph.getNodeAt(0, 0).get();
        var objectif = graph.getNodeAt(input.get(0).length() - 1, input.size() - 1).get();
        shorterPath(depart, objectif);
        return null;
    }

    @Override
    public Object rule2() {
        return null;
    }

    record Graph(Node[][] map) {
        static Graph fromInput(List<String> in) {
            var map = new Node[in.get(0).length()][in.size()];
            for (var y = 0; y < in.size(); ++y) {
                for (var x = 0; x < in.get(y).length(); ++x) {
                    var cout = Integer.parseInt(in.get(y).charAt(x) + "");
                    map[x][y] = new Node(x, y, cout);
                }
            }
            return new Graph(map);
        }

        Optional<Node> getNodeAt(int x, int y) {
            if (x >= 0 && x < map.length && y >= 0 && y < map[0].length) {
                return Optional.of(map[x][y]);
            }
            return Optional.empty();
        }
    }

    static class Node implements Comparable<Node> {
        private int x;
        private int y;
        private int cout;
        private int heuristique;

        public Node() {
        }

        public Node(int x, int y, int cout) {
            this.x = x;
            this.y = y;
            this.cout = cout;
            this.heuristique = 0;
        }

        List<Node> getVoisins(Graph graph) {
            return Stream.of(
                    graph.getNodeAt(x - 1, y),
                    graph.getNodeAt(x + 1, y),
                    graph.getNodeAt(x, y - 1),
                    graph.getNodeAt(x, y + 1)
            ).filter(Optional::isPresent).map(Optional::get).toList();
        }

        @Override
        public int compareTo(Node node) {
            if (this.heuristique < node.heuristique) {
                return 1;
            } else if (this.heuristique == node.heuristique) {
                return 0;
            }
            return -1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Node node = (Node) o;

            return new EqualsBuilder().append(x, node.x).append(y, node.y).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(x).append(y).toHashCode();
        }
    }

    private void shorterPath(Node depart, Node objectif) {
        final List<Node> closedList = new ArrayList<>();
        final Set<Node> openList = new TreeSet<>();
        openList.add(depart);

        while(!openList.isEmpty()) {
            Node u = new Node();//openList.derouler();
            if(u.equals(objectif)) {
                //reconstituer chemin
                break;
            }
            for(var v: u.getVoisins(graph)) {

                if(closedList.contains(v) || openList.)
            }
        }
    }
}
