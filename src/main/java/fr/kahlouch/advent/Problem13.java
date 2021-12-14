package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.List;

public class Problem13 extends Problem {
    private Map initialMap;
    private List<FoldLine> foldLines = new ArrayList<>();

    @Override
    public void setupData() {
        this.initialMap = new Map(input);
        for (var str : input) {
            if (str.startsWith("fold along")) {
                if (str.startsWith("fold along y")) {
                    foldLines.add(new FoldLine(null, Integer.parseInt(str.substring(str.lastIndexOf("=") + 1))));
                } else {
                    foldLines.add(new FoldLine(Integer.parseInt(str.substring(str.lastIndexOf("=") + 1)), null));
                }
            }
        }
    }

    @Override
    public Object rule1() {
        return this.initialMap.fold(foldLines.get(0)).countDots();
    }

    @Override
    public Object rule2() {
        var map = initialMap;
        for (var fl : foldLines) {
            map = map.fold(fl);
        }
        return map.toString();
    }

    static class Map {
        private final boolean[][] map;

        public Map(List<String> in) {
            List<Point> points = new ArrayList<>();
            for (var str : in) {
                if (str.isEmpty()) {
                    break;
                }
                var split = str.split(",");
                int x = Integer.parseInt(split[0]);
                int y = Integer.parseInt(split[1]);
                points.add(new Point(x, y));
            }
            var maxX = points.stream().mapToInt(Point::x).max().getAsInt();
            var maxY = points.stream().mapToInt(Point::y).max().getAsInt();
            this.map = new boolean[maxX + 1][maxY + 1];
            points.forEach(p -> this.map[p.x][p.y] = true);
        }

        public Map(boolean[][] map) {
            this.map = map;
        }

        public Map fold(FoldLine foldLine) {
            int newX;
            int newY;
            if (foldLine.foldX != null) {
                newX = this.map.length / 2;
                newY = this.map[0].length;
            } else {
                newX = this.map.length;
                newY = this.map[0].length / 2;
            }
            var foldedMap = new boolean[newX][newY];
            for (var x = 0; x < newX; ++x) {
                for (var y = 0; y < newY; ++y) {
                    // si selon y  alors x identique et reversey = ymax - 1 - y
                    final boolean compareWith;
                    if (foldLine.foldX != null) {
                        compareWith = this.map[this.map.length - 1 - x][y];
                    } else {
                        compareWith = this.map[x][this.map[0].length - 1 - y];
                    }
                    foldedMap[x][y] = this.map[x][y] || compareWith;
                }
            }
            return new Map(foldedMap);
        }

        long countDots() {
            long count = 0;
            for (var l : this.map) {
                for (var b : l) {
                    if (b) {
                        ++count;
                    }
                }
            }
            return count;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (var y = 0; y < this.map[0].length; ++y) {
                sb.append("\n");
                for (var x = 0; x < this.map.length; ++x) {
                    sb.append(this.map[x][y] ? "#" : ".");
                }
            }
            return sb.toString();
        }
    }

    record FoldLine(Integer foldX, Integer foldY) {

    }

    record Point(int x, int y) {

    }
}
