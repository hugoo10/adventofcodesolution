package fr.kahlouch.advent.year.year_2015;

import fr.kahlouch.advent.common.Day;

import java.nio.file.Path;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

class Day2 extends Day {
    public Day2() {
        super(2);
    }

    public static void main(String[] args) {
        new Day2();
    }

    @Override
    protected Object resolve(Path inputPath) {
        return lineStream(inputPath)
                .map(Box::parse)
                .mapToLong(Box::computeWrappingArea)
                .sum();
    }

    private record Box(int length, int width, int height) {
        private static final Pattern BOX_DIMENSION_PATTERN = Pattern.compile("^(\\d+)x(\\d+)x(\\d+)$");

        static Box parse(String input) {
            final var matcher = BOX_DIMENSION_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var length = Integer.parseInt(matcher.group(1));
                final var width = Integer.parseInt(matcher.group(2));
                final var height = Integer.parseInt(matcher.group(3));
                return new Box(length, width, height);
            }
            throw new IllegalArgumentException("Coul not parse");
        }

        long computeWrappingArea() {
            final var smallest = IntStream.of(length, width, height).sorted().limit(2).reduce((a, b) -> a * b).getAsInt();
            return (2L * length * width) + (2L * width * height) + (2L * height * length) + smallest;
        }
    }
}
