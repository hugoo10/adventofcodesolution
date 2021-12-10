package fr.kahlouch.advent;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Stream;

public class Problem09 extends Problem {
    private HeightMap heightMap;

    @Override
    public void setupData() {
        this.heightMap = HeightMap.fromInput(this.input);
    }

    @Override
    public Object rule1() {
        return heightMap.getLowPoints().stream().mapToInt(i -> i.height + 1).sum();
    }


    @Override
    public Object rule2() {
        return heightMap.getBasins().stream().limit(3).reduce(1, (a, b) -> a * b);
    }

    record HeightMap(int[][] map) {
        static HeightMap fromInput(List<String> in) {
            var width = in.get(0).length();
            var height = in.size();
            var map = new int[width][height];
            for (var i = 0; i < in.size(); ++i) {
                for (var j = 0; j < in.get(i).length(); ++j) {
                    map[j][i] = Integer.parseInt(in.get(i).charAt(j) + "");
                }
            }
            return new HeightMap(map);
        }


        List<HeightPoint> getLowPoints() {
            List<HeightPoint> lowPoints = new ArrayList<>();
            for (var x = 0; x < this.map.length; ++x) {
                for (var y = 0; y < this.map[x].length; ++y) {
                    if (isLowerThatNeighbours(x, y)) {
                        lowPoints.add(new HeightPoint(x, y, this.map[x][y]));
                    }
                }
            }
            return lowPoints;
        }


        List<Integer> getBasins() {
            return getLowPoints()
                    .parallelStream()
                    .map(this::findBasin)
                    .sorted(Comparator.reverseOrder())
                    .toList();
        }

        private int findBasin(HeightPoint point) {
            Set<HeightPoint> non9 = new ConcurrentSkipListSet<>();
            findNon9(point.x, point.y, non9);
            return non9.size();
        }

        private void findNon9(int x, int y, Set<HeightPoint> non9) {
            if (!non9.contains(new HeightPoint(x, y, -1))) {
                getValueAt(x, y)
                        .filter(v -> v < 9)
                        .ifPresent(v -> {
                            non9.add(new HeightPoint(x, y, v));
                            Stream.of(
                                    Map.entry(x - 1, y), //left
                                    Map.entry(x + 1, y), //right
                                    Map.entry(x, y - 1), //up
                                    Map.entry(x, y + 1) //down
                            ).parallel().forEach(e -> findNon9(e.getKey(), e.getValue(), non9));
                        });
            }
        }


        boolean isLowerThatNeighbours(int x, int y) {
            var value = this.map[x][y];
            return Stream.of(
                            getValueAt(x - 1, y), //left
                            getValueAt(x + 1, y), //right
                            getValueAt(x, y - 1), //up
                            getValueAt(x, y + 1) //down
                    ).filter(Optional::isPresent)
                    .allMatch(v -> v.get() > value);
        }

        Optional<Integer> getValueAt(int x, int y) {
            if (x >= 0 && x < this.map.length && y >= 0 && y < this.map[x].length) {
                return Optional.of(this.map[x][y]);
            }
            return Optional.empty();
        }

    }

    record HeightPoint(int x, int y, int height) implements Comparable {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            HeightPoint that = (HeightPoint) o;

            return new EqualsBuilder().append(x, that.x).append(y, that.y).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(x).append(y).toHashCode();
        }

        @Override
        public int compareTo(Object o) {
            return this.toCompare().compareTo(((HeightPoint)o).toCompare());
        }

        @Override
        public String toString() {
            return x + "-" + y + "-" + height;
        }

        public String toCompare(){
            return x + "-" + y;
        }
    }
}
