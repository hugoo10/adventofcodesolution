package fr.kahlouch.advent;

import fr.kahlouch.advent.exception.GenericException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Problem {
    private final static Path RESOURCES_FOLDER = Path.of("").toAbsolutePath().resolve(Path.of("src", "main", "resources"));
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

    public void setupData() {
    }

    public abstract Object rule1();

    public abstract Object rule2();
}
