package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;

class Day8 extends AdventProblem {
    public static void main(String[] args) {
        new Day8();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        return Input.of(inputPath).multiLines()
                .lines(this::computeNbLetters)
                .mapToLong(i -> i)
                .sum();
    }

    @Override
    protected Object resolve2(Path inputPath) {
        return Input.of(inputPath).multiLines()
                .lines(this::reverse)
                .mapToLong(i -> i)
                .sum();
    }

    public int computeNbLetters(String input, long idx) {
        final var nbStringLitterals = input.length();
        var toClean = input.substring(0, input.length() - 1)
                .substring(1);
        var cleaned = replace(toClean);
        return nbStringLitterals - cleaned.length();
    }

    private String replace(String toClean) {
        return   toClean
                .replace("\\\\", "|")
                .replace("\\\"", "'")
                .replaceAll("\\\\x.{2}", "&");
    }

    public int reverse(String input, long idx) {
        final var nbStringLitterals = input.length();
        final var dirtied = input.replace("\\", "\\\\")
                .replace("\"", "\\\"");
        final var dirtierLength = dirtied.length() + 2;
        return dirtierLength - nbStringLitterals;
    }
}
