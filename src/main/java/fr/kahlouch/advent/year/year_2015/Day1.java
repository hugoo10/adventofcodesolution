package fr.kahlouch.advent.year.year_2015;

import fr.kahlouch.advent.common.Day;

import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class Day1 extends Day {
    public Day1() {
        super(11);
    }

    public static void main(String[] args) {
        new Day1();
    }

    @Override
    protected Object resolve(Path inputPath) {
        return List.of(
                resolve1(inputPath),
                resolve2(inputPath)
        );
    }

    private Object resolve1(Path inputPath) {
        final var floor = new AtomicInteger(0);
        charStream(inputPath)
                .map(Direction::parse)
                .forEach(direction -> direction.accept(floor));
        return floor;
    }

    private Object resolve2(Path inputPath) {
        final var floor = new AtomicInteger(0);
        final var position = new AtomicInteger(1);
        return charStream(inputPath)
                .map(Direction::parse)
                .filter(direction -> {
                    direction.accept(floor);
                    if(floor.get() < 0){
                        return true;
                    }
                    position.incrementAndGet();
                    return false;
                }).findFirst()
                .map(t -> position.get())
                .orElse(-1);
    }

    private enum Direction {
        UP, DOWN;

        static Direction parse(Character character) {
            if (character == '(') {
                return UP;
            }
            return DOWN;
        }

        void accept(AtomicInteger floor) {
            if (this == UP) {
                floor.incrementAndGet();
            } else {
                floor.decrementAndGet();
            }
        }
    }
}
