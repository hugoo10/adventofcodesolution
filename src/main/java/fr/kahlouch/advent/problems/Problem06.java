package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;


public class Problem06 extends Problem {
    final Set<Race> races = new HashSet<>();
    public static void main(String[] args) {
        Problem.solve(Problem06.class);
    }

    @Override
    public void setupData() {


        final var times = Arrays.stream(this.lines.get(0).replace("Time:", "").split(" ")).map(s -> s.strip().trim()).filter(Predicate.not(String::isEmpty)).map(Long::parseLong).toList();
        final var distances =  Arrays.stream(this.lines.get(1).replace("Distance:", "").split(" ")).map(s -> s.strip().trim()).filter(Predicate.not(String::isEmpty)).map(Long::parseLong).toList();

        for(var i =0; i<times.size();++i) {
            this.races.add(new Race(times.get(i), distances.get(i)));
        }
    }

    @Override
    public Object rule1() {
        return this.races.stream().mapToLong(Race::countWinningSolution).reduce(1, (left, right) -> left *right);
    }

    @Override
    public Object rule2() {
        return this.races.stream().mapToLong(Race::countWinningSolution).reduce(1, (left, right) -> left *right);
    }

    record Race(double time, long distanceToBeat) {
        long countWinningSolution() {
            final var delta =  (time * time) -4 * (-1d * -distanceToBeat);
            if (delta < 0) {
                return 0;
            }
            else if (delta == 0d) {
                return 1;
            } else {
                final var r1 = Math.round(Math.ceil((-time + Math.sqrt(delta)) / -2d));
                final var r2 = Math.round(Math.floor((-time - Math.sqrt(delta)) / -2d));

                var resp = Math.abs(r2-r1) + 1;
                final var valueAtR1 = Math.round(-Math.pow(r1,2) + r1 * time);
                if(distanceToBeat == valueAtR1) {
                    resp -= 2;
                }
                return resp;
            }
        }
    }

}
