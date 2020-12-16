package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Problem14 extends Problem<BigInteger> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem14.txt", Problem14.class);
    }

    private static Map<String, Map<BigInteger, BigInteger>> getParsedProblem(List<String> input) {
        Map<String, Map<BigInteger, BigInteger>> parsedProblem = new LinkedHashMap<>();
        String mask = null;
        for (String str : input) {
            if (str.startsWith("mask")) {
                mask = str.split(" ")[2];
                parsedProblem.put(mask, new LinkedHashMap<>());
            } else {
                int startCrochet = str.indexOf("[");
                int endCrochet = str.indexOf("]");
                parsedProblem.get(mask).put(BigInteger.valueOf(Long.parseLong(str.substring(startCrochet + 1, endCrochet))), BigInteger.valueOf(Long.parseLong(str.split(" ")[2])));
            }
        }
        return parsedProblem;
    }

    @Override
    public BigInteger rule1() {
        Map<String, Map<BigInteger, BigInteger>> parsedProblem = getParsedProblem(input);
        Map<BigInteger, BigInteger> resultMap = new LinkedHashMap<>();

        parsedProblem.forEach((key, value) -> {
            final char[] mask = key.toCharArray();
            value.forEach((idx, val) -> resultMap.put(idx, applyMask(mask, val)));
        });
        return resultMap.values().stream().reduce(BigInteger.ZERO, BigInteger::add);
    }

    @Override
    public BigInteger rule2() {
        Map<String, Map<BigInteger, BigInteger>> parsedProblem = getParsedProblem(input);
        Map<BigInteger, BigInteger> resultMap = new LinkedHashMap<>();

        parsedProblem.forEach((key, value) -> {
            final char[] mask = key.toCharArray();
            value.forEach((idx, val) -> {
                List<BigInteger> addresses = applyMask2(mask, idx);
                addresses.forEach(address -> resultMap.put(address, val));
            });
        });
        return resultMap.values().stream().reduce(BigInteger.ZERO, BigInteger::add);
    }


    private static BigInteger applyMask(char[] mask, BigInteger value) {
        String binaryStr = value.toString(2);
        while (binaryStr.length() < 36) {
            binaryStr = "0" + binaryStr;
        }
        char[] binary = binaryStr.toCharArray();
        for (int i = 0; i < mask.length; ++i) {
            if (mask[i] != 'X') {
                binary[i] = mask[i];
            }
        }
        return new BigInteger(String.valueOf(binary), 2);
    }

    private static List<BigInteger> applyMask2(char[] mask, BigInteger value) {
        String binaryStr = value.toString(2);
        while (binaryStr.length() < 36) {
            binaryStr = "0" + binaryStr;
        }
        char[] binary = binaryStr.toCharArray();
        List<char[]> addresses = compute(0, mask, binary);
        return addresses.stream().map(ca -> new BigInteger(String.valueOf(ca), 2)).collect(Collectors.toList());
    }

    private static List<char[]> compute(int idx, char[] mask, char[] binary) {
        List<char[]> addresses = new ArrayList<>();
        if (idx < mask.length) {
            if (mask[idx] == '1') {
                char[] copy = Arrays.copyOf(binary, binary.length);
                copy[idx] = '1';
                addresses.addAll(compute(idx + 1, mask, copy));
            } else if (mask[idx] == 'X') {
                char[] copy1 = Arrays.copyOf(binary, binary.length);
                copy1[idx] = '1';
                addresses.addAll(compute(idx + 1, mask, copy1));
                char[] copy2 = Arrays.copyOf(binary, binary.length);
                copy2[idx] = '0';
                addresses.addAll(compute(idx + 1, mask, copy2));
            } else {
                addresses.addAll(compute(idx + 1, mask, binary));
            }
        } else {
            return List.of(binary);
        }
        return addresses;
    }
}
