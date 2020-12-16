package fr.kahlouch.advent;

import java.lang.reflect.Constructor;

public class ProblemSolver {
    private ProblemSolver() {
    }

    public static <PROBLEM extends Problem<?>> void solve(String path, Class<PROBLEM> clazz) {
        try {
            final Constructor<PROBLEM> constructor = clazz.getConstructor();
            System.out.println("Solution 1: " + constructor.newInstance().init(path).rule1());
            System.out.println("Solution 2: " + constructor.newInstance().init(path).rule2());
        } catch (Exception e) {
            throw new RuntimeException("Une erreur innatendue", e);
        }
    }
}
