package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.geometry.Grid2D;
import fr.kahlouch.coding._common.geometry.Point2D;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

class Day6 extends AdventProblem {
    public static void main(String[] args) {
        new Day6();
    }

    private Day6() {
        super(5);
    }

    protected Object resolve1(Path inputPath) {
        final var grid = Grid2D.booleanGrid(1000, 1000, false);
        Input.of(inputPath).multiLines()
                .lines(CommandParser.INSTANCE)
                .forEach(command -> grid.applyDiagonally(command.from, command.to, command.action.doAction));
        return grid.stream()
                .filter(Predicate.isEqual(true))
                .count();
    }

    protected Object resolve2(Path inputPath, Object response1) {
        final var grid = Grid2D.intGrid(1000, 1000, 0);
        Input.of(inputPath).multiLines()
                .lines(CommandParser.INSTANCE)
                .forEach(command -> grid.applyDiagonally(command.from, command.to, command.action.doActionBis));
        return grid.stream()
                .mapToLong(i -> i)
                .sum();
    }

    private enum CommandParser implements Parser<Command> {
        INSTANCE;

        private static final Pattern LINE_PATTERN = Pattern.compile("^(.+)\\s(\\d+),(\\d+).*\\s(\\d+),(\\d+)$");

        @Override
        public Command parse(String input, long idx) {
            final var matcher = LINE_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var action = switch (matcher.group(1)) {
                    case "turn on" -> Action.TURN_ON;
                    case "turn off" -> Action.TURN_OFF;
                    case "toggle" -> Action.TOGGLE;
                    default -> throw new IllegalArgumentException("unknown action");
                };
                final var from = Point2D.of(matcher.group(2), matcher.group(3));
                final var to = Point2D.of(matcher.group(4), matcher.group(5));
                return new Command(action, from, to);
            }
            throw new IllegalArgumentException("not matching regex");
        }
    }

    private record Command(Action action, Point2D from, Point2D to) {

    }

    private enum Action {
        TURN_ON(b -> true, i -> i + 1),
        TOGGLE(b -> !b, i -> i + 2),
        TURN_OFF(b -> false, i -> Math.max(0, i - 1));

        Action(Function<Boolean, Boolean> doAction, Function<Integer, Integer> doActionBis) {
            this.doAction = doAction;
            this.doActionBis = doActionBis;
        }

        private final Function<Boolean, Boolean> doAction;
        private final Function<Integer, Integer> doActionBis;


    }
}
