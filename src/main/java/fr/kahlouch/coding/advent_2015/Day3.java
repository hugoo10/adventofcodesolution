package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.Problem;
import fr.kahlouch.coding._common.Responses;
import fr.kahlouch.coding._common.geometry.Direction;
import fr.kahlouch.coding._common.geometry.Point2D;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;

import java.nio.file.Path;
import java.util.HashSet;

class Day3 extends Problem {
    public Day3() {
        super(4);
    }

    public static void main(String[] args) {
        new Day3();
    }

    @Override
    protected Object resolve(Path inputPath) {
        return Responses.of(
                resolve1(inputPath),
                resolve2(inputPath)
        );
    }

    private Object resolve1(Path inputPath) {
        var point = new Point2D(0, 0);
        final var visitedPoints = new HashSet<Point2D>();
        visitedPoints.add(point);
        for (var move : Input.of(inputPath).line().chars(MoveParser.INSTANCE).toList()) {
            point = move.apply(point);
            visitedPoints.add(point);
        }
        return visitedPoints.size();
    }

    private Object resolve2(Path inputPath) {
        var santa = new Point2D(0, 0);
        var robot = new Point2D(0, 0);
        final var visitedPoints = new HashSet<Point2D>();
        visitedPoints.add(santa);
        final var moves = Input.of(inputPath).line().chars(MoveParser.INSTANCE).toList();
        for (var i = 0; i < moves.size() - 1; i += 2) {
            santa = moves.get(i).apply(santa);
            if (i + 1 < moves.size()) {
                robot = moves.get(i + 1).apply(robot);
            }
            visitedPoints.add(santa);
            visitedPoints.add(robot);
        }
        return visitedPoints.size();
    }

    private enum MoveParser implements Parser<Move> {
        INSTANCE;

        @Override
        public Move parse(String input, long idx) {
            final var direction = switch (input) {
                case ">" -> Direction.EAST;
                case "<" -> Direction.WEST;
                case "^" -> Direction.NORTH;
                case "v" -> Direction.SOUTH;
                default -> throw new IllegalStateException("Unexpected value: " + input);
            };
            return new Move(direction);
        }
    }

    private record Move(Direction direction) {
        Point2D apply(Point2D point2D) {
            return point2D.applyDirection(direction);
        }
    }
}
