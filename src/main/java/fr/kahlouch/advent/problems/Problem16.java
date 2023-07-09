package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class Problem16 extends Problem {

    Graph graph;

    record Node(String id, int rate, int released) {

        Node(String id, int rate) {
            this(id, rate, -1);
        }
    }


    static class RouteNode implements Comparable<RouteNode> {
        Node current;
        Node previous;
        double routeScore;
        double estimatedScore;
        Map<String, Integer> visited;

        public RouteNode(Node current, Node previous, double routeScore, double estimatedScore, Map<String, Integer> visited) {
            this.current = current;
            this.previous = previous;
            this.routeScore = routeScore;
            this.estimatedScore = estimatedScore;
            this.visited = new TreeMap<>(visited);
        }

        RouteNode(Node current, Map<String, Integer> visited) {
            this(current, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, visited);
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

    record RouteFinder(Graph graph, Scorer nextNodeScorer, Scorer targetScorer) {
        public List<Node> findRoute(Node from, Node to) {
            Queue<RouteNode> openSet = new PriorityQueue<>();
            Map<Node, RouteNode> allNodes = new HashMap<>();

            RouteNode start = new RouteNode(from, null, 0d, targetScorer.computeCost(from, to), new HashMap<>());
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
                    var visited = new TreeMap<>(next.visited);
//                    visited.add(connection.id);
                    RouteNode nextNode = allNodes.getOrDefault(connection, new RouteNode(connection, visited));
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

    record Graph(Map<String, Node> nodes, Map<String, List<String>> graph) {
        public Node getNode(String id) {
            return nodes.get(id);
        }

        public Set<Node> getConnections(Node node) {
            return graph.get(node.id).stream()
                    .map(this::getNode)
                    .collect(Collectors.toSet());
        }

    }

    record Scorer() {
        double computeCost(Node from, Node to) {
            return 1;
        }
    }

    @Override
    public void setupData() {
        Map<String, Node> nodes = new HashMap<>();
        Map<String, List<String>> graphMap = new HashMap<>();
        lines.forEach(line -> {
            var nodeIdLeft = line.replace("Valve ", "").split(" has flow rate=");
            var nodeId = nodeIdLeft[0];
            var rateLeft = nodeIdLeft[1].split("; tunnels? leads? to valves? ");
            var rate = Integer.parseInt(rateLeft[0]);
            var linkedNode = rateLeft[1].split(", ");
            var node = new Node(nodeId, rate);
            nodes.put(node.id, node);
            graphMap.put(node.id, List.of(linkedNode));
        });
        graph = new Graph(nodes, graphMap);
    }

    record Destination(String id, int cost) implements Comparable<Destination> {
        @Override
        public int compareTo(Destination o) {
            return id.compareTo(o.id);
        }
    }

    /*
    --- Day 16: Proboscidea Volcanium ---
    The sensors have led you to the origin of the distress signal: yet another handheld device, just like the one the Elves gave you. However, you don't see any Elves around; instead, the device is surrounded by elephants! They must have gotten lost in these tunnels, and one of the elephants apparently figured out how to turn on the distress signal.

    The ground rumbles again, much stronger this time. What kind of cave is this, exactly? You scan the cave with your handheld device; it reports mostly igneous rock, some ash, pockets of pressurized gas, magma... this isn't just a cave, it's a volcano!

    You need to get the elephants out of here, quickly. Your device estimates that you have 30 minutes before the volcano erupts, so you don't have time to go back out the way you came in.

    You scan the cave for other options and discover a network of pipes and pressure-release valves. You aren't sure how such a system got into a volcano, but you don't have time to complain; your device produces a report (your puzzle input) of each valve's flow rate if it were opened (in pressure per minute) and the tunnels you could use to move between the valves.

    There's even a valve in the room you and the elephants are currently standing in labeled AA. You estimate it will take you one minute to open a single valve and one minute to follow any tunnel from one valve to another. What is the most pressure you could release?

    For example, suppose you had the following scan output:

    Valve AA has flow rate=0; tunnels lead to valves DD, II, BB
    Valve BB has flow rate=13; tunnels lead to valves CC, AA
    Valve CC has flow rate=2; tunnels lead to valves DD, BB
    Valve DD has flow rate=20; tunnels lead to valves CC, AA, EE
    Valve EE has flow rate=3; tunnels lead to valves FF, DD
    Valve FF has flow rate=0; tunnels lead to valves EE, GG
    Valve GG has flow rate=0; tunnels lead to valves FF, HH
    Valve HH has flow rate=22; tunnel leads to valve GG
    Valve II has flow rate=0; tunnels lead to valves AA, JJ
    Valve JJ has flow rate=21; tunnel leads to valve II
    All of the valves begin closed. You start at valve AA, but it must be damaged or jammed or something: its flow rate is 0, so there's no point in opening it. However, you could spend one minute moving to valve BB and another minute opening it; doing so would release pressure during the remaining 28 minutes at a flow rate of 13, a total eventual pressure release of 28 * 13 = 364. Then, you could spend your third minute moving to valve CC and your fourth minute opening it, providing an additional 26 minutes of eventual pressure release at a flow rate of 2, or 52 total pressure released by valve CC.

    Making your way through the tunnels like this, you could probably open many or all of the valves by the time 30 minutes have elapsed. However, you need to release as much pressure as possible, so you'll need to be methodical. Instead, consider this approach:

    == Minute 1 ==
    No valves are open.
    You move to valve DD.

    == Minute 2 ==
    No valves are open.
    You open valve DD.

    == Minute 3 ==
    Valve DD is open, releasing 20 pressure.
    You move to valve CC.

    == Minute 4 ==
    Valve DD is open, releasing 20 pressure.
    You move to valve BB.

    == Minute 5 ==
    Valve DD is open, releasing 20 pressure.
    You open valve BB.

    == Minute 6 ==
    Valves BB and DD are open, releasing 33 pressure.
    You move to valve AA.

    == Minute 7 ==
    Valves BB and DD are open, releasing 33 pressure.
    You move to valve II.

    == Minute 8 ==
    Valves BB and DD are open, releasing 33 pressure.
    You move to valve JJ.

    == Minute 9 ==
    Valves BB and DD are open, releasing 33 pressure.
    You open valve JJ.

    == Minute 10 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve II.

    == Minute 11 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve AA.

    == Minute 12 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve DD.

    == Minute 13 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve EE.

    == Minute 14 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve FF.

    == Minute 15 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve GG.

    == Minute 16 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You move to valve HH.

    == Minute 17 ==
    Valves BB, DD, and JJ are open, releasing 54 pressure.
    You open valve HH.

    == Minute 18 ==
    Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
    You move to valve GG.

    == Minute 19 ==
    Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
    You move to valve FF.

    == Minute 20 ==
    Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
    You move to valve EE.

    == Minute 21 ==
    Valves BB, DD, HH, and JJ are open, releasing 76 pressure.
    You open valve EE.

    == Minute 22 ==
    Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
    You move to valve DD.

    == Minute 23 ==
    Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
    You move to valve CC.

    == Minute 24 ==
    Valves BB, DD, EE, HH, and JJ are open, releasing 79 pressure.
    You open valve CC.

    == Minute 25 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

    == Minute 26 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

    == Minute 27 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

    == Minute 28 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

    == Minute 29 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.

    == Minute 30 ==
    Valves BB, CC, DD, EE, HH, and JJ are open, releasing 81 pressure.
    This approach lets you release the most pressure possible in 30 minutes with this valve layout, 1651.

    Work out the steps to release the most pressure in 30 minutes. What is the most pressure you can release?
     */
    @Override
    public Object rule1() {
        var routeFinder = new RouteFinder(graph, new Scorer(), new Scorer());
        var list = graph.nodes().values().stream().filter(node -> node.rate > 0 || node.id.equals("AA")).toList();
        var map = new TreeMap<String, TreeSet<Destination>>();
        var mapBis = new TreeMap<String, TreeSet<String>>();
        var newNodes = new TreeMap<String, Node>();
        for (var i = 0; i < list.size(); ++i) {
            map.put(list.get(i).id, new TreeSet<>());
            for (var j = 0; j < list.size(); ++j) {
                if (i == j) continue;
                var from = list.get(i);
                var to = list.get(j);
                if (to.id.equals("AA")) continue;
                var route = routeFinder.findRoute(from, to);

                var doublon = route.stream()
                        .filter(node -> !node.equals(from))
                        .filter(node -> !node.equals(to))
                        .filter(node -> !node.equals(graph.getNode("AA")))
                        .anyMatch(list::contains);
                if (doublon) continue;
                map.get(from.id).add(new Destination(to.id, route.size() - 1));
            }
        }
        Stack<String> toHandleStack = new Stack<>();
        toHandleStack.push("AA_1");
        do {
            var toHandle = toHandleStack.pop();
            if (mapBis.containsKey(toHandle)) continue;
            mapBis.put(toHandle, new TreeSet<>());
            var id = toHandle.split("_")[0];
            var number = Integer.parseInt(toHandle.split("_")[1]);
            var rate = graph.getNode(id).rate;
            var newNode = new Node(toHandle, rate, (30 - number) * rate);
            newNodes.put(toHandle, newNode);
            map.get(id).forEach(dest -> {
                var destId = dest.id;
                var newNumber = number + dest.cost + 1;
                if (newNumber <= 30) {
                    var newDest = destId + "_" + newNumber;
                    mapBis.get(toHandle).add(newDest);
                    toHandleStack.push(newDest);
                }
            });
            if (mapBis.get(toHandle).isEmpty()) {
                mapBis.get(toHandle).add("END_30");
            }
        } while (toHandleStack.size() > 0);

        return null;
    }

    @Override
    public Object rule2() {
        return null;
    }
}
