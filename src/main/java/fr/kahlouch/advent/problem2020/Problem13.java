package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Problem13 extends Problem<BigInteger> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem13.txt", Problem13.class);
    }

    @Override
    public BigInteger rule1() {
        long nb = Long.parseLong(input.get(0));
        return Arrays.stream(input.get(1).split(","))
                .filter(s -> !s.equals("x"))
                .map(Long::parseLong)
                .map(b -> Map.entry(b, b - (nb % b)))
                .min(Map.Entry.comparingByValue())
                .map(e -> BigInteger.valueOf(e.getKey() * e.getValue()))
                .get();
    }

    @Override
    public BigInteger rule2() {
        List<String> buses = Arrays.asList(input.get(1).split(","));
        List<Map.Entry<Long, Long>> list = new ArrayList<>();
        for (int i = 0; i < buses.size(); ++i) {
            if (buses.get(i).equals("x")) continue;
            list.add(Map.entry(Long.parseLong(buses.get(i)), (long) i));
        }
        final BigInteger N = list.stream().map(Map.Entry::getKey).map(BigInteger::valueOf).reduce(BigInteger.ONE, BigInteger::multiply);
        List<BigInteger> solutions = new ArrayList<>();
        list.forEach(e -> {
            BigInteger restei = BigInteger.valueOf(e.getValue());
            BigInteger ni = BigInteger.valueOf(e.getKey());
            BigInteger bi = ni.subtract(restei).mod(ni);
            BigInteger Ni = N.divide(ni);
            BigInteger xi = Ni.modInverse(ni);
            solutions.add(BigInteger.ONE.multiply(bi).multiply(Ni).multiply(xi));
        });
        return solutions.stream().reduce(BigInteger.ONE, BigInteger::add).mod(N);
    }
}
