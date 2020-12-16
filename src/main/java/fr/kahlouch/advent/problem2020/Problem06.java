package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Problem06 extends Problem<Integer> {
    public static List<Map<Character, Integer>> getAnswers(List<String> input) {
        List<Map<Character, Integer>> answers = new ArrayList<>();

        Map<Character, Integer> characters = new TreeMap<>();
        answers.add(characters);
        for (String line : input) {
            if (line.isEmpty()) {
                characters = new TreeMap<>();
                answers.add(characters);
            } else {
                for (char answer : line.toCharArray()) {
                    if (!characters.containsKey(answer)) {
                        characters.put(answer, 0);
                    }
                    characters.put(answer, characters.get(answer) + 1);
                }

            }
        }
        return answers;
    }

    @Override
    public Integer rule1() {
        List<Map<Character, Integer>> answers = getAnswers(input);
        return answers.stream().map(Map::size).reduce(0, Integer::sum);
    }

    @Override
    public Integer rule2() {
        List<Map<Character, Integer>> answers = getAnswers(input);
        List<Integer> count = new ArrayList<>();
        count.add(0);
        int idx = 0;
        for (String line : input) {
            if (line.isEmpty()) {
                idx++;
                count.add(0);
            } else {
                count.set(idx, count.get(idx) + 1);
            }
        }

        int total = 0;
        for (int i = 0; i < count.size(); ++i) {
            for(Map.Entry<Character, Integer> answer:answers.get(i).entrySet()) {
                if(answer.getValue().equals(count.get(i))) {
                    total ++;
                }
            }
        }
        return total;
    }
}
