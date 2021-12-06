package fr.kahlouch.advent;

import java.util.*;

public class Problem05 extends Problem {
    List<Line> lines = new ArrayList<>();

    @Override
    public void setupData() {
        this.input.forEach(in -> lines.add(Line.fromStr(in)));
    }

    @Override
    public Object rule1() {
        var linesHV = lines.stream().filter(line ->
                line.isHorizontal() || line.isVertical()
        ).toList();
        var cross = new HashSet<Point>();
        for (int i = 0; i < linesHV.size() - 1; ++i) {
            for (int j = i + 1; j < linesHV.size(); ++j) {
                var crossing = linesHV.get(i).isCrossing(linesHV.get(j));
                cross.addAll(crossing);
            }
        }
        return cross.size();
    }

    @Override
    public Object rule2() {
        var count = 0;
        var map = new HashMap<Integer, HashMap<Integer, Integer>>();
        for (var line : lines) {
            var xdir = line.from.x <= line.to.x ? 1 : -1;
            var ydir = line.from.y <= line.to.y ? 1 : -1;

            var x = line.from.x - xdir;
            var y = line.from.y - ydir;

            do {
                x = xdir > 0 ? Math.min(x + xdir, line.to.x) : Math.max(x + xdir, line.to.x);
                y = ydir > 0 ? Math.min(y + ydir, line.to.y) : Math.max(y + ydir, line.to.y);
                if (!map.containsKey(x)) {
                    map.put(x, new HashMap<>());
                }
                if (!map.get(x).containsKey(y)) {
                    map.get(x).put(y, 0);
                }
                map.get(x).put(y, map.get(x).get(y) + 1);
                if (map.get(x).get(y) == 2) {
                    ++count;
                }
            } while (x != line.to.x || y != line.to.y);
        }
        return count;
    }

    record Point(int x, int y) {
        static Point fromStr(String str) {
            var split = str.split(",");
            var x = Integer.parseInt(split[0]);
            var y = Integer.parseInt(split[1]);
            return new Point(x, y);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Point point = (Point) o;

            if (x != point.x) return false;
            return y == point.y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }

        @Override
        public String toString() {
            return x + "," + y;
        }
    }

    record Line(Point from, Point to) {
        static Line fromStr(String str) {
            var split = str.split("->");
            var from = Point.fromStr(split[0].strip());
            var to = Point.fromStr(split[1].strip());
            return new Line(from, to);
        }

        List<Point> isCrossing(Line line) {
            if (isParallel(line)) {
                if (isHorizontal()) {
                    if (this.from.y == line.from.y) {
                        var meX = new int[]{this.from.x, this.to.x};
                        var youX = new int[]{line.from.x, line.to.x};
                        Arrays.sort(meX);
                        Arrays.sort(youX);
                        var res = new ArrayList<Point>();
                        for (var m = meX[0]; m <= meX[1]; ++m) {
                            for (var y = youX[0]; y <= youX[1]; ++y) {
                                if (m == y) {
                                    res.add(new Point(m, this.from.y));
                                }
                            }
                        }
                        return res;
                    }
                } else {
                    if (this.from.x == line.from.x) {
                        var meY = new int[]{this.from.y, this.to.y};
                        var youY = new int[]{line.from.y, line.to.y};
                        Arrays.sort(meY);
                        Arrays.sort(youY);
                        var res = new ArrayList<Point>();
                        for (var m = meY[0]; m <= meY[1]; ++m) {
                            for (var y = youY[0]; y <= youY[1]; ++y) {
                                if (m == y) {
                                    res.add(new Point(this.from.x, m));
                                }
                            }
                        }
                        return res;
                    }
                }
            } else if (isHorizontal()) {
                var testY = new int[]{this.from.y, line.from.y, line.to.y};
                Arrays.sort(testY);
                if (testY[1] == this.from.y) {
                    var testX = new int[]{line.from.x, this.from.x, this.to.x};
                    Arrays.sort(testX);
                    if (testX[1] == line.from.x) {
                        return Collections.singletonList(new Point(line.from.x, this.from.y));
                    }
                }
            } else {
                var testY = new int[]{line.from.y, this.from.y, this.to.y};
                Arrays.sort(testY);
                if (testY[1] == line.from.y) {
                    var testX = new int[]{this.from.x, line.from.x, line.to.x};
                    Arrays.sort(testX);
                    if (testX[1] == this.from.x) {
                        return Collections.singletonList(new Point(this.from.x, line.from.y));
                    }
                }
            }
            return Collections.emptyList();
        }

        boolean isParallel(Line line) {
            return (this.isHorizontal() && line.isHorizontal()) || (this.isVertical() && line.isVertical());
        }

        boolean isHorizontal() {
            return this.from.y == this.to.y;
        }

        boolean isVertical() {
            return this.from.x == this.to.x;
        }

        @Override
        public String toString() {
            return this.from.toString() + " -> " + this.to.toString();
        }
    }
}
