package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.ProblemResolver;

import java.util.Arrays;
import java.util.List;

public class Problem02 {
    public static void main(String[] args) {
        new ProblemResolver("problem2020/problem02.txt", Problem02::rule1, Problem02::rule2).resolve();
    }

    public static String rule1(List<String> input) {
        int count = 0;
        for (String element : input) {
            String[] splitted = element.split("\\s");
            String nb = splitted[0];
            int from = Integer.parseInt(nb.split("-")[0]);
            int to = Integer.parseInt(nb.split("-")[1]);
            String letter = splitted[1].replace(":", "");
            String password = splitted[2];
            long c = Arrays.stream(password.split("")).filter(l -> l.equals(letter)).count();
            if (from <= c && c <= to) {
                count++;
            }
        }
        return count + "";
    }


    public static String rule2(List<String> input) {
        int count = 0;
        for (String element : input) {
            String[] splitted = element.split("\\s");
            String nb = splitted[0];
            int from = Integer.parseInt(nb.split("-")[0]);
            int to = Integer.parseInt(nb.split("-")[1]);
            char letter = splitted[1].replace(":", "").charAt(0);
            String password = splitted[2];
            if (password.charAt(from - 1) == letter ^ password.charAt(to - 1) == letter) {
                count++;
            }
        }
        return count + "";
    }
}
