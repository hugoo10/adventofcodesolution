package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Problem04 extends Problem {
    final List<BingoGrid> bingoGrids = new ArrayList<>();
    List<Integer> numbers;

    @Override
    public void setupData() {
        this.numbers = Arrays.stream(this.input.get(0).split(","))
                .map(Integer::parseInt)
                .toList();
        for (int i = 2; i < this.input.size(); i += 6) {
            this.bingoGrids.add(BingoGrid.fromList(this.input.subList(i, i + 5)));
        }
    }

    @Override
    public Object rule1() {
        for (var nb : this.numbers) {
            Optional<BingoGrid> winner = this.bingoGrids.parallelStream()
                    .peek(bg -> bg.applyNumber(nb))
                    .filter(BingoGrid::isWin)
                    .findAny();
            if (winner.isPresent()) {
                var sum = winner.get().getSumNonChecked();
                return sum * nb;
            }
        }

        return null;
    }

    @Override
    public Object rule2() {
        var loosers = new ArrayList<>(this.bingoGrids);
        for (var nb : this.numbers) {
            loosers.parallelStream()
                    .forEach(bg -> bg.applyNumber(nb));
            var res = loosers.stream().filter(BingoGrid::isWin)
                    .map(bg -> {
                        var sum = bg.getSumNonChecked();
                        System.out.printf("[%s] SUM: %s - NB: %s = %s%n", bg.idx, sum, nb, sum * nb);
                        return bg.idx;
                    }).toList();
            loosers.removeIf(bg -> res.contains(bg.idx));
        }
        return null;
    }

    record BingoGrid(int idx, BingoCase[][] grid) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BingoGrid bingoGrid = (BingoGrid) o;

            return idx == bingoGrid.idx;
        }

        @Override
        public int hashCode() {
            return idx;
        }

        private static int cnt = 0;

        static BingoGrid fromList(List<String> gridStr) {
            BingoCase[][] grid = new BingoCase[5][5];
            for (var i = 0; i < grid.length; ++i) {
                var subStr = gridStr.get(i).strip().replace("  ", " ").split(" ");
                for (var j = 0; j < 5; ++j) {
                    try {
                        grid[i][j] = new BingoCase(Integer.parseInt(subStr[j]));
                    } catch (Exception e) {
                        System.err.println(e);
                    }
                }
            }
            return new BingoGrid(cnt++, grid);
        }

        void applyNumber(int nb) {
            Arrays.stream(this.grid)
                    .parallel()
                    .flatMap(Arrays::stream)
                    .forEach(bc -> {
                        try {
                            if (bc.value == nb) {
                                bc.checked = true;
                            }
                        } catch (Exception e) {
                            System.err.println();
                        }
                    });
        }

        boolean isWin() {
            return Arrays.stream(this.grid)
                    .anyMatch(line -> Arrays.stream(line).allMatch(bc -> bc.checked));
        }

        int getSumNonChecked() {
            return Arrays.stream(this.grid)
                    .flatMap(Arrays::stream)
                    .filter(bc -> !bc.checked)
                    .map(bc -> bc.value)
                    .reduce(0, (a, b) -> a + b);
        }

    }

    static class BingoCase {
        private int value;
        private boolean checked;

        public BingoCase(int value) {
            this.value = value;
            this.checked = false;
        }
    }
}
