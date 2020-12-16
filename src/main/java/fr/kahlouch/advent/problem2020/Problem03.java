package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Problem03 extends Problem<Long> {
    @Override
    public Long rule1() {
        List<List<Boolean>> map = new ArrayList<>();
        for (String line : input) {
            List<Boolean> treeLine = new ArrayList<>();
            do {
                for (char caseTree : line.toCharArray()) {
                    treeLine.add(caseTree == '#');
                }
            } while (treeLine.size() < input.size() * 3);
            map.add(treeLine);
        }
        List<Long> results = new ArrayList<>();
        int x = 0;
        int y = 0;
        int count = 0;
        while (y < input.size()) {
            if (map.get(y).get(x)) {
                count++;
            }
            x += 3;
            y += 1;
        }
        return (long) count;
    }

    @Override
    public Long rule2() {
        List<List<Boolean>> map = new ArrayList<>();
        for (String line : input) {
            List<Boolean> treeLine = new ArrayList<>();
            do {
                for (char caseTree : line.toCharArray()) {
                    treeLine.add(caseTree == '#');
                }
            } while (treeLine.size() < input.size() * 7);
            map.add(treeLine);
        }
        List<Long> results = new ArrayList<>();
        List<Map.Entry<Integer, Integer>> steps = List.of(
                Map.entry(1, 1),
                Map.entry(3, 1),
                Map.entry(5, 1),
                Map.entry(7, 1),
                Map.entry(1, 2)
        );

        for (Map.Entry<Integer, Integer> entry : steps) {
            int x = 0;
            int y = 0;
            int count = 0;
            while (y < input.size()) {
                if (map.get(y).get(x)) {
                    count++;
                }
                x += entry.getKey();
                y += entry.getValue();
            }
            results.add((long) count);
        }

        return results.stream().reduce(1L, (a, b) -> a * b);
    }
}

