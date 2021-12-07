package fr.kahlouch.advent;

import java.util.Arrays;
import java.util.List;

public class Problem07 extends Problem {
    List<Integer> orderedPositions;

    @Override
    public void setupData() {
        orderedPositions = Arrays.stream(this.input.get(0).split(",")).map(Integer::parseInt).sorted().toList();
    }

    @Override
    public Object rule1() {
        var size = this.orderedPositions.size();
        var mediane = this.orderedPositions.get(size / 2);

        return this.orderedPositions.parallelStream().map(v -> Math.abs(v - mediane)).reduce(0, Integer::sum);
    }

    @Override
    public Object rule2() {
        var sum = this.orderedPositions.parallelStream().reduce(0, Integer::sum);
        var moyenneUR = sum / (double) this.orderedPositions.size();
        var moyenne = Math.round(moyenneUR) - 1;
        return this.orderedPositions.parallelStream().map(v -> {
            var diff = Math.abs(v - moyenne);
            var s = 0L;
            for (var i = 0; i < diff; ++i) {
                s += (i + 1);
            }
            return s;
        }).reduce(0L, Long::sum);
    }
}
