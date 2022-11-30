package fr.kahlouch.advent;

import fr.kahlouch.advent.exception.GenericException;

import java.lang.reflect.Constructor;
import java.util.logging.Logger;

public interface ProblemSolver {
    Logger log = Logger.getLogger("ProblemSolver");

    static <P extends Problem> void solve(String path, Class<P> clazz) {
        try {
            final Constructor<P> constructor = clazz.getConstructor();
            log.info("Solution 1: " + constructor.newInstance().init(path).rule1());
            log.info("Solution 2: " + constructor.newInstance().init(path).rule2());
        } catch (Exception e) {
            throw new GenericException("Une erreur innatendue", e);
        }
    }
}
