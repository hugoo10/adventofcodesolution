package fr.kahlouch.advent;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Problem14 extends Problem {
    private String formula;
    private Map<String, String> polymerTemplate = new TreeMap<>();


    @Override
    public void setupData() {
        this.formula = this.input.get(0);
        for (var i = 2; i < input.size(); ++i) {
            var split = this.input.get(i).split("->");
            this.polymerTemplate.put(split[0].strip(), split[1].strip());
        }
    }

    @Override
    public Object rule1() {
        return resolve(this.formula, 10);
    }

    @Override
    public Object rule2() {
        return resolve(this.formula, 40);
    }


    private long resolve(String formula, int nbLoop) {
        var res = new Polymer(formula);
        for (var i = 0; i < nbLoop; ++i) {
            res = res.step(this.polymerTemplate);
        }
        Map<String, Long> perLetter = new HashMap<>();
        res.count.forEach((key, value) -> {
            var ll = key.charAt(0) + "";
            var lr = key.charAt(1) + "";
            perLetter.putIfAbsent(ll, 0L);
            perLetter.putIfAbsent(lr, 0L);
            perLetter.computeIfPresent(ll, (str, count) -> count + value);
            perLetter.computeIfPresent(lr, (str, count) -> count + value);
        });
        perLetter.replaceAll((str, count) -> count / 2);
        var firstLetter = formula.charAt(0) + "";
        var lastLetter = formula.charAt(formula.length() - 1) + "";
        perLetter.compute(firstLetter, (str, count) -> count+1);
        perLetter.compute(lastLetter, (str, count) -> count+1);
        var min = perLetter.values().stream().mapToLong(i -> i).min().getAsLong();
        var max = perLetter.values().stream().mapToLong(i -> i).max().getAsLong();
        return max - min;
    }


    static class Polymer {
        private Map<String, Long> count;

        public Polymer(String formula) {
            count = new HashMap<>();
            for (var i = 0; i < formula.length() - 1; ++i) {
                var str = formula.charAt(i) + "" + formula.charAt(i + 1);
                count.putIfAbsent(str, 0L);
                count.computeIfPresent(str, (key, value) -> value + 1);
            }
        }

        public Polymer(Map<String, Long> count) {
            this.count = count;
        }

        public Polymer step(Map<String, String> template) {
            Map<String, Long> newCount = new HashMap<>();

            this.count.forEach((key, value) -> {
                var newChar = template.get(key);
                var newChainLeft = key.charAt(0) + newChar;
                var newChainRight = newChar + key.charAt(1);
                newCount.putIfAbsent(newChainLeft, 0L);
                newCount.putIfAbsent(newChainRight, 0L);
                newCount.computeIfPresent(newChainLeft, (chain, count) ->
                        count + value
                );
                newCount.computeIfPresent(newChainRight, (chain, count) ->
                        count + value
                );
            });

            return new Polymer(newCount);
        }
    }

//    import java.nio.file.*;
//
//    class Day14b {
//        public static void main(String[] args) throws Exception {
//            List<String> lines = Files.readAllLines(Path.of(Day14.class.getResource("/day14.txt").toURI()));
//            var splitLines = lines.stream().map(line -> line.split(" -> ")).toList();
//
//            Map<String, String> leftMap = new HashMap<>();
//            Map<String, String> rightMap = new HashMap<>();
//            for (int i = 2; i < splitLines.size(); i++) {
//                String[] mapping = splitLines.get(i);
//                String from = mapping[0];
//                String to = mapping[1];
//                leftMap.put(from, from.charAt(0) + to);
//                rightMap.put(from, to + from.charAt(1));
//            }
//
//            String initial = splitLines.get(0)[0];
//            Map<String, Long> pairs = new HashMap<>(Map.of(initial.substring(initial.length() - 1), 1L));
//            for (int i = 0; i < initial.length() - 1; i++) {
//                pairs.compute(initial.substring(i, i + 2), (k, v) -> v == null ? 1 : v + 1);
//            }
//
//            for (int g = 0; g < 40; g++) {
//                Map<String, Long> newPairs = new HashMap<>(pairs.size());
//                pairs.forEach((p, freq) -> {
//                    if (leftMap.containsKey(p)) {
//                        newPairs.compute(leftMap.get(p), (k, f0) -> f0 == null ? freq : f0 + freq);
//                        newPairs.compute(rightMap.get(p), (k, f0) -> f0 == null ? freq : f0 + freq);
//                    } else {
//                        newPairs.compute(p, (k, f0) -> f0 == null ? freq : f0 + freq);
//                    }
//                });
//                pairs = newPairs;
//            }
//            List<Long> frequencies = pairs.entrySet().stream().collect(groupingBy(
//                            e -> e.getKey().charAt(0),
//                            summingLong(Map.Entry::getValue)))
//                    .values().stream().sorted().toList();
//            System.out.println(frequencies.get(frequencies.size() - 1) - frequencies.get(0));
//        }
}
