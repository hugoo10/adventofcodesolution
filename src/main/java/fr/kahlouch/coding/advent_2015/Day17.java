package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.optimization.sac_a_dos.Knapsack;
import fr.kahlouch.coding._common.optimization.sac_a_dos.KnapsackItem;
import fr.kahlouch.coding._common.optimization.sac_a_dos.KnapsackResolver;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;

class Day17 extends AdventProblem {
    public Day17() {
        super(0);
    }

    public static void main(String[] args) {
        new Day17();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var containers = Input.of(inputPath)
                .multiLines()
                .lines((input, idx) -> new Container(Long.parseLong(input)))
                .toList();

        return new KnapsackResolver().generateAllFullKnapsacks(containers, 150).size();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var containers = Input.of(inputPath)
                .multiLines()
                .lines((input, idx) -> new Container(Long.parseLong(input)))
                .toList();

        final var completes = new KnapsackResolver().generateAllFullKnapsacks(containers, 150);

        final var minSize = completes.stream().map(Knapsack::size).min(Double::compare).orElseThrow();
        return completes.stream()
                .filter(knapsack -> knapsack.size() == minSize)
                .count();
    }

    private record Container(long capacity) implements KnapsackItem {
        @Override
        public double weight() {
            return capacity;
        }

        @Override
        public double value() {
            return 0;
        }
    }
}
