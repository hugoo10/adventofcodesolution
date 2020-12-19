package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.util.List;

public class _Solver2020 {
    private static String folder = "problem2020";
    private static List<Class<? extends Problem>> problems = List.of(
            Problem01.class,
            Problem02.class,
            Problem03.class,
            Problem04.class,
            Problem05.class,
            Problem06.class,
            Problem07.class,
            Problem08.class,
            Problem09.class,
            Problem10.class,
            Problem11.class,
            Problem12.class,
            Problem13.class,
            Problem14.class,
            Problem15.class,
            Problem16.class,
            Problem17.class,
            Problem18.class,
            Problem19.class,
            Problem20.class,
            Problem21.class,
            Problem22.class,
            Problem23.class,
            Problem24.class
    );

    public static void main(String[] args) {
        int toSolve = Integer.parseInt(args[0]);
        ProblemSolver.solve(String.format("%s/problem%02d.txt", folder, toSolve), problems.get(toSolve - 1));
    }

}
