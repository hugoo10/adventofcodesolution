package fr.kahlouch.advent;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem12 extends Problem {
    private final Map<String, Cave> caves = new HashMap<>();

    @Override
    public void setupData() {
        this.input.forEach(link -> {
            final var split = link.split("-");
            final var cave1 = caves.computeIfAbsent(split[0], Cave::new);
            final var cave2 = caves.computeIfAbsent(split[1], Cave::new);
            cave1.addNeighbour(cave2);
            cave2.addNeighbour(cave1);
        });
    }

    @Override
    public Object rule1() {
        final var paths = computePaths("start", new ArrayList<>());
        return paths.size();
    }

    private List<List<String>> computePaths(String name, List<String> path) {
        final var cave = caves.get(name);
        if (cave.isLittle && path.contains(name)) {
            return null;
        }
        path.add(name);
        List<List<String>> solutions = new ArrayList<>();

        if ("end".equals(name)) {
            solutions.add(path);
        } else {
            for (var neighbours : cave.neighbours) {
                final var availablePaths = computePaths(neighbours.name, new ArrayList<>(path));
                if (availablePaths != null) {
                    solutions.addAll(availablePaths);
                }
            }
        }
        return solutions;
    }

    private List<List<String>> computePaths2(String name, Path p) {
        final var cave = caves.get(name);
        var doubleLittle = p.already2;
        if (cave.isLittle) {
            final var count = p.path.parallelStream().filter(n -> n.equals(name)).count();
            if (cave.isExtreme()) {
                if (count == 1) {
                    return null;
                }
            } else if (count >= 1) {
                if (doubleLittle) {
                    return null;
                }
                doubleLittle = true;
            }
        }
        p.path.add(name);
        List<List<String>> solutions = new ArrayList<>();

        if ("end".equals(name)) {
            solutions.add(p.path);
        } else {
            for (var neighbours : cave.neighbours) {
                final var availablePaths = computePaths2(neighbours.name, new Path(new ArrayList<>(p.path), doubleLittle));
                if (availablePaths != null) {
                    solutions.addAll(availablePaths);
                }
            }
        }
        return solutions;
    }

    @Override
    public Object rule2() {
        final var paths = computePaths2("start", new Path(new ArrayList<>(), false));
        //paths.forEach(System.out::println);
        return paths.size();
    }

    static class Cave {
        private String name;
        private boolean isLittle;
        private List<Cave> neighbours;

        public Cave(String name) {
            this.name = name;
            this.isLittle = !StringUtils.isAllUpperCase(this.name);
            this.neighbours = new ArrayList<>();
        }

        public boolean isExtreme() {
            return this.name.equals("start") || this.name.equals("end");
        }

        public void addNeighbour(Cave cave) {
            this.neighbours.add(cave);
        }
    }

    static record Path(List<String> path, boolean already2) {
    }

}
