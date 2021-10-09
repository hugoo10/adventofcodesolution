package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class Problem19 extends Problem<Integer> {
    private Map<String, MultiRule> rules;
    private List<String> toTest;


    @Override
    public void setupData() {
        this.rules = new TreeMap<>();
        this.toTest = new ArrayList<>();
        int i = 0;
        for (; i < this.input.size(); ++i) {
            if (this.input.get(i).isEmpty()) break;
            String[] split = input.get(i).split(":");
            rules.put(split[0], new MultiRule(split[0],
                    Arrays.stream(split[1].strip().split("\\|"))
                            .map(Rule::new)
                            .collect(Collectors.toList()))
            );
        }
        i++;
        for (; i < this.input.size(); ++i) {
            this.toTest.add(this.input.get(i));
        }
        toTest.sort(String::compareTo);
    }

    @Override
    public Integer rule1() {
        return findSolution();
    }

    @Override
    public Integer rule2() {
        this.rules.put("8", new MultiRule("8", List.of(new Rule("42"), new Rule("42 800"))));
        this.rules.put("11", new MultiRule("11", List.of(new Rule("42 31"), new Rule("42 1100 31"))));

        int max = 0;
        for (int i = 0; i <= max; ++i) {
            String currentNum800 = String.format("8%02d", i);
            String currentNum1100 = String.format("11%02d", i);
            String nextNum800 = String.format("8%02d", i + 1);
            String nextNum1100 = String.format("11%02d", i + 1);

            if (i < max) {
                this.rules.put(currentNum800, new MultiRule(currentNum800, List.of(new Rule("42"), new Rule("42 " + nextNum800))));
                this.rules.put(currentNum1100, new MultiRule(currentNum1100, List.of(new Rule("42 31"), new Rule("42 " + nextNum1100 + " 31"))));
            } else {
                this.rules.put(currentNum800, new MultiRule(currentNum800, List.of(new Rule("42"))));
                this.rules.put(currentNum1100, new MultiRule(currentNum1100, List.of(new Rule("42 31"))));
            }
        }
        return findSolution();
    }

    private Integer findSolution() {
        for (MultiRule multiRule : rules.values()) {
            for (Rule rule : multiRule.rules) {
                rule.findSubRules(this.rules);
            }
        }
        int max = toTest.stream().map(String::length).max(Integer::compare).get();

        List<String> result = this.rules.get("0").getRules();
        result.sort(String::compareTo);

        List<String> matching = toTest.stream()
                //.filter(str -> str.length() == 24)
                .filter(result::contains)
                .collect(Collectors.toList());
        return matching.size();
    }

    static class Rule {
        //pre
        List<String> rulesStr;
        Character letter;

        //post
        List<Expression> expressions;
        List<String> resolved = null;

        public Rule(String rule) {
            String stripped = rule.strip();
            if (stripped.startsWith("\"")) {
                this.letter = stripped.charAt(1);
            } else {
                this.rulesStr = Arrays.asList(stripped.split(" "));
            }
        }

        private void findSubRules(Map<String, MultiRule> map) {
            this.expressions = new ArrayList<>();
            if (this.rulesStr != null) {
                this.rulesStr.stream()
                        .map(map::get)
                        .map(Expression::new)
                        .forEach(expressions::add);

            } else {
                this.expressions.add(new Expression(this.letter));
            }
        }

        private List<String> getRules() {
            if (resolved != null) {
                return this.resolved;
            }
            List<List<String>> ordered = new ArrayList<>();
            for (Expression expression : this.expressions) {
                List<String> subs = expression.getRules();
                ordered.add(subs);
            }
            int size = ordered.stream().map(List::size).reduce(1, (a, b) -> a * b);

            List<String> otherWay = new ArrayList<>();
            for (int i = 0; i < size; ++i) {
                otherWay.add(ordered.get(0).get(i % ordered.get(0).size()));
            }
            otherWay.sort(String::compareTo);
            for (int j = 1; j < ordered.size(); ++j) {
                for (int i = 0; i < size; ++i) {
                    otherWay.set(i, otherWay.get(i) + ordered.get(j).get(i % ordered.get(j).size()));
                }
            }
            this.resolved = otherWay;
            return otherWay;
        }
    }

    static class MultiRule {
        List<Rule> rules;
        String number;

        public MultiRule(String number, List<Rule> rules) {
            this.rules = rules;
            this.number = number;
        }

        private List<String> getRules() {
            List<String> rules = new ArrayList<>();
            for (Rule rule : this.rules) {
                rules.addAll(rule.getRules());
            }
            return rules;
        }
    }

    static class Expression {
        Character letter;
        MultiRule rules;

        public Expression(Character letter) {
            this.letter = letter;
        }

        public Expression(MultiRule rules) {
            this.rules = rules;
        }

        private List<String> getRules() {

            if (letter != null) {
                return new ArrayList<>(List.of(this.letter + ""));
            }
            return this.rules.getRules();
        }
    }
}
