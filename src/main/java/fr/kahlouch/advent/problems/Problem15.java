package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Problem15 extends Problem {
    List<Sensor> sensors = new ArrayList<>();
    Map<Integer, TreeSet<Integer>> beaconsAtY = new TreeMap<>();
    Map<Integer, TreeSet<Integer>> sensorsAtY = new TreeMap<>();

    record Interval(int y, int from, int to) implements Comparable<Interval> {

        private boolean isOverlapping(Interval that) {
            return (this.from <= that.from && this.to >= that.from) ||
                    (this.from <= that.to && this.to >= that.to);
        }

        private boolean isNextTo(Interval that) {
            return this.to + 1 == that.from || this.from - 1 == that.to;
        }

        Optional<Interval> merge(Interval that) {
            if (!isOverlapping(that) && !isNextTo(that)) return Optional.empty();
            return Optional.of(new Interval(this.y, Math.min(this.from, that.from), Math.max(this.to, that.to)));
        }

        @Override
        public int compareTo(Interval o) {
            var fromC = Integer.compare(this.from, o.from);
            return fromC == 0 ? Integer.compare(this.to, o.to) : fromC;
        }
    }

    record Intervals(int y, TreeSet<Interval> intervalSet) {

        Intervals(int y, Interval interval) {
            this(y, new TreeSet<>(Set.of(interval)));
        }

        Intervals merge(Intervals that) {
            var merged = new TreeSet<Interval>();
            merged.addAll(this.intervalSet);
            merged.addAll(that.intervalSet);

            TreeSet<Interval> reduced = merged;
            int previousSize;
            do {
                previousSize = reduced.size();
                reduced = reduce(merged);
            } while (reduced.size() > 1 && reduced.size() < previousSize);
            return new Intervals(that.y, reduced);
        }

        static TreeSet<Interval> reduce(TreeSet<Interval> treeSet) {
            List<Interval> reduced = new ArrayList<>();
            var list = new ArrayList<>(treeSet);
            for (var i = 0; i < list.size() - 1; i += 2) {
                var interval1 = list.get(i);
                var interval2 = list.get(i + 1);
                var mergedOpt = interval1.merge(interval2);
                if (mergedOpt.isPresent()) {
                    reduced.add(mergedOpt.get());
                } else {
                    reduced.add(interval1);
                    reduced.add(interval2);
                }
            }
            if (list.size() % 2 == 1) {
                var interval2 = list.get(list.size() - 1);
                if(list.size() == 1) {
                    reduced.add(interval2);
                } else {
                    var interval1 = reduced.get(reduced.size() - 1);
                    reduced.remove(reduced.size() - 1);
                    var mergedOpt = interval1.merge(interval2);
                    if (mergedOpt.isPresent()) {
                        reduced.add(mergedOpt.get());
                    } else {
                        reduced.add(interval1);
                        reduced.add(interval2);
                    }
                }
            }
            return new TreeSet<>(reduced);
        }
    }

    record BigSensor(TreeMap<Integer, Intervals> intervalMap) {
        static AtomicInteger merged = new AtomicInteger(0);

        BigSensor merge(BigSensor other) {
            var yToInspect = new TreeSet<Integer>();
            yToInspect.addAll(this.intervalMap.keySet());
            yToInspect.addAll(other.intervalMap.keySet());
            TreeMap<Integer, Intervals> resultMap = new TreeMap<>();
            for (var y : yToInspect) {
                if (!this.intervalMap.containsKey(y)) {
                    resultMap.put(y, other.intervalMap.get(y));
                } else if (!other.intervalMap.containsKey(y)) {
                    resultMap.put(y, this.intervalMap.get(y));
                } else {
                    resultMap.put(y, this.intervalMap.get(y).merge(other.intervalMap.get(y)));
                }
            }
            System.out.println(merged.incrementAndGet());
            return new BigSensor(resultMap);
        }

    }

    record Sensor(Point position, Beacon beacon) {
        BigSensor toBigSensor(int topMargin) {
            var minMax = new TreeMap<Integer, Intervals>();
            var maxDistance = distanceFromBeacon();
            for (var diffY = 0; diffY <= maxDistance; ++diffY) {
                var diffX = Math.abs(maxDistance - diffY);
                var fromX = Math.max(0, this.position.x - diffX);
                var toX = Math.min(topMargin, this.position.x + diffX);

                var newY = this.position.y + diffY;
                if (newY >= 0 && newY <= topMargin) {
                    minMax.putIfAbsent(newY, new Intervals(newY, new Interval(newY, fromX, toX)));
                }
                var mnewY = this.position.y - diffY;
                if (mnewY >= 0 && mnewY <= topMargin) {
                    minMax.putIfAbsent(mnewY, new Intervals(mnewY, new Interval(mnewY, fromX, toX)));
                }
            }
            return new BigSensor(minMax);
        }


        int distanceFromBeacon() {
            return distanceFrom(beacon.position);
        }

        int distanceFrom(Point point) {
            return Math.abs(this.position.x - point.x) + Math.abs(this.position.y - point.y);
        }

        Set<Point> getScannedAt(int y) {
            var maxDistance = distanceFromBeacon();
            var diffY = Math.abs(this.position.y - y);
            var scanned = new HashSet<Point>();
            for (var diffX = 0; diffX + diffY <= maxDistance; ++diffX) {
                scanned.add(new Point(this.position.x + diffX, y));
                scanned.add(new Point(this.position.x - diffX, y));
            }
            return scanned;
        }

        public boolean cover(Point p) {
            return this.distanceFrom(p) <= this.distanceFromBeacon();
        }
    }

    record Beacon(Point position) {
    }

    static Point parsePoint(String in) {
        var split = in.split(", ");
        return new Point(Integer.parseInt(split[0].substring(2)), Integer.parseInt(split[1].substring(2)));
    }

    @Override
    public void setupData() {
        lines.forEach(line -> {
            var split = line.replace("Sensor at ", "").split(": closest beacon is at ");
            var beacon = new Beacon(parsePoint(split[1]));
            var sensor = new Sensor(parsePoint(split[0]), beacon);
            sensors.add(sensor);
            beaconsAtY.putIfAbsent(beacon.position.y, new TreeSet<>());
            beaconsAtY.get(beacon.position.y).add(beacon.position.x);
            sensorsAtY.putIfAbsent(sensor.position.y, new TreeSet<>());
            sensorsAtY.get(sensor.position.y).add(sensor.position.x);
        });
    }

    /*
    --- Day 15: Beacon Exclusion Zone ---
    You feel the ground rumble again as the distress signal leads you to a large network of subterranean tunnels. You don't have time to search them all, but you don't need to: your pack contains a set of deployable sensors that you imagine were originally built to locate lost Elves.

    The sensors aren't very powerful, but that's okay; your handheld device indicates that you're close enough to the source of the distress signal to use them. You pull the emergency sensor system out of your pack, hit the big button on top, and the sensors zoom off down the tunnels.

    Once a sensor finds a spot it thinks will give it a good reading, it attaches itself to a hard surface and begins monitoring for the nearest signal source beacon. Sensors and beacons always exist at integer coordinates. Each sensor knows its own position and can determine the position of a beacon precisely; however, sensors can only lock on to the one beacon closest to the sensor as measured by the Manhattan distance. (There is never a tie where two beacons are the same distance to a sensor.)

    It doesn't take long for the sensors to report back their positions and closest beacons (your puzzle input). For example:

    Sensor at x=2, y=18: closest beacon is at x=-2, y=15
    Sensor at x=9, y=16: closest beacon is at x=10, y=16
    Sensor at x=13, y=2: closest beacon is at x=15, y=3
    Sensor at x=12, y=14: closest beacon is at x=10, y=16
    Sensor at x=10, y=20: closest beacon is at x=10, y=16
    Sensor at x=14, y=17: closest beacon is at x=10, y=16
    Sensor at x=8, y=7: closest beacon is at x=2, y=10
    Sensor at x=2, y=0: closest beacon is at x=2, y=10
    Sensor at x=0, y=11: closest beacon is at x=2, y=10
    Sensor at x=20, y=14: closest beacon is at x=25, y=17
    Sensor at x=17, y=20: closest beacon is at x=21, y=22
    Sensor at x=16, y=7: closest beacon is at x=15, y=3
    Sensor at x=14, y=3: closest beacon is at x=15, y=3
    Sensor at x=20, y=1: closest beacon is at x=15, y=3
    So, consider the sensor at 2,18; the closest beacon to it is at -2,15. For the sensor at 9,16, the closest beacon to it is at 10,16.

    Drawing sensors as S and beacons as B, the above arrangement of sensors and beacons looks like this:

                   1    1    2    2
         0    5    0    5    0    5
     0 ....S.......................
     1 ......................S.....
     2 ...............S............
     3 ................SB..........
     4 ............................
     5 ............................
     6 ............................
     7 ..........S.......S.........
     8 ............................
     9 ............................
    10 ....B.......................
    11 ..S.........................
    12 ............................
    13 ............................
    14 ..............S.......S.....
    15 B...........................
    16 ...........SB...............
    17 ................S..........B
    18 ....S.......................
    19 ............................
    20 ............S......S........
    21 ............................
    22 .......................B....
    This isn't necessarily a comprehensive map of all beacons in the area, though. Because each sensor only identifies its closest beacon, if a sensor detects a beacon, you know there are no other beacons that close or closer to that sensor. There could still be beacons that just happen to not be the closest beacon to any sensor. Consider the sensor at 8,7:

                   1    1    2    2
         0    5    0    5    0    5
    -2 ..........#.................
    -1 .........###................
     0 ....S...#####...............
     1 .......#######........S.....
     2 ......#########S............
     3 .....###########SB..........
     4 ....#############...........
     5 ...###############..........
     6 ..#################.........
     7 .#########S#######S#........
     8 ..#################.........
     9 ...###############..........
    10 ....B############...........
    11 ..S..###########............
    12 ......#########.............
    13 .......#######..............
    14 ........#####.S.......S.....
    15 B........###................
    16 ..........#SB...............
    17 ................S..........B
    18 ....S.......................
    19 ............................
    20 ............S......S........
    21 ............................
    22 .......................B....
    This sensor's closest beacon is at 2,10, and so you know there are no beacons that close or closer (in any positions marked #).

    None of the detected beacons seem to be producing the distress signal, so you'll need to work out where the distress beacon is by working out where it isn't. For now, keep things simple by counting the positions where a beacon cannot possibly be along just a single row.

    So, suppose you have an arrangement of beacons and sensors like in the example above and, just in the row where y=10, you'd like to count the number of positions a beacon cannot possibly exist. The coverage from all sensors near that row looks like this:

                     1    1    2    2
           0    5    0    5    0    5
     9 ...#########################...
    10 ..####B######################..
    11 .###S#############.###########.
    In this example, in the row where y=10, there are 26 positions where a beacon cannot be present.

    Consult the report from the sensors you just deployed. In the row where y=2000000, how many positions cannot contain a beacon?
     */
    @Override
    public Object rule1() {
        var checkY = 10;
        var scanned = new HashSet<Point>();
        sensors.forEach(sensor -> scanned.addAll(sensor.getScannedAt(checkY)));
        return scanned.size() - beaconsAtY.getOrDefault(checkY, new TreeSet<>()).size() - sensorsAtY.getOrDefault(checkY, new TreeSet<>()).size();
    }

    /*
    --- Part Two ---
    Your handheld device indicates that the distress signal is coming from a beacon nearby. The distress beacon is not detected by any sensor, but the distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.

    To isolate the distress beacon's signal, you need to determine its tuning frequency, which can be found by multiplying its x coordinate by 4000000 and then adding its y coordinate.

    In the example above, the search space is smaller: instead, the x and y coordinates can each be at most 20. With this reduced search area, there is only a single position that could have a beacon: x=14, y=11. The tuning frequency for this distress beacon is 56000011.

    Find the only possible position for the distress beacon. What is its tuning frequency?
     */
    @Override
    public Object rule2() {
        var topMargin = 4000000;
        var listd = sensors.stream()
                .map(s -> s.toBigSensor(topMargin))
                .reduce(new BigSensor(new TreeMap<>()), BigSensor::merge)
                .intervalMap.values();
        return listd.stream()
                .map(intervals -> {
                    var reduced = intervals.intervalSet;
                    reduced = Intervals.reduce(reduced);
                    reduced = Intervals.reduce(reduced);
                    reduced = Intervals.reduce(reduced);
                    reduced = Intervals.reduce(reduced);
                    return new Intervals(intervals.y, reduced);
                })
                .filter(intervals -> intervals.intervalSet.size() > 1)
                .findFirst()
                .map(intervals -> {
                    var list = new ArrayList<>(intervals.intervalSet);
                    return new Point(list.get(0).to + 1, intervals.y);
                }).map(p -> p.x * 4_000_000L + p.y).get();

    }
}
