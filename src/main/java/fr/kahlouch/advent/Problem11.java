package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Problem11 extends Problem {
    private static long flash1 = 0;

    private Octopus[][] octopi = new Octopus[10][10];

    @Override
    public void setupData() {
        for (var y = 0; y < 10; ++y) {
            for (var x = 0; x < 10; ++x) {
                octopi[x][y] = new Octopus(new Point(x, y), Integer.parseInt(input.get(y).charAt(x) + ""));
            }
        }
        for (var y = 0; y < 10; ++y) {
            for (var x = 0; x < 10; ++x) {
                octopi[x][y].setNeighbours(getNeighbours(octopi[x][y].position));
            }
        }
    }

    @Override
    public Object rule1() {
        for (var i = 0; i < 100; ++i) {
            for (var y = 0; y < 10; ++y) {
                for (var x = 0; x < 10; ++x) {
                    octopi[x][y].live();
                }
            }
            for (var y = 0; y < 10; ++y) {
                for (var x = 0; x < 10; ++x) {
                    octopi[x][y].prepareNextTurn();
                }
            }
        }
        return flash1;
    }

    @Override
    public Object rule2() {
        var i = 0;
        var synchro = false;
        do {
            for (var y = 0; y < 10; ++y) {
                for (var x = 0; x < 10; ++x) {
                    octopi[x][y].live();
                }
            }
            for (var y = 0; y < 10; ++y) {
                for (var x = 0; x < 10; ++x) {
                    octopi[x][y].prepareNextTurn();
                }
            }
            synchro = areAllOctopusSynchronized();
            ++i;
        } while (!synchro);
        return i;
    }

    static class Octopus {
        private final Point position;
        private List<Octopus> neighbours;
        private boolean hasFlashed = false;
        private int energy;

        public Octopus(Point position, int energy) {
            this.position = position;
            this.energy = energy;
        }

        public void setNeighbours(List<Octopus> neighbours) {
            this.neighbours = neighbours;
        }

        public void prepareNextTurn() {
            hasFlashed = false;
        }

        public void live() {
            if (!hasFlashed) {
                if (this.energy == 9) {
                    flash();
                } else {
                    this.energy++;
                }
            }
        }

        private void flash() {
            hasFlashed = true;
            this.energy = 0;
            Problem11.flash1++;
            this.neighbours.forEach(Octopus::live);
        }
    }

    private List<Octopus> getNeighbours(Point pos) {
        final List<Octopus> octo = new ArrayList<>();
        for (var i = -1; i <= 1; ++i) {
            for (var j = -1; j <= 1; ++j) {
                if (j == 0 && i == 0) continue;
                getOctopusAt(pos.x + i, pos.y + j).ifPresent(octo::add);
            }
        }
        return octo;
    }

    private Optional<Octopus> getOctopusAt(int x, int y) {
        if (x >= 0 && x < 10 && y >= 0 && y < 10) {
            return Optional.of(this.octopi[x][y]);
        }
        return Optional.empty();
    }

    record Point(int x, int y) {
    }

    private boolean areAllOctopusSynchronized() {
        var val = octopi[0][0].energy;
        for (var y = 0; y < 10; ++y) {
            for (var x = 0; x < 10; ++x) {
                if (octopi[x][y].energy != val) return false;
            }
        }
        return true;
    }
}
