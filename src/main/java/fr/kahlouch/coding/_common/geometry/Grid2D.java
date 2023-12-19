package fr.kahlouch.coding._common.geometry;

import org.apache.commons.collections4.list.FixedSizeList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class Grid2D<T> {
    private final List<List<T>> grid;

    private Grid2D(List<List<T>> grid) {
        this.grid = grid;
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
}
