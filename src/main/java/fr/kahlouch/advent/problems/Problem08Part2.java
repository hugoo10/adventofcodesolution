package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;
import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.*;
import java.util.regex.Pattern;


public class Problem08Part2 extends Problem {
    public static void main(String[] args) {
        Problem.solvePart2(Problem08Part2.class, 8);
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
    public Object rule2() {
        var currentSteps = stepMap.values().stream().filter(Step::isStart).map(step -> new IntermediateStep(0, step, this.instructions.clone())).toList();
        final var steps = currentSteps.stream()
                .map(in -> computeToEnd(in, stepMap)).toList();
        return steps.stream()
                .map(IntermediateStep::count)
                .reduce(1L, ArithmeticUtils::lcm);
    }

    private static IntermediateStep computeToEnd(IntermediateStep intermediateStep, Map<String, Step> stepMap) {
        var count = intermediateStep.count;
        var currentStep = intermediateStep.step;
        for (var direction : intermediateStep.instructions) {
            count++;
            final var nextStepId = currentStep.nextStepId(direction);
            currentStep = stepMap.get(nextStepId);
            if (currentStep.isEnd()) {
                return new IntermediateStep(count, currentStep, intermediateStep.instructions);
            }
        }
        throw new RuntimeException("Ne doit pas passer par la");
    }

    private record IntermediateStep(long count, Step step, Instructions instructions) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            IntermediateStep that = (IntermediateStep) o;

            return count == that.count;
        }

        @Override
        public int hashCode() {
            return (int) (count ^ (count >>> 32));
        }
    }


    private static class Instructions implements Iterable<Direction>, Iterator<Direction>, Cloneable {

        public static Instructions parse(String input) {
            Objects.requireNonNull(input);
            final var list = input.chars().mapToObj(i -> (char) i).map(Direction::fromChar).toList();
            return new Instructions(list);
        }

        @Override
        public Instructions clone() {
            return new Instructions(this.directions);
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

        private static Pattern STEP_PATTERN = Pattern.compile("^([A-Z0-9]{3})\\s=\\s\\(([A-Z0-9]{3}),\\s([A-Z0-9]{3})\\)$");

        public static Step parse(String input) {
            Objects.requireNonNull(input);
            final var matcher = STEP_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var id = matcher.group(1);
                final var left = matcher.group(2);
                final var right = matcher.group(3);
                return new Step(id, left, right);
            }
            throw new IllegalArgumentException("L'input ne matche pas la regex: " + input);
        }

        private String nextStepId(Direction direction) {
            return switch (direction) {
                case LEFT -> left;
                case RIGHT -> right;
            };
        }

        public boolean isStart() {
            return this.id.endsWith("A");
        }

        public boolean isEnd() {
            return this.id.endsWith("Z");
        }


        @Override
        public int compareTo(Step o) {
            return this.id.compareTo(o.id);
        }


    }
}
