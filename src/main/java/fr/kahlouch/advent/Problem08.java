package fr.kahlouch.advent;

import java.util.*;

public class Problem08 extends Problem {
    List<List<String>> left = new ArrayList<>();
    List<List<String>> right = new ArrayList<>();

    @Override
    public void setupData() {
        // 7 -> use 3 digits
        // 4 -> use 4 digits
        // 1 -> use 2 digits
        // 8 -> use 7 digits
        for (var i = 0; i < this.input.size(); ++i) {
            var idx = i;
            var str = input.get(i);
            left.add(new ArrayList<>());
            right.add(new ArrayList<>());
            var split = str.split("\\|");
            Arrays.stream(split[0].strip().split(" "))
                    .forEach(l -> left.get(idx).add(ord(l.strip())));
            Arrays.stream(split[1].strip().split(" "))
                    .forEach(l -> right.get(idx).add(ord(l.strip())));
        }

    }

    @Override
    public Object rule1() {
        var count = 0;
        for (var list : this.right) {
            for (var str : list) {
                if (convert(str).isPresent()) {
                    count++;
                }
            }
        }
        return count;
    }

    Optional<Integer> convert(String in) {
        var size = in.length();
        return Optional.ofNullable(
                switch (size) {
                    case 2 -> 1;
                    case 3 -> 7;
                    case 4 -> 4;
                    case 7 -> 8;
                    default -> null;
                }
        );
    }

    @Override
    public Object rule2() {
        var sum = 0L;
        for (var i = 0; i < left.size(); ++i) {
            Map<Integer, String> int2Str = new HashMap<>();
            Map<String, Integer> strToInt = new HashMap<>();
            var digits = new ArrayList<>(left.get(i));
            digits.forEach(str -> {
                var convert = convert(str);
                if (convert.isPresent()) {
                    int2Str.put(convert.get(), str);
                    strToInt.put(str, convert.get());
                }
            });

            digits.remove(int2Str.get(1));
            digits.remove(int2Str.get(4));
            digits.remove(int2Str.get(7));
            digits.remove(int2Str.get(8));

            int2Str.put(3, find3(int2Str, digits));
            strToInt.put(int2Str.get(3), 3);
            digits.remove(int2Str.get(3));

            int2Str.put(9, find9(int2Str, digits));
            strToInt.put(int2Str.get(9), 9);
            digits.remove(int2Str.get(9));

            int2Str.put(0, find0(int2Str, digits));
            strToInt.put(int2Str.get(0), 0);
            digits.remove(int2Str.get(0));

            int2Str.put(6, find6(int2Str, digits));
            strToInt.put(int2Str.get(6), 6);
            digits.remove(int2Str.get(6));

            int2Str.put(5, find5(int2Str, digits));
            strToInt.put(int2Str.get(5), 5);
            digits.remove(int2Str.get(5));

            int2Str.put(2, find2(int2Str, digits));
            strToInt.put(int2Str.get(2), 2);
            digits.remove(int2Str.get(2));

            String res = "";
            for (var str : right.get(i)) {
                res += strToInt.get(str);
            }
            sum += Long.parseLong(res);

        }

        return sum;
    }

    String ord(String str) {
        return Arrays.stream(str.split("")).sorted().reduce("", (a, b) -> a + b);
    }

    private String find5(Map<Integer, String> int2Str, ArrayList<String> digits) {
        String nb6 = int2Str.get(6);
        return digits.stream().filter(s -> s.length() == 5).filter(s -> minus(s, nb6) == 0).findFirst().get();

    }

    private String find2(Map<Integer, String> int2Str, ArrayList<String> digits) {
        return digits.stream().findFirst().get();
    }

    String find3(Map<Integer, String> int2Str, List<String> digits) {
        String nb1 = int2Str.get(1);
        return digits.stream().filter(s -> s.length() == 5).filter(s -> minus(s, nb1) == 3).findFirst().get();
    }

    String find9(Map<Integer, String> int2Str, List<String> digits) {
        String nb3 = int2Str.get(3);
        return digits.stream().filter(s -> s.length() == 6).filter(s -> minus(s, nb3) == 1).findFirst().get();
    }

    String find0(Map<Integer, String> int2Str, List<String> digits) {
        String nb7 = int2Str.get(7);
        return digits.stream().filter(s -> s.length() == 6).filter(s -> minus(s, nb7) == 3).findFirst().get();
    }

    String find6(Map<Integer, String> int2Str, List<String> digits) {
        return digits.stream().filter(s -> s.length() == 6).findFirst().get();
    }

    int minus(String that, String minus) {
        var res = that;
        for (var c : minus.toCharArray()) {
            res = res.replaceAll(c + "", "");
        }
        return res.length();
    }
}
