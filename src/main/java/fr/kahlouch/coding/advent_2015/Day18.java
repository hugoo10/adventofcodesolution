package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.geometry.Grid2D;
import fr.kahlouch.coding._common.geometry.Grid2DCollector;
import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Stream;

class Day18 extends AdventProblem {
    public static void main(String[] args) {
        new Day18();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        var grid = Input.of(inputPath)
                .multiLines()
                .lines()
                .collect(new Grid2DCollector(""))
                .apply(str -> str.equals("#"));

        for (int i = 0; i < 100; ++i) {
            grid = grid.applyOnEach(Day18::doStep);
        }

        return grid.stream().filter(v -> v).count();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        var grid = Input.of(inputPath)
                .multiLines()
                .lines()
                .collect(new Grid2DCollector(""))
                .apply(str -> str.equals("#"));

        for (int i = 0; i < 100; ++i) {
            grid = grid.applyOnEach(Day18::doStepBug);
        }

        return grid.stream().filter(v -> v).count();
    }

    private static boolean doStep(Grid2D<Boolean> grid, Grid2D.Coordinates pointCoord) {
        final var onNeighbour = Stream.of(
                        new Grid2D.Coordinates(-1, -1), new Grid2D.Coordinates(-1, 0), new Grid2D.Coordinates(-1, 1),
                        new Grid2D.Coordinates(0, -1), new Grid2D.Coordinates(0, 1),
                        new Grid2D.Coordinates(1, -1), new Grid2D.Coordinates(1, 0), new Grid2D.Coordinates(1, 1)
                ).map(pointCoord::apply)
                .map(grid::getValueAt)
                .mapMulti(Optional<Boolean>::ifPresent)
                .filter(b -> b)
                .count();

        final var currentValue = grid.getValueAt(pointCoord).orElseThrow();

        if (currentValue) {
            return onNeighbour == 2 || onNeighbour == 3;
        }
        return onNeighbour == 3;
    }

    private static boolean doStepBug(Grid2D<Boolean> grid, Grid2D.Coordinates pointCoord) {
        if (grid.isCorner(pointCoord)) return true;
        return doStep(grid, pointCoord);
    }
}
