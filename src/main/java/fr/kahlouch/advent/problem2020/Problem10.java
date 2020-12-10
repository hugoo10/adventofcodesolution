package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.ProblemResolver;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Problem10 {
    public static void main(String[] args) {
        new ProblemResolver("problem2020/problem10.txt", Problem10::rule1, Problem10::rule2).resolve();
    }

    private static List<Integer> getOutlets(List<String> input) {
        return input.stream().map(Integer::parseInt).sorted().collect(Collectors.toList());
    }

    private static String rule1(List<String> intput) {
        List<Integer> outlets = getOutlets(intput);
        Map<Integer, Integer> res = new HashMap<>();

        int tmp = 0;
        for (int i = 0; i < outlets.size(); ++i) {
            int diff = outlets.get(i) - tmp;
            if (!res.containsKey(diff)) {
                res.put(diff, 0);
            }
            res.put(diff, res.get(diff) + 1);
            tmp = outlets.get(i);
        }
        return (res.get(1) * (res.get(3) + 1)) + "";
    }

    private static String rule2(List<String> intput) {
        List<Integer> outlets = getOutlets(intput);
        outlets.add(0, 0);
        outlets.add(outlets.get(outlets.size() - 1) + 3);
        Map<Integer, Map<Integer, BigInteger>> countMap = new TreeMap<>();
        countMap.put(outlets.get(outlets.size() - 2), Map.of(outlets.get(outlets.size() - 1), BigInteger.ONE));
        for (int i = outlets.size() - 3; i >= 0; --i) {
            int current = outlets.get(i);
            countMap.put(current, new TreeMap<>());
            Stream.of(
                    test(current, i+1, countMap, outlets),
                    test(current, i+2, countMap, outlets),
                    test(current, i+3, countMap, outlets)
            ).filter(Objects::nonNull).forEach(e -> countMap.get(current).put(e.getKey(), e.getValue()));
        }
        return countMap.get(0).values().stream().reduce(BigInteger.ZERO, BigInteger::add) + "";
    }

    private static Map.Entry<Integer, BigInteger> test(int previous, int idxToTest, Map<Integer, Map<Integer, BigInteger>> countMap, List<Integer> outlets) {
        if (idxToTest >= outlets.size() || !countMap.containsKey(outlets.get(idxToTest))) return null;
        int next = outlets.get(idxToTest);
        int diff = next - previous;
        if (diff <= 3){
            return Map.entry(next, countMap.get(next).values().stream().reduce(BigInteger.ZERO, BigInteger::add));
        }
        return null;
    }
}
