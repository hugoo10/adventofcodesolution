package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.regex.Pattern;


public class Problem08Part1 extends Problem {
    public static void main(String[] args) {
        Problem.solvePart1(Problem08Part1.class,8);
    }

    private final Map<String, Step> stepMap = new TreeMap<>();
    private Instructions instructions;

    @Override
    public void setupData() {
        this.instructions = Instructions.parse(lines.getFirst());
        for (var idx = 2; idx < lines.size(); ++idx) {
            final var step = Step.parse(lines.get(idx));
            stepMap.put(step.id, step);
        }
    }

    @Override
    public Object rule1() {
        var count = 0;
        var currentStep = stepMap.get(Step.START_ID);
        for (var direction : instructions) {
            count++;
            final var nextStepId = currentStep.nextStepId(direction);
            currentStep = stepMap.get(nextStepId);
            if (currentStep.isEnd()) {
                return count;
            }
        }
        return "NOT_FOUND";
    }


    private static class Instructions implements Iterable<Direction>, Iterator<Direction> {

        public static Instructions parse(String input) {
            Objects.requireNonNull(input);
            final var list = input.chars().mapToObj(i -> (char) i).map(Direction::fromChar).toList();
            return new Instructions(list);
        }


        private final List<Direction> directions;
        private int idx = 0;

        private Instructions(List<Direction> directions) {
            this.directions = Collections.unmodifiableList(directions);
        }


        @Override
        public Iterator<Direction> iterator() {
            return this;
        }

        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Direction next() {
            final var direction = directions.get(idx);
            this.idx = (idx + 1) % this.directions.size();
            return direction;
        }


    }

    private enum Direction {
        LEFT, RIGHT;

        public static Direction fromChar(char input) {
            Objects.requireNonNull(input);
            if (input == 'L') {
                return LEFT;
            }
            if (input == 'R') {
                return RIGHT;
            }
            throw new IllegalArgumentException("Input non valable: " + input);
        }
    }

    private record Step(String id, String left, String right) implements Comparable<Step> {

        private static Pattern STEP_PATTERN = Pattern.compile("^([A-Z]{3})\\s=\\s\\(([A-Z]{3}),\\s([A-Z]{3})\\)$");

        public static Step parse(String input) {
            Objects.requireNonNull(input);
            final var matcher = STEP_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var id = matcher.group(1);
                final var left = matcher.group(2);
                final var right = matcher.group(3);
                return new Step(id, left, right);
            }
            throw new IllegalArgumentException("L'input ne matche pas la regex");
        }

        public static final String START_ID = "AAA";
        private static final String END_ID = "ZZZ";

        private String nextStepId(Direction direction) {
            return switch (direction) {
                case LEFT -> left;
                case RIGHT -> right;
            };
        }

        public boolean isStart() {
            return START_ID.equals(this.id);
        }

        public boolean isEnd() {
            return END_ID.equals(this.id);
        }


        @Override
        public int compareTo(Step o) {
            return this.id.compareTo(o.id);
        }


    }
}
