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


    public static <P extends Problem> void solve(Class<P> clazz) {
        try {
            final var testFile = clazz.getSimpleName().toLowerCase() + "_part%s_test.txt";
            final var realFile = clazz.getSimpleName().toLowerCase() + "_part%s.txt";
            final Constructor<P> constructor = clazz.getConstructor();
            log.info("Solution TEST 1: " + constructor.newInstance().init(testFile.formatted("1")).rule1());
            log.info("Solution TEST 2: " + constructor.newInstance().init(testFile.formatted("2")).rule2());
            log.info("Solution 1: " + constructor.newInstance().init(realFile.formatted("1")).rule1());
            log.info("Solution 2: " + constructor.newInstance().init(realFile.formatted("2")).rule2());
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

    public abstract Object rule1();

    public abstract Object rule2();


}
