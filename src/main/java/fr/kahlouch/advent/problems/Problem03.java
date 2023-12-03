package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;


public class Problem03 extends Problem {
    public static void main(String[] args) {
        Problem.solve(Problem03.class);
    }

    private final Map<Integer, Set<Number>> numbersMap = new HashMap<>();
    private final Set<Symbol> symbols = new HashSet<>();

    @Override
    public void setupData() {
        for (var y = 0; y < this.lines.size(); y++) {
            final var line = this.lines.get(y);
            this.numbersMap.put(y, new HashSet<>());

            int fromX = -1;
            for (var x = 0; x < line.length(); ++x) {
                final var caracatere = line.charAt(x);
                if (!Character.isDigit(caracatere)) {
                    if (caracatere != '.') {
                        this.symbols.add(new Symbol(caracatere, x, y));
                    }
                    if (fromX >= 0) {
                        addNumberAt(fromX, x - 1, y, line);
                    }
                    fromX = -1;
                } else {
                    if (fromX < 0) {
                        fromX = x;
                    }
                    if (x + 1 >= line.length()) {
                        addNumberAt(fromX, x, y, line);
                    }
                }
            }
        }
    }

    private void addNumberAt(int fromX, int toX, int y, String line) {
        final var valueStr = line.substring(fromX, toX + 1);
        final var value = Long.parseLong(valueStr);
        final var number = new Number(value, fromX, toX, y);
        this.numbersMap.get(y).add(number);
    }

    @Override
    public Object rule1() {
        final Set<Number> partNumbers = new HashSet<>();
        this.symbols.forEach(symbol -> {
            for (var y = symbol.y - 1; y <= symbol.y + 1; ++y) {
                if (!numbersMap.containsKey(y)) continue;
                numbersMap.get(y).forEach(number -> {
                    if (number.isAdjacentTo(symbol)) {
                        partNumbers.add(number);
                    }
                });
            }
        });
        return partNumbers.stream().mapToLong(Number::value).sum();
    }

    @Override
    public Object rule2() {
        long sum = 0;
        return this.symbols.stream().filter(Symbol::isPossibleGear).map(symbol -> {
                    final var partNumbers = new HashSet<Number>();
                    for (var y = symbol.y - 1; y <= symbol.y + 1; ++y) {
                        if (!numbersMap.containsKey(y)) continue;
                        numbersMap.get(y).forEach(number -> {
                            if (number.isAdjacentTo(symbol)) {
                                partNumbers.add(number);
                            }
                        });
                    }
                    return partNumbers;
                }).filter(set -> set.size() == 2)
                .mapToLong(set -> {
                    final var list = new ArrayList<Number>(set);
                    return list.get(0).value * list.get(1).value;
                }).sum();
    }

    record Number(UUID id, long value, int fromX, int toX, int y) {
        public Number(long value, int fromX, int toX, int y) {
            this(UUID.randomUUID(), value, fromX, toX, y);
        }

        boolean isAdjacentTo(Symbol symbol) {
            Objects.requireNonNull(symbol);
            return symbol.isAdjacentTo(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Number number = (Number) o;

            return Objects.equals(id, number.id);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }
    }

    record Symbol(char value, int x, int y) {
        boolean isAdjacentTo(Number number) {
            Objects.requireNonNull(number);
            if (this.y != number.y() && this.y > number.y() + 1 && this.y < number.y() - 1) {
                return false;
            }
            if (this.x > number.toX() + 1) {
                return false;
            }
            return this.x >= number.fromX() - 1;
        }

        boolean isPossibleGear() {
            return this.value == '*';
        }
    }
}

