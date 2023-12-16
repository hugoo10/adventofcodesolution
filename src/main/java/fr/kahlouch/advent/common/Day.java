package fr.kahlouch.advent.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Stream;

public abstract class Day {
    private static final Logger log;

    static {
        final var stream = Day.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
            log = Logger.getLogger("Day");
        } catch (IOException e) {
            throw new RuntimeException("Exception à l'initialisation du logger", e);
        }
    }

    private static final Path RESOURCES_FOLDER = Path.of("").toAbsolutePath().resolve(Path.of("src", "main", "resources"));
    private final String year;
    private final String day;
    private Path testFolder;
    private Path inputFile;

    public Day() {
        final var packageName = this.getClass().getPackageName();
        this.year = Arrays.stream(packageName.split("\\.")).toList().getLast();
        this.day = this.getClass().getSimpleName().toLowerCase();
        createInputFilesIfNotPresent();
        resolveAll();
    }

    private void createInputFilesIfNotPresent() {
        try {
            final var yearFolder = RESOURCES_FOLDER.resolve(this.year);
            if (Files.notExists(yearFolder)) {
                Files.createDirectory(yearFolder);
            }
            final var dayFolder = yearFolder.resolve(this.day);
            if (Files.notExists(dayFolder)) {
                Files.createDirectory(dayFolder);
            }
            this.testFolder = dayFolder.resolve("tests");
            if (Files.notExists(testFolder)) {
                Files.createDirectory(testFolder);
            }
            this.inputFile = dayFolder.resolve("input.txt");
            if (Files.notExists(inputFile)) {
                Files.createFile(inputFile);
            }
            final var testFile = testFolder.resolve("test1.txt");
            if (Files.notExists(testFile)) {
                Files.createFile(testFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void resolveAll() {
        try (final var testFiles = Files.list(this.testFolder)) {
            testFiles.forEach(path -> {
                final var testFileName = path.getFileName().toString().replace(".txt", "").toUpperCase();
                final var testName = testFileName + ":";
                log.info("SOLUTION %-15s".formatted(testName) + doResolve(path));
            });
            log.info("SOLUTION:%-15s".formatted("") + doResolve(this.inputFile));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object doResolve(Path path) {
        try {
            if (Files.size(path) <= 0) {
                return "FICHIER VIDE";
            }
            return resolve(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Object resolve(Path inputPath);

    protected Stream<String> lineStream(Path inputPath) {
        try {
            return Files.lines(inputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Stream<Character> charStream(Path inputPath) {
        return lineStream(inputPath)
                .flatMap(line -> line.chars()
                        .mapToObj(i -> (char) i)
                );
    }


}
