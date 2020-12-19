package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Problem17 extends Problem<Integer> {
    private Map<Integer, Map<Integer, Map<Integer, Map<Integer, Boolean>>>> map;
    private Set<Point4D> activePoints;

    @Override
    public void setupData() {
        this.map = new HashMap<>();
        this.activePoints = new HashSet<>();

        for (int i = 0; i < this.input.size(); ++i) {
            for (int j = 0; j < this.input.get(i).length(); ++j) {
                boolean isActive = this.input.get(i).charAt(j) == '#';
                Point4D point = new Point4D(i, j, 0);
                setPointInMap(point, isActive);
                if (isActive) {
                    this.activePoints.add(point);
                }
            }
        }
    }


    private void setPointInMap(Point4D point, boolean active) {
        this.map.putIfAbsent(point.x, new HashMap<>());
        this.map.get(point.x).putIfAbsent(point.y, new HashMap<>());
        this.map.get(point.x).get(point.y).putIfAbsent(point.z, new HashMap<>());
        this.map.get(point.x).get(point.y).get(point.z).put(point.w, active);
    }

    private int getActiveNeighbors(Point4D point, boolean is4D) {
        int count = 0;
        int newX, newY, newZ, newW;
        for (int i = -1; i <= 1; ++i) {
            newX = point.x + i;
            if (map.containsKey(newX)) {
                for (int j = -1; j <= 1; ++j) {
                    newY = point.y + j;
                    if (map.get(newX).containsKey(newY)) {
                        for (int k = -1; k <= 1; ++k) {
                            newZ = point.z + k;
                            if (map.get(newX).get(newY).containsKey(newZ)) {
                                for (int l = (is4D ? -1 : 0); l <= (is4D ? 1 : 0); ++l) {
                                    newW = point.w + l;
                                    if (i == 0 && j == 0 && k == 0 && l == 0) continue;
                                    if (map.get(newX).get(newY).get(newZ).containsKey(newW) && map.get(newX).get(newY).get(newZ).get(newW)) {
                                        ++count;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    private Set<Point4D> getDeactivatedNeighbors(Point4D point, boolean is4D) {
        final Set<Point4D> deactivated = new HashSet<>();
        int newX, newY, newZ, newW;
        for (int i = -1; i <= 1; ++i) {
            newX = point.x + i;
            for (int j = -1; j <= 1; ++j) {
                newY = point.y + j;
                for (int k = -1; k <= 1; ++k) {
                    newZ = point.z + k;
                    for (int l = (is4D ? -1 : 0); l <= (is4D ? 1 : 0); ++l) {
                        newW = point.w + l;
                        if (!map.containsKey(newX)
                                || !map.get(newX).containsKey(newY)
                                || !map.get(newX).get(newY).containsKey(newZ)
                                || !map.get(newX).get(newY).get(newZ).containsKey(newW)
                                || !map.get(newX).get(newY).get(newZ).get(newW)) {
                            deactivated.add(new Point4D(newX, newY, newZ, newW));
                        }
                    }
                }
            }
        }
        return deactivated;
    }

    private Integer solve(boolean is4D) {
        final Set<Point4D> toActivate = new HashSet<>();
        final Set<Point4D> toDeactivate = new HashSet<>();
        final Set<Point4D> deactivatedToTest = new HashSet<>();

        for (int i = 0; i < 6; ++i) {
            toActivate.clear();
            toDeactivate.clear();
            deactivatedToTest.clear();

            this.activePoints.forEach(point -> {
                final int activeNeighbors = getActiveNeighbors(point, is4D);
                if (activeNeighbors != 2 && activeNeighbors != 3) {
                    toDeactivate.add(point);
                }
                deactivatedToTest.addAll(getDeactivatedNeighbors(point, is4D));
            });

            deactivatedToTest.forEach(point -> {
                final int activeNeighbors = getActiveNeighbors(point, is4D);
                if (activeNeighbors == 3) {
                    toActivate.add(point);
                }
            });

            this.activePoints.removeAll(toDeactivate);
            this.activePoints.addAll(toActivate);
            toDeactivate.forEach(point -> setPointInMap(point, false));
            toActivate.forEach(point -> setPointInMap(point, true));

        }
        return this.activePoints.size();
    }

    @Override
    public Integer rule1() {
        return solve(false);
    }

    @Override
    public Integer rule2() {
        return solve(true);
    }

    static class Point4D {
        private int x, y, z, w;

        public Point4D(int x, int y, int z, int w) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
        }

        public Point4D(int x, int y, int z) {
            this(x, y, z, 0);
        }

        public Point4D(int x, int y) {
            this(x, y, 0);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Point4D point4D = (Point4D) o;

            return new EqualsBuilder().append(x, point4D.x).append(y, point4D.y).append(z, point4D.z).append(w, point4D.w).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder(17, 37).append(x).append(y).append(z).append(w).toHashCode();
        }
    }
}
