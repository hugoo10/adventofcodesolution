package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.List;

public class Problem09 extends Problem<Integer> {
    @Override
    public Integer rule1() {
        List<Integer> testNumbers = new ArrayList<>();
        for (int i = 0; i < 25; ++i) {
            testNumbers.add(Integer.parseInt(input.get(i)));
        }

        int testInput = -1;
        boolean ok;
        for (int i = 25; i < input.size(); ++i) {
            ok = false;
            testInput = Integer.parseInt(input.get(i));
            bigLoop:
            for (int a = 0; a < testNumbers.size() - 1; ++a) {
                for (int b = a + 1; b < testNumbers.size(); ++b) {
                    if ((testNumbers.get(a) + testNumbers.get(b)) == testInput) {
                        ok = true;
                        break bigLoop;
                    }
                }
            }
            if (ok) {
                testNumbers.remove(0);
                testNumbers.add(testInput);
            } else {
                break;
            }
        }
        return testInput;
    }


    @Override
    public Integer rule2() {
        int referenceNumber = rule1();

        int sum;
        List<Integer> resp = new ArrayList<>();
        bigLoop2:
        for (int i = 0; i < input.size() - 1; ++i) {
            resp.clear();
            resp.add(Integer.parseInt(input.get(i)));
            for (int j = i + 1; j < input.size(); ++j) {
                resp.add(Integer.parseInt(input.get(j)));
                sum = resp.stream().reduce(0, Integer::sum);
                if (sum == referenceNumber) {
                    break bigLoop2;
                } else if (sum > referenceNumber) {
                    break;
                }
            }
        }
        return (resp.stream().min(Integer::compare).get() + resp.stream().max(Integer::compare).get());
    }
}
