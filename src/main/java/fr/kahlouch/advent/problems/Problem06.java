package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.Arrays;
import java.util.HashSet;

public class Problem06 extends Problem {
    String line;

    @Override
    public void setupData() {
        line = this.lines.get(0);
    }

    @Override
    public Object rule1() {
        var i = -1;
        boolean ok;
        do {
            ++i;
            var sub = line.substring(i, i + 4);
            var set = new HashSet<String>();
            ok = Arrays.stream(sub.split(""))
                    .map(set::add)
                    .filter(b -> !b)
                    .findFirst()
                    .orElse(true);
        } while (!ok);
        return i + 4;
    }

    @Override
    public Object rule2() {
        var i = -1;
        boolean ok;
        do {
            ++i;
            var sub = line.substring(i, i + 14);
            var set = new HashSet<String>();
            ok = Arrays.stream(sub.split(""))
                    .map(set::add)
                    .filter(b -> !b)
                    .findFirst()
                    .orElse(true);
        } while (!ok);
        return i + 14;
    }
}
