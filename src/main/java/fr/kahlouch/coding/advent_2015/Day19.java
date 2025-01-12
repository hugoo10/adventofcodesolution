package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;
import fr.kahlouch.coding._common.string.StringUtils;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Day19 extends AdventProblem {
    public Day19() {
        super(2);
    }

    public static void main(String[] args) {
        new Day19();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var split = Input.of(inputPath)
                .multiLines()
                .splitOnEmptyLine();

        final var replacements = split.getFirst()
                .lines(ReplacementParser.INSTANCE)
                .toList();

        final var toReplace = split.getLast()
                .lines()
                .findFirst()
                .orElseThrow();

        final Set<String> molecules = new HashSet<>();

        for (var replacement : replacements) {
            int idx = 0;
            while (StringUtils.replaceFirstFrom(toReplace, replacement.from, replacement.to, idx)
                    instanceof StringUtils.ReplacedString(String newString, int foundIndex)) {
                molecules.add(newString);
                idx = foundIndex + 1;
            }
        }
        return molecules.size();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var split = Input.of(inputPath)
                .multiLines()
                .splitOnEmptyLine();

        final var replacements = split.getFirst()
                .lines(ReplacementParser.INSTANCE)
                .toList();

        final var cible = split.getLast()
                .lines()
                .findFirst()
                .orElseThrow();

        minCount = null;
        reverseToTarget(cible, replacements, 0, "e");
        return minCount;
    }

    private record Replacement(String from, String to) {
    }

    private enum ReplacementParser implements Parser<Replacement> {
        INSTANCE;

        private static final Regex REPLACEMENT_REGEX = new Regex("^(.+) => (.+)$");


        @Override
        public Replacement parse(String input, long idx) {
            final var matcher = REPLACEMENT_REGEX.match(input);
            return new Replacement(matcher.group(1), matcher.group(2));
        }
    }

    private static Integer minCount;


    private void reverseToTarget(String intermediate, List<Replacement> replacements, int count, String target) {
        if (minCount != null && minCount < count) {
            return;
        }

        if (intermediate.equals(target)) {
            if (minCount == null) {
                minCount = count;
            } else if (minCount > count) {
                minCount = count;
            }
        }


        for (var replacement : replacements) {
            int idx = 0;
            while (StringUtils.replaceFirstFrom(intermediate, replacement.to, replacement.from, idx)
                    instanceof StringUtils.ReplacedString(String newString, int foundIndex)) {
                reverseToTarget(newString, replacements, count + 1, target);
                idx = foundIndex + 1;
            }
        }
    }
}
