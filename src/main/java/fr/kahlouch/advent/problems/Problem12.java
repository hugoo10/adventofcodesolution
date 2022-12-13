package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class Problem12 extends Problem {
    Graph graph;

    enum Type {
        START, END, NODE
    }

    record Node(int id, int x, int y, char height, Type type) {
    }


    static class RouteNode implements Comparable<RouteNode> {
        Node current;
        Node previous;
        double routeScore;
        double estimatedScore;

        public RouteNode(Node current, Node previous, double routeScore, double estimatedScore) {
            this.current = current;
            this.previous = previous;
            this.routeScore = routeScore;
            this.estimatedScore = estimatedScore;
        }

        RouteNode(Node current) {
            this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        }
        @Override
        public int compareTo(RouteNode other) {
            return Double.compare(this.estimatedScore, other.estimatedScore);
        }

        public Node getCurrent() {
            return current;
        }

        public void setCurrent(Node current) {
            this.current = current;
        }

        public Node getPrevious() {
            return previous;
        }

        public void setPrevious(Node previous) {
            this.previous = previous;
        }

        public double getRouteScore() {
            return routeScore;
        }

        public void setRouteScore(double routeScore) {
            this.routeScore = routeScore;
        }

        public double getEstimatedScore() {
            return estimatedScore;
        }

        public void setEstimatedScore(double estimatedScore) {
            this.estimatedScore = estimatedScore;
        }
    }

    record RouteFinder(Graph graph, Scorer nextNodeScorer, Scorer targetScorer){
        public List<Node> findRoute(Node from, Node to) {
            Queue<RouteNode> openSet = new PriorityQueue<>();
            Map<Node, RouteNode> allNodes = new HashMap<>();

            RouteNode start = new RouteNode(from, null, 0d, targetScorer.computeCost(from, to));
            openSet.add(start);
            allNodes.put(from, start);

            while (!openSet.isEmpty()) {
                RouteNode next = openSet.poll();
                //build route
                if (next.getCurrent().equals(to)) {
                    List<Node> route = new ArrayList<>();
                    RouteNode current = next;
                    do {
                        route.add(0, current.getCurrent());
                        current = allNodes.get(current.getPrevious());
                    } while (current != null);
                    return route;
                }
                graph.getConnections(next.getCurrent()).forEach(connection -> {
                    RouteNode nextNode = allNodes.getOrDefault(connection, new RouteNode(connection));
                    allNodes.put(connection, nextNode);

                    double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);
                    if (newScore < nextNode.getRouteScore()) {
                        nextNode.setPrevious(next.getCurrent());
                        nextNode.setRouteScore(newScore);
                        nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                        openSet.add(nextNode);
                    }
                });
            }
            throw new IllegalStateException("No route found");
        }
    }

    record Graph(Map<Integer, Node> nodes, Map<Integer, List<Integer>> graph, Node start, Node end) {
        public Node getNode(int id) {
            return nodes.get(id);
        }

        public Set<Node> getConnections(Node node) {
            return graph.get(node.id).stream()
                    .map(this::getNode)
                    .collect(Collectors.toSet());
        }

    }

    record Scorer() {
        double computeCost(Node from, Node to){
            return Math.sqrt(Math.pow(from.x + to.x,2) + Math.pow(from.y + to.y, 2));
        }
    }

    @Override
    public void setupData() {
        Node start = null;
        Node end = null;
        final var nodes = new Node[this.lines.get(0).length()][this.lines.size()];
        final var nodeMap = new HashMap<Integer, Node>();
        final var graph = new HashMap<Integer, List<Integer>>();
        for (var lIdx = 0; lIdx < this.lines.size(); ++lIdx) {
            for (var cIdx = 0; cIdx < this.lines.get(lIdx).length(); ++cIdx) {
                final var id = this.lines.get(lIdx).length() * lIdx + cIdx;
                final var c = lines.get(lIdx).charAt(cIdx);
                final var height = c == 'S' ? 'a' : c == 'E' ? 'z' : c;
                final var node = new Node(id, cIdx, lIdx, height, c == 'S' ? Type.START : c == 'E' ? Type.END : Type.NODE);
                if (node.type == Type.START) {
                    start = node;
                } else if (node.type == Type.END) {
                    end = node;
                }
                nodeMap.put(id, node);
                nodes[node.x][node.y] = node;
            }
        }
        for (var x = 0; x < nodes.length; ++x) {
            for (var y = 0; y < nodes[x].length; ++y) {
                final var node = nodes[x][y];
                graph.put(node.id, new ArrayList<>());
                for (var xd = -1; xd <= 1; xd += 2) {
                    if (x + xd >= 0 && x + xd < nodes.length && Math.abs(nodes[x + xd][y].height - node.height) <= 1) {
                        graph.get(node.id).add(nodes[x + xd][y].id);
                    }
                }
                for (var yd = -1; yd <= 1; yd += 2) {
                    if (y + yd >= 0 && y + yd < nodes[x].length && Math.abs(nodes[x][y + yd].height - node.height) <= 1) {
                        graph.get(node.id).add(nodes[x][y + yd].id);
                    }
                }
            }
        }
        this.graph = new Graph(nodeMap, graph, start, end);

    }

    @Override
    public Object rule1() {
        var routeFinder = new RouteFinder(graph, new Scorer(), new Scorer());
        var res = routeFinder.findRoute(graph.start, graph.end);
        return res.size() -1;
    }

    @Override
    public Object rule2() {
        return null;
    }
}
