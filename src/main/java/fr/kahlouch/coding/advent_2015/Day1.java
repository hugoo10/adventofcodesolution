package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;

class Day1 extends AdventProblem {
    private Day1() {
        super(11);
    }

    public static void main(String[] args) {
        new Day1();
    }

    protected Object resolve1(Path inputPath) {
        final var floor = new AtomicInteger(0);
        Input.of(inputPath).line()
                .chars(DirectionParser.INSTANCE)
                .forEach(direction -> direction.accept(floor));
        return floor;
    }

    protected Object resolve2(Path inputPath) {
        final var floor = new AtomicInteger(0);
        return Input.of(inputPath).line()
                .chars(DirectionParser.INSTANCE)
                .filter(direction -> {
                    direction.accept(floor);
                    return floor.get() < 0;
                }).findFirst()
                .map(direction -> direction.idx + 1)
                .orElse(-1L);
    }

    private enum DirectionParser implements Parser<Direction> {
        INSTANCE;

        @Override
        public Direction parse(String input, long index) {
            if (input.equals("(")) {
                return new Direction(index, true);
            }
            return new Direction(index, false);
        }
    }

    private record Direction(long idx, boolean up) {
        void accept(AtomicInteger floor) {
            if (this.up) {
                floor.incrementAndGet();
            } else {
                floor.decrementAndGet();
            }
        }
    }
}
