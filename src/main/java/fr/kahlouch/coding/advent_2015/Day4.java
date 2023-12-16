package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.Problem;
import fr.kahlouch.coding._common.Responses;
import fr.kahlouch.coding._common.cryptography.Hash;
import fr.kahlouch.coding._common.input.Input;

import java.nio.file.Path;
import java.util.function.Predicate;

class Day4 extends Problem {
    public static void main(String[] args) {
        new Day4();
    }

    public Day4() {
        super(2);
    }

    @Override
    protected Object resolve(Path inputPath) {
        return Responses.of(
                resolve1(inputPath),
                resolve2(inputPath)
        );
    }

    private Object resolve1(Path inputPath) {
        final var start = Input.of(inputPath).line().content();
        final Predicate<String> startWith5Zeros = str -> str.startsWith("00000");
        for (var i = 0; ; i++) {
            if (startWith5Zeros.test(Hash.md5(start + i))) {
                return i;
            }
        }
    }

    private Object resolve2(Path inputPath) {
        final var start = Input.of(inputPath).line().content();
        final Predicate<String> startWith6Zeros = str -> str.startsWith("000000");
        for (var i = 0; ; i++) {
            if (startWith6Zeros.test(Hash.md5(start + i))) {
                return i;
            }
        }
    }
}
