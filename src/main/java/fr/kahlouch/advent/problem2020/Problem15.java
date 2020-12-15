package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.ProblemResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem15 {
    public static void main(String[] args) {
        new ProblemResolver("problem2020/problem15.txt", Problem15::rule1, Problem15::rule2).resolve();
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

    private static String resolve(List<String> input, int nieme) {
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


        return last + "";
    }

    private static String rule1(List<String> input) {
        return resolve(input, 2020);
    }

    private static String rule2(List<String> input) {
        return resolve(input, 30_000_000);
    }
}
