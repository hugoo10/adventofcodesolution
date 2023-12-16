package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.Problem;
import fr.kahlouch.coding._common.Responses;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class Day2 extends Problem {
    public Day2() {
        super(2);
    }

    public static void main(String[] args) {
        new Day2();
    }

    @Override
    protected Object resolve(Path inputPath) {
        return Responses.of(
                resolve1(inputPath),
                resolve2(inputPath)
        );
    }

    private Object resolve1(Path inputPath) {
        return Input.of(inputPath)
                .multiLines()
                .lines(BoxParser.INSTANCE)
                .mapToLong(Box::computeWrappingArea)
                .sum();
    }
    private Object resolve2(Path inputPath) {
        return Input.of(inputPath)
                .multiLines()
                .lines(BoxParser.INSTANCE)
                .mapToLong(Box::computeRibbon)
                .sum();
    }

    enum BoxParser implements Parser<Box> {
        INSTANCE;

        private static final Pattern BOX_DIMENSION_PATTERN = Pattern.compile("^(\\d+)x(\\d+)x(\\d+)$");

        @Override
        public Box parse(String input, long andIncrement) {
            final var matcher = BOX_DIMENSION_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var length = Integer.parseInt(matcher.group(1));
                final var width = Integer.parseInt(matcher.group(2));
                final var height = Integer.parseInt(matcher.group(3));
                return new Box(length, width, height);
            }
            throw new IllegalArgumentException("Could not parse");
        }
    }

    record Box(int length, int width, int height) {
        long computeWrappingArea() {
            final var smallest = IntStream.of(length, width, height).sorted().limit(2).reduce((a, b) -> a * b).getAsInt();
            return (2L * length * width) + (2L * width * height) + (2L * height * length) + smallest;
        }

        long computeRibbon() {
            final var smallest = IntStream.of(length, width, height).sorted().limit(2).reduce((a, b) -> 2 * a + 2 * b).getAsInt();
            return (long) length * width * height + smallest;
        }
    }
}
