package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.graph.Connection;
import fr.kahlouch.coding._common.graph.Graph;
import fr.kahlouch.coding._common.graph.Node;
import fr.kahlouch.coding._common.graph.algorithm.GraphPath;
import fr.kahlouch.coding._common.graph.collector.AsymmetricGraphCollector;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

class Day13 extends AdventProblem {
    public static void main(String[] args) {
        new Day13();
    }

    @Override
    protected Object resolve1(Path inputPath) {

        final BinaryOperator<GraphPath> BEST_ARRANGEMENT = (a, b) -> Double.compare(a.cyclicWeight(), b.cyclicWeight()) >= 0 ? a : b;
        final var graphPath = Input.of(inputPath).multiLines()
                .lines(ConnectionParser.INSTANCE)
                .collect(AsymmetricGraphCollector.INSTANCE)
                .voyageurDeCommerce()
                .exact(BEST_ARRANGEMENT);
        return graphPath.cyclicWeight();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final BinaryOperator<GraphPath> BEST_ARRANGEMENT = (a, b) -> Double.compare(a.cyclicWeight(), b.cyclicWeight()) >= 0 ? a : b;
        final var graph = Input.of(inputPath).multiLines()
                .lines(ConnectionParser.INSTANCE)
                .collect(AsymmetricGraphCollector.INSTANCE);
        final var me = new Node("Me");
        graph.nodes().forEach(node -> {
            me.addLinkedNode(node, 0);
            node.addLinkedNode(me, 0);
        });
        final var graphWithMe = new Graph(Stream.concat(graph.nodes().stream(), Stream.of(me)).toList());
        final var graphPath = graphWithMe.voyageurDeCommerce()
                .exact(BEST_ARRANGEMENT);
        return graphPath.cyclicWeight();
    }

    private enum ConnectionParser implements Parser<Connection> {
        INSTANCE;

        private static final Regex regex = new Regex("^(\\S+)\\swould\\s(gain|lose)\\s(\\d+)\\shappiness\\sunits\\sby\\ssitting\\snext\\sto\\s(\\S+).$");

        @Override
        public Connection parse(String input, long idx) {
            final var analyzer = regex.analyze(input);
            final var from = analyzer.group(1);
            final var winLose = analyzer.group(2);
            final var value = analyzer.groupDouble(3);
            final var to = analyzer.group(4);
            return new Connection(from, to, value * ("lose".equals(winLose) ? -1 : 1));
        }
    }
}
