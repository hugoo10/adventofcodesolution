package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;
import fr.kahlouch.coding._common.stream.Streams;

import java.nio.file.Path;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Day11 extends AdventProblem {
    public Day11() {
        super(5);
    }

    public static void main(String[] args) {
        new Day11();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var password = new Password(Input.of(inputPath).line().content());
        return password.nextValid().value;
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        return new Password((String)response1).nextValid().value;
    }

    record Password(String value) {
        private static final Predicate<String> DOUBLE_PAIRS = new Regex("^.*(.)\\1.*(.)\\2.*$").toPredicate();
        private static final String THREE_LETTERS_ROW = Streams.iterate(0, 23)
                .filter(i -> i < 6 || i > 14)
                .map(i -> {
                    final var first = (char) ('a' + i);
                    final var second = (char) ('a' + i + 1);
                    final var third = (char) ('a' + i + 2);
                    return "" + first + second + third;
                }).collect(Collectors.joining("|"));
        private static final Predicate<String> HAS_THREE_LETTERS_ROW = new Regex("^.*("+THREE_LETTERS_ROW+").*$").toPredicate();
        private static final Predicate<String> NOT_CONTAINS_FORBIDEN_LETTER = Predicate.not(new Regex("^.*[iol].*$").toPredicate());
        private static final Predicate<String> IS_VALID = DOUBLE_PAIRS.and(HAS_THREE_LETTERS_ROW).and(NOT_CONTAINS_FORBIDEN_LETTER);


        private Password nextValid() {
            var charArray = this.value.toCharArray();
            do {
                charArray = increment(charArray);
            } while (!IS_VALID.test(new String(charArray)));
            return new Password(new String(charArray));
        }

        private static char[] increment(char[] charArray) {
            final var incremented = new char[charArray.length];
            var increment = true;
            for (var i = charArray.length - 1; i >= 0; --i) {
                if (increment) {
                    final var newChar = charArray[i] + 1;
                    if (newChar > 'z') {
                        incremented[i] = 'a';
                    } else {
                        incremented[i] = (char) newChar;
                        increment = false;
                    }
                } else {
                    incremented[i] = charArray[i];
                }
            }
            return incremented;
        }
    }
}
