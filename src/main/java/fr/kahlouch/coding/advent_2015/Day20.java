package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.problem.AdventProblem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

class Day20 extends AdventProblem {
    public Day20() {
        super(0);
    }

    public static void main(String[] args) {
        new Day20();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var objectif = 36_000_000;
        final var obj_divided_by_10 = objectif / 10;

        long sum = 0;
        List<Long> values = new ArrayList<>();
        for(int i = 1; i <= obj_divided_by_10; i++) {
            sum += i;
            values.add((long)i);
            if(sum >= obj_divided_by_10) {
                break;
            }
        }
        List<Long> toUse = new ArrayList<>(values);
        for(int i =0; i < values.size(); i++) {
            for(int j = i+1; j < values.size(); j++) {
                if(values.get(j) % values.get(i) == 0) {
                    toUse.remove(values.get(i));
                    break;
                }
            }
        }

        return toUse.stream().mapToLong(i->i)
                .reduce(1L, (i,j) -> {

                    final var res =  i*j;
                    return res;
                });
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        return null;
    }
}
