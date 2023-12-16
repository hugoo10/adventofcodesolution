package fr.kahlouch.advent.year.year_2015;

import fr.kahlouch.advent.common.Day;

import java.nio.file.Path;

public class Day1 extends Day {
    public static void main(String[] args) {
        new Day1();
    }

    @Override
    protected Object resolve(Path inputPath) {
        return charStream(inputPath).toList();
    }
}
