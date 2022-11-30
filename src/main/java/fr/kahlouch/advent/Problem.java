package fr.kahlouch.advent;

import fr.kahlouch.advent.exception.GenericException;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public abstract class Problem {
    protected List<String> lines;

    public Problem init(String path) {
        final var classLoader = getClass().getClassLoader();
        Optional.ofNullable(classLoader.getResource(path))
                .map(URL::getFile)
                .map(File::new)
                .ifPresent(testInput -> {
                    try (final var sc = new Scanner(testInput)) {
                        this.lines = new ArrayList<>();
                        while (sc.hasNextLine()) {
                            lines.add(sc.nextLine());
                        }
                        setupData();
                    } catch (FileNotFoundException fnfe) {
                        throw new GenericException("Erreur en lisant fichier input", fnfe);
                    }
                });
        return this;
    }

    public void setupData() {
    }

    public abstract Object rule1();

    public abstract Object rule2();
}
