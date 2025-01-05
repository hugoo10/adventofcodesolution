package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.graph.Connection;
import fr.kahlouch.coding._common.graph.algorithm.GraphPath;
import fr.kahlouch.coding._common.graph.collector.SymmetricGraphCollector;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;

class Day9 extends AdventProblem {
    public static void main(String[] args) {
        new Day9();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        return Input.of(inputPath).multiLines()
                .lines(ConnectionParser.INSTANCE)
                .collect(SymmetricGraphCollector.INSTANCE)
                .voyageurDeCommerce()
                .exact(GraphPath.SHORTEST)
                .weight();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        return Input.of(inputPath).multiLines()
                .lines(ConnectionParser.INSTANCE)
                .collect(SymmetricGraphCollector.INSTANCE)
                .voyageurDeCommerce()
                .exact(GraphPath.LONGEST)
                .weight();
    }

    private enum ConnectionParser implements Parser<Connection> {
        INSTANCE;
        private static final Regex CONNECTION_REGEX = new Regex("^(.+)\\sto\\s(.+)\\s=\\s(\\d+)$");

        @Override
        public Connection parse(String input, long idx) {
            final var matcher = CONNECTION_REGEX.match(input);
            final var from = matcher.group(1);
            final var to = matcher.group(2);
            final var weight = matcher.groupDouble(3);
            return new Connection(from, to, weight);
        }
    }
}
