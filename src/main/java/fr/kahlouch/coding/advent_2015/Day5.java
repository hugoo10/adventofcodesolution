package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.regex.Pattern;

class Day5 extends AdventProblem {
    public static void main(String[] args) {
        new Day5();
    }

    private Day5() {
        super(9);
    }

    protected Object resolve1(Path inputPath) {
        final Predicate<String> contains3Vowels = str -> Pattern.matches("^.*[aeiou].*[aeiou].*[aeiou].*$", str);
        final Predicate<String> contains2LettersTwiceInARow = str -> Pattern.matches("^.*(.)\\1.*$", str);
        final Predicate<String> containsForbiddenStrings = str ->
                Pattern.matches("^.*ab.*$", str) ||
                        Pattern.matches("^.*cd.*$", str) ||
                        Pattern.matches("^.*pq.*$", str) ||
                        Pattern.matches("^.*xy.*$", str);
        final Predicate<String> isNice = contains3Vowels.and(contains2LettersTwiceInARow).and(Predicate.not(containsForbiddenStrings));

        return Input.of(inputPath).multiLines()
                .lines()
                .filter(isNice)
                .count();
    }

    protected Object resolve2(Path inputPath) {
        final Predicate<String> containsPairTwice = str -> Pattern.matches("^.*(.{2}).*\\1.*$", str);
        final Predicate<String> containsLetterSandwich = str -> Pattern.matches("^.*(.).\\1.*$", str);
        final Predicate<String> isNice = containsPairTwice.and(containsLetterSandwich);

        return Input.of(inputPath).multiLines()
                .lines()
                .filter(isNice)
                .count();
    }
}
