package fr.kahlouch.advent;

import fr.kahlouch.advent.exception.GenericException;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Logger;

public abstract class Problem {
    private static final Logger log = Logger.getLogger("ProblemSolver");
    private static final Path RESOURCES_FOLDER = Path.of("").toAbsolutePath().resolve(Path.of("src", "main", "resources"));


    public static <P extends Problem> void solve(Class<P> problemClass) {
        final var className = problemClass.getSimpleName();
        final var dayStr = className.substring(className.length() - 2);
        final var day = Integer.parseInt(dayStr);
        solve(problemClass, day, 1);
        solve(problemClass, day, 2);
    }

    public static <P extends Problem> void solvePart1(Class<P> problemClass, int day) {
        solve(problemClass, day, 1);
    }

    public static <P extends Problem> void solvePart2(Class<P> problemClass, int day) {
        solve(problemClass, day, 2);
    }

    public static <P extends Problem> void solve(Class<P> problemClass, int day, int part) {
        if (day < 1 || day > 24) {
            throw new IllegalArgumentException("Le jour doit être en 1 et 24");
        }
        if (part != 1 && part != 2) {
            throw new IllegalArgumentException("La partie d'exercice ne peut être que 1 ou 2");
        }
        try {
            final var problem = "problem%02d".formatted(day);
            final var testFile = problem + "_part%s_test.txt".formatted(part);
            final var realFile = problem + "_part%s.txt".formatted(part);
            final Constructor<P> constructor = problemClass.getConstructor();
            if (part == 1) {
                log.info("Solution TEST 1: " + constructor.newInstance().init(testFile.formatted("1")).rule1());
                log.info("Solution 1: " + constructor.newInstance().init(realFile.formatted("1")).rule1());
            } else {
                log.info("Solution TEST 2: " + constructor.newInstance().init(testFile.formatted("2")).rule2());
                log.info("Solution 2: " + constructor.newInstance().init(realFile.formatted("2")).rule2());
            }
        } catch (Exception e) {
            throw new GenericException("Une erreur innatendue", e);
        }
    }

    protected List<String> lines;

    public Problem init(String fileName) {
        final var testInput = RESOURCES_FOLDER.resolve(fileName);
        try (final var linesStream = Files.lines(testInput)) {
            this.lines = linesStream.toList();
        } catch (IOException ioe) {
            throw new GenericException("Erreur en lisant fichier input", ioe);
        }
        setupData();
        return this;
    }

    public abstract void setupData();

    public Object rule1() {
        throw new UnsupportedOperationException();
    }

    public Object rule2() {
        throw new UnsupportedOperationException();
    }


}
