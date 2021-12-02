package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.List;

public class Problem01 extends Problem {
    final List<Integer> integerList = new ArrayList<>();

    @Override
    public void setupData() {
        this.input.forEach(s -> integerList.add(Integer.parseInt(s)));
    }

    @Override
    public Object rule1() {
        return count(this.integerList);
    }

    @Override
    public Object rule2() {
        List<Integer> sumList = new ArrayList<>();
        for (int i = 0; i < this.integerList.size() - 2; ++i) {
            sumList.add(this.integerList.get(i) + this.integerList.get(i+1) + this.integerList.get(i+2));
        }
        return count(sumList);
    }

    private int count(List<Integer> intList) {
        int previous = intList.get(0);
        int count = 0;
        for (int i = 1; i < intList.size(); ++i) {
            if (previous < intList.get(i)) {
                count++;
            }
            previous = intList.get(i);
        }
        return count;
    }
}
