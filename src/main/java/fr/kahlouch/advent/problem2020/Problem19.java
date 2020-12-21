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
            rules.put(split[0], new MultiRule(
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
        for (MultiRule multiRule : rules.values()) {
            for (Rule rule : multiRule.rules) {
                rule.findSubRules(this.rules);
            }
        }
    }

    @Override
    public Integer rule1() {
        Set<String> result = new TreeSet<>(this.rules.get("0").getRules());
        List<String> matching = result.stream().filter(toTest::contains).collect(Collectors.toList());
        matching.sort(String::compareTo);
        return matching.size();
    }

    @Override
    public Integer rule2() {
        return null;
    }

    static class Rule {
        //pre
        List<String> rulesStr;
        Character letter;

        //post
        List<Expression> expressions;

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
            for (int j = 1; j < ordered.size(); ++j) {
                for (int i = 0; i < size; ++i) {
                    otherWay.set(i, otherWay.get(i) + ordered.get(j).get(i % ordered.get(j).size()));
                }
            }
            return otherWay;
        }
    }

    static class MultiRule {
        List<Rule> rules;

        public MultiRule(List<Rule> rules) {
            this.rules = rules;
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
