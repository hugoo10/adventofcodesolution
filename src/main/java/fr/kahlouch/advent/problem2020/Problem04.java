package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.ProblemResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Problem04 {
    public static void main(String[] args) {
        new ProblemResolver("problem2020/problem04.txt", Problem04::rule1, Problem04::rule2).resolve();
    }

    public static String rule1(List<String> input) {
        List<String> mandatoryKeys = List.of(
                "byr",
                "iyr",
                "eyr",
                "hgt",
                "hcl",
                "ecl",
                "pid"
        );

        List<Map<String, String>> travelers = new ArrayList<>();
        Map<String, String> traveler = new HashMap<>();
        travelers.add(traveler);

        for (String info : input) {
            if (info.isEmpty()) {
                traveler = new HashMap<>();
                travelers.add(traveler);
            } else {
                for (String prop : info.split("\\s")) {
                    var splitted = prop.trim().split(":");
                    traveler.put(splitted[0], splitted[1]);
                }
            }
        }

        int count = 0;
        for (Map<String, String> tr : travelers) {
            if (mandatoryKeys.stream().allMatch(tr::containsKey)) {
                count++;
            }
        }
        return count + "";
    }

    public static String rule2(List<String> input) {
        Map<String, Predicate<String>> validation = Map.ofEntries(
                Map.entry("byr", s -> {
                    if (s.matches("\\d{4}")) {
                        int year = Integer.parseInt(s);
                        return year >= 1920 && year <= 2002;
                    }
                    return false;
                }),
                Map.entry("iyr", s -> {
                    if (s.matches("\\d{4}")) {
                        int year = Integer.parseInt(s);
                        return year >= 2010 && year <= 2020;
                    }
                    return false;
                }),
                Map.entry("eyr", s -> {
                    if (s.matches("\\d{4}")) {
                        int year = Integer.parseInt(s);
                        return year >= 2020 && year <= 2030;
                    }
                    return false;
                }),
                Map.entry("hgt", s -> {
                    if (s.matches("\\d+(cm|in)")) {
                        int size = Integer.parseInt(s.substring(0, s.length() - 2));
                        if (s.endsWith("cm")) {
                            return size >= 150 && size <= 193;
                        } else if (s.endsWith("in")) {
                            return size >= 59 && size <= 76;
                        }
                    }
                    return false;
                }),
                Map.entry("hcl", s -> s.matches("#(\\d|[a-f]){6}")),
                Map.entry("ecl", s -> List.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(s)),
                Map.entry("pid", s -> s.matches("\\d{9}"))
        );


        List<Map<String, String>> travelers = new ArrayList<>();
        Map<String, String> traveler = new HashMap<>();
        travelers.add(traveler);

        for (String info : input) {
            if (info.isEmpty()) {
                traveler = new HashMap<>();
                travelers.add(traveler);
            } else {
                for (String prop : info.split("\\s")) {
                    var splitted = prop.trim().split(":");
                    traveler.put(splitted[0], splitted[1]);
                }
            }
        }

        int count = 0;
        for (Map<String, String> tr : travelers) {
            if (validation.entrySet().stream().allMatch(en -> tr.containsKey(en.getKey()) && en.getValue().test(tr.get(en.getKey())))) {
                count++;
            }
        }
        return count + "";
    }
}
