package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.util.List;
import java.util.stream.Collectors;

public class Problem01 extends Problem<Integer> {

    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem01.txt", Problem01.class);
    }

    @Override
    public Integer rule1() {
        List<Integer> list = input.stream().map(Integer::parseInt).collect(Collectors.toList());
        for (int i = 0; i < list.size() - 1; ++i) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i) + list.get(j) == 2020) {
                    return list.get(i) * list.get(j);
                }
            }
        }
        return null;
    }

    @Override
    public Integer rule2() {
        List<Integer> list = input.stream().map(Integer::parseInt).collect(Collectors.toList());
        for (int i = 0; i < list.size() - 2; ++i) {
            for (int j = i + 1; j < list.size() - 1; j++) {
                if (list.get(i) + list.get(j) > 2020) continue;
                for (int k = j + 1; k < list.size(); k++) {
                    if (list.get(i) + list.get(j) + list.get(k) == 2020) {
                        return list.get(i) * list.get(j) * list.get(k);
                    }
                }
            }
        }
        return null;
    }
}
