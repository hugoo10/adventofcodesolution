package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;


public class Problem01 extends Problem {
    public static void main(String[] args) {
        Problem.solve(Problem01.class);
    }

    @Override
    public void setupData() {

    }

    @Override
    public Object rule1() {
        return this.lines.stream()
                .map(Calibration::extractCalibration)
                .mapToLong(Calibration::value)
                .sum();
    }

    @Override
    public Object rule2() {
        return this.lines.stream()
                .map(StringToDigitConverter::convert)
                .map(Calibration::extractCalibration)
                .mapToLong(Calibration::value)
                .sum();
    }

    @NoArgsConstructor
    private final class StringToDigitConverter {
        private static final Map<String, Integer> MATCHING_MAP = Map.of(
                "one", 1,
                "two", 2,
                "three", 3,
                "four", 4,
                "five", 5,
                "six", 6,
                "seven", 7,
                "eight", 8,
                "nine", 9
        );

        public static String convert(String line) {
            final var result = new StringBuilder();
            var tmpString = line;

            Optional<ReplaceWithIndex> foundReplacementOpt;
            do {
                final var sub = tmpString;
                foundReplacementOpt = MATCHING_MAP.entrySet()
                        .stream()
                        .map(entry -> {
                            if (sub.contains(entry.getKey())) {
                                return new ReplaceWithIndex(sub.indexOf(entry.getKey()), entry.getKey(), entry.getValue());
                            }
                            return null;
                        })
                        .filter(Objects::nonNull)
                        .sorted()
                        .findFirst();
                if (foundReplacementOpt.isPresent()) {
                    final var foundReplacement = foundReplacementOpt.get();
                    result.append(tmpString.substring(0, foundReplacement.index)).append(foundReplacement.to);
                    tmpString = tmpString.substring(foundReplacement.index + foundReplacement.from.length() - 1);
                } else {
                    result.append(tmpString.substring(0));
                }
            } while (foundReplacementOpt.isPresent());

            return result.toString();

        }

        record ReplaceWithIndex(int index, String from, int to) implements Comparable<ReplaceWithIndex> {

            @Override
            public int compareTo(ReplaceWithIndex o) {
                return this.index - o.index;
            }
        }

    }

    private record Calibration(long value) {
        private static Pattern DIGIT_PATTERN = Pattern.compile("\\D");

        static Calibration extractCalibration(String line) {
            Objects.requireNonNull(line);
            var lineWithOnlyDigits = DIGIT_PATTERN.matcher(line).replaceAll("");
            if (lineWithOnlyDigits.length() < 2) {
                lineWithOnlyDigits += lineWithOnlyDigits;
            } else if (lineWithOnlyDigits.length() > 2) {
                lineWithOnlyDigits = lineWithOnlyDigits.charAt(0) + "" + lineWithOnlyDigits.charAt(lineWithOnlyDigits.length() - 1);
            }
            final var value = Long.parseLong(lineWithOnlyDigits);
            return new Calibration(value);
        }
    }
}
