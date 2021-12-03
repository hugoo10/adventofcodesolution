package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.List;

public class Problem03 extends Problem {
    @Override
    public void setupData() {

    }

    @Override
    public Object rule1() {
        Counter result = this.input.stream()
                .map(Counter::fromBinary)
                .reduce(null, Counter::cumul);
        return result.epsilon() * result.gamma();
    }

    @Override
    public Object rule2() {
        var more = filterOutMore(this.input, 0).get(0);
        var least = filterOutLeast(this.input, 0).get(0);

        return Integer.parseInt(more, 2) * Integer.parseInt(least, 2);
    }

    static List<String> filterOutMore(List<String> list, int idx) {
        if (list.size() == 1) return list;
        var countOnes = list.parallelStream().map(s -> s.charAt(idx)).filter(c -> c == '1').count();
        if (countOnes * 2 >= list.size()) {
            return filterOutMore(list.stream().filter(s -> s.charAt(idx) == '1').toList(), idx + 1);
        }
        return filterOutMore(list.stream().filter(s -> s.charAt(idx) == '0').toList(), idx + 1);
    }

    static List<String> filterOutLeast(List<String> list, int idx) {
        if (list.size() == 1) return list;
        var countOnes = list.parallelStream().map(s -> s.charAt(idx)).filter(c -> c == '1').count();
        if (countOnes * 2 < list.size()) {
            return filterOutLeast(list.stream().filter(s -> s.charAt(idx) == '1').toList(), idx + 1);
        }
        return filterOutLeast(list.stream().filter(s -> s.charAt(idx) == '0').toList(), idx + 1);
    }

    record Counter(List<Integer> ones, List<Integer> zeros) {
        public static Counter fromBinary(String binary) {
            List<Integer> one = new ArrayList<>();
            List<Integer> zero = new ArrayList<>();
            binary.chars().forEach(c -> {
                if (c == '1') {
                    one.add(1);
                    zero.add(0);
                } else {
                    one.add(0);
                    zero.add(1);
                }
            });
            return new Counter(one, zero);
        }

        static Counter cumul(Counter counter, Counter counter2) {
            if (counter == null) return counter2;
            if (counter2 == null) return counter;
            List<Integer> one = new ArrayList<>();
            List<Integer> zero = new ArrayList<>();
            for (int i = 0; i < counter.ones.size(); ++i) {
                one.add(counter.ones.get(i) + counter2.ones.get(i));
                zero.add(counter.zeros.get(i) + counter2.zeros.get(i));
            }
            return new Counter(one, zero);
        }

        public int gamma() {
            String res = "";
            for (int i = 0; i < ones.size(); i++) {
                if (ones.get(i) > zeros.get(i)) {
                    res += "1";
                } else {
                    res += "0";
                }
            }
            return Integer.parseInt(res, 2);
        }

        public int epsilon() {
            String res = "";
            for (int i = 0; i < ones.size(); i++) {
                if (ones.get(i) > zeros.get(i)) {
                    res += "0";
                } else {
                    res += "1";
                }
            }
            return Integer.parseInt(res, 2);
        }
    }
}

