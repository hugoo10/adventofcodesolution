package fr.kahlouch.coding._common.geometry;

import org.apache.commons.collections4.list.FixedSizeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

public class Grid2D<T> {

    public record Coordinates(int rowNum, int colNum) {
        public Coordinates apply(Coordinates coordinates) {
            return new Coordinates(rowNum + coordinates.rowNum, colNum + coordinates.colNum);
        }
    }

    private final List<List<T>> grid;

    public Grid2D(List<List<T>> grid) {
        final var newGrid = new ArrayList<List<T>>();
        for (var row : grid) {
            final var newRow = List.copyOf(row);
            newGrid.add(newRow);
        }
        this.grid = List.copyOf(newGrid);
    }

    public <O> Grid2D<O> apply(Function<T, O> converter) {
        final var newGrid = new ArrayList<List<O>>();
        for (var row : grid) {
            final var newRow = new ArrayList<O>();
            for (var col : row) {
                newRow.add(converter.apply(col));
            }
            newGrid.add(newRow);
        }
        return new Grid2D<>(newGrid);
    }

    public Grid2D<T> applyOnEach(BiFunction<Grid2D<T>, Coordinates, T> functionOnCell) {
        final var newGrid = new ArrayList<List<T>>();
        for (var rowNum = 0; rowNum < this.grid.size(); ++rowNum) {
            final var row = new ArrayList<T>();
            for (var colNum = 0; colNum < this.grid.getFirst().size(); ++colNum) {
                row.add(functionOnCell.apply(this, new Coordinates(rowNum, colNum)));
            }
            newGrid.add(row);
        }

        return new Grid2D<>(newGrid);
    }

    public static <T> Grid2D<T> grid(int width, int height, T initValue) {
        final var grid = new ArrayList<List<T>>(width);
        Stream.iterate(0, i -> i < width, i -> i + 1)
                .forEach(i -> {
                    final var column = new ArrayList<T>(height);
                    for (var j = 0; j < height; j++) {
                        column.add(initValue);
                    }
                    grid.add(FixedSizeList.fixedSizeList(column));
                });
        return new Grid2D<>(FixedSizeList.fixedSizeList(grid));
    }

    public static <T> Grid2D<T> grid(Class<T> clazz, int width, int height) {
        return grid(width, height, null);
    }

    public static Grid2D<Boolean> booleanGrid(int width, int height, boolean initAt) {
        return grid(width, height, initAt);
    }

    public static Grid2D<Integer> intGrid(int width, int height, int initAt) {
        return grid(width, height, initAt);
    }

    public Stream<T> stream() {
        return this.grid.stream().flatMap(Collection::stream);
    }

    public Optional<T> getValueAt(int rowNum, int colNum) {
        if (rowNum < 0 || rowNum >= this.grid.size()) {
            return Optional.empty();
        }
        final var row = this.grid.get(rowNum);

        if (colNum < 0 || colNum >= row.size()) {
            return Optional.empty();
        }
        return Optional.of(row.get(colNum));
    }

    public Optional<T> getValueAt(Coordinates coordinates) {
        return this.getValueAt(coordinates.rowNum, coordinates.colNum);
    }

    public void applyDiagonally(Point2D from, Point2D to, Function<T, T> transform) {
        final var minX = Math.min(from.x(), to.x());
        final var minY = Math.min(from.y(), to.y());
        final var maxX = Math.max(from.x(), to.x());
        final var maxY = Math.max(from.y(), to.y());

        for (var x = minX; x <= maxX; ++x) {
            for (var y = minY; y <= maxY; ++y) {
                final var line = this.grid.get((int) x);
                line.set((int) y, transform.apply(line.get((int) y)));
            }
        }
    }

    public boolean isCorner(Coordinates coordinates) {
        final var lastRow = this.grid.size() - 1;
        final var lastCol = this.grid.getFirst().size() - 1;

        return coordinates.rowNum == 0 && coordinates.colNum == 0 ||
                coordinates.rowNum == lastRow && coordinates.colNum == lastCol ||
                coordinates.rowNum == 0 && coordinates.colNum == lastCol ||
                coordinates.rowNum == lastRow && coordinates.colNum == 0;
    }
}
