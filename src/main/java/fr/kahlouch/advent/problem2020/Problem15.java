package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem15 extends Problem<Long> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem15.txt", Problem15.class);
    }

    private static Map.Entry<Map.Entry<Integer, Long>, Map<Long, List<Integer>>> init(List<String> input) {
        long last = -1;
        int count = 0;
        Map<Long, List<Integer>> map = new HashMap<>();
        int value = 1;
        for (String line : input) {
            String[] split = line.split(",");
            count += split.length;
            for (int i = 0; i < split.length; ++i) {
                long l = Long.parseLong(split[i]);
                map.putIfAbsent(l, new ArrayList<>());
                map.get(l).add(value++);
                last = l;
            }
        }
        return Map.entry(Map.entry(count, last), map);
    }

    private Long resolve(int nieme) {
        final Map.Entry<Map.Entry<Integer, Long>, Map<Long, List<Integer>>> entry = init(input);
        final Map<Long, List<Integer>> map = entry.getValue();
        long last = entry.getKey().getValue();
        int count = entry.getKey().getKey();

        for (int i = count + 1; i <= nieme; ++i) {
            long nextValue;
            List<Integer> lastList = map.get(last);
            if (lastList.size() == 1) {
                nextValue = 0;
            } else {
                int lastIdx = lastList.get(lastList.size() - 1);
                int beforeLastIdx = lastList.get(lastList.size() - 2);
                nextValue = lastIdx - beforeLastIdx;
            }

            map.putIfAbsent(nextValue, new ArrayList<>());
            map.get(nextValue).add(i);

            last = nextValue;
        }


        return last;
    }

    @Override
    public Long rule1() {
        return resolve(2020);
    }

    @Override
    public Long rule2() {
        return resolve(30_000_000);
    }
}
