package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.stream.Collectors;

public class Problem19 extends Problem<Integer> {
    private Map<String, List<Rule>> rulesStr;
    private List<String> toTest;


    @Override
    public void setupData() {
        this.rulesStr = new TreeMap<>();
        this.toTest = new ArrayList<>();
        int i = 0;
        for (; i < this.input.size(); ++i) {
            if (this.input.get(i).isEmpty()) break;
            String[] split = input.get(i).split(":");
            rulesStr.put(split[0], Arrays.stream(split[1].strip().split("\\|"))
                    .map(Rule::new)
                    .collect(Collectors.toList()));
        }
        i++;
        for (; i < this.input.size(); ++i) {
            this.toTest.add(this.input.get(i));
        }
        for (List<Rule> rules : rulesStr.values()) {
            for (Rule rule : rules) {
                rule.findSubRules(rulesStr);
            }
        }
    }

    @Override
    public Integer rule1() {
        List<Result> result = this.rulesStr.get("6").get(0).getRules();
        return null;
    }

    @Override
    public Integer rule2() {
        return null;
    }

    static class Rule {
        Character letter;
        List<String> rulesStr;
        List<List<Rule>> rules;

        public Rule(String rule) {
            String stripped = rule.strip();
            if (stripped.startsWith("\"")) {
                this.letter = stripped.charAt(1);
            } else {
                this.rulesStr = Arrays.asList(stripped.split(" "));
            }
        }

        private void findSubRules(Map<String, List<Rule>> map) {
            if (this.rulesStr != null) {
                this.rules = this.rulesStr.stream()
                        .map(map::get)
                        .collect(Collectors.toList());
            }
        }

        private List<Result> getRules() {
            if (this.letter != null) {
                return List.of(new Result("" + this.letter));
            }

            List<List<List<Result>>> list = new ArrayList<>();
            for(List<Rule> rules : this.rules) {
                List<List<Result>> subList = new ArrayList<>();
                for(Rule rule : rules) {
                    subList.add(rule.getRules());
                }
                list.add(subList);
            }

            return null;
        }
    }

    static class Result {
        String str;

        public Result(String str) {
            this.str = str;
        }
    }
}
