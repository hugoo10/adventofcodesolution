package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.math.BinaryOperations;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.BiFunction;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Day7 extends AdventProblem {
    public static void main(String[] args) {
        new Day7();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var unresolved = Input.of(inputPath).multiLines()
                .lines(FormulaParser.INSTANCE)
                .collect(Collectors.toSet());
        final var resolved = new TreeMap<String, Long>();
        do {
            for (final var formula : new HashSet<>(unresolved)) {
                formula.resolve(resolved).ifPresent(result -> {
                    unresolved.remove(formula);
                    resolved.put(formula.id, result);
                });
            }
        } while (!unresolved.isEmpty());
        return resolved.get("a");
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var unresolved = Input.of(inputPath).multiLines()
                .lines(FormulaParser.INSTANCE)
                .map(f -> {
                    if (f.id.equals("b")) {
                        return new Formula("b", "3176");
                    }
                    return f;
                })
                .collect(Collectors.toSet());
        final var resolved = new TreeMap<String, Long>();
        do {
            for (final var formula : new HashSet<>(unresolved)) {
                formula.resolve(resolved).ifPresent(result -> {
                    unresolved.remove(formula);
                    resolved.put(formula.id, result);
                });
            }
        } while (!unresolved.isEmpty());
        return resolved.get("a");
    }

    private enum FormulaParser implements Parser<Formula> {
        INSTANCE;

        private static final Pattern FORMULA_PATTERN = Pattern.compile("^(.+)\\s->\\s(.+)$");

        @Override
        public Formula parse(String input, long idx) {
            final var matcher = FORMULA_PATTERN.matcher(input);
            if (matcher.matches()) {
                final var formula = matcher.group(1);
                final var id = matcher.group(2);
                return new Formula(id, formula);
            }
            throw new IllegalArgumentException("could not parse " + input);
        }
    }

    private record Formula(String id, String formula) {
        private static final Pattern NUMBER_PATTERN = Pattern.compile("^(\\d+)$");
        private static final long MAX = 65535;

        public Optional<Long> resolve(Map<String, Long> resolved) {
            try {
                if (NUMBER_PATTERN.matcher(formula).matches()) {
                    return Optional.of(Long.parseLong(formula));
                }
                if (formula.startsWith("NOT")) {
                    return getValueFromIdentifiant(formula.substring(4), resolved)
                            .map(BinaryOperations::complement)
                            .map(v -> v < 0 ? MAX + v + 1 : v);
                }
                final var split = formula.split("\\s");
                if (split.length == 1) {
                    return getValueFromIdentifiant(split[0], resolved);
                }
                final var left = getValueFromIdentifiant(split[0], resolved);
                final var right = getValueFromIdentifiant(split[2], resolved);
                final var operation = split[1];
                if (left.isEmpty() || right.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(getOperation(operation).apply(left.get(), right.get()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private Optional<Long> getValueFromIdentifiant(String id, Map<String, Long> resolved) {
            if (NUMBER_PATTERN.matcher(id).matches()) {
                return Optional.of(Long.parseLong(id));
            }
            return Optional.ofNullable(resolved.get(id));
        }

        private BiFunction<Long, Long, Long> getOperation(String operation) {
            return switch (operation) {
                case "AND" -> BinaryOperations::and;
                case "OR" -> BinaryOperations::or;
                case "LSHIFT" -> BinaryOperations::leftShift;
                case "RSHIFT" -> BinaryOperations::unsignedRightShift;
                default -> throw new IllegalArgumentException("Unknown operation: " + operation);
            };
        }
    }
}
