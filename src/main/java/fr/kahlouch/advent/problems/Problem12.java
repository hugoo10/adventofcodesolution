package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem12 extends Problem {
    Graph graph;

    enum Type {
        START, END, NODE
    }

    record Node(int id, int x, int y, char height, Type type) {
    }

    record Graph(Map<Integer, Node> nodes, Map<Integer, List<Integer>> graph, Node start, Node end) {

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
        return null;
    }

    @Override
    public Object rule2() {
        return null;
    }
}
