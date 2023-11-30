package fr.kahlouch.advent;

import fr.kahlouch.advent.exception.GenericException;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public interface ProblemSolver {
    Logger log = Logger.getLogger("ProblemSolver");

    static <P extends Problem> void solve(Class<P> clazz) {
        try {
            final var testFile = clazz.getSimpleName().toLowerCase() + "_test.txt";
            final var realFile = clazz.getSimpleName().toLowerCase() + ".txt";
            final Constructor<P> constructor = clazz.getConstructor();
            log.info("Solution TEST 1: " + constructor.newInstance().init(testFile).rule1());
            log.info("Solution TEST 2: " + constructor.newInstance().init(testFile).rule2());
            log.info("Solution 1: " + constructor.newInstance().init(realFile).rule1());
            log.info("Solution 2: " + constructor.newInstance().init(realFile).rule2());
        } catch (Exception e) {
            throw new GenericException("Une erreur innatendue", e);
        }
    }
}
