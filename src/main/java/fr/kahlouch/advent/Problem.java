package fr.kahlouch.advent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Problem {
    protected List<String> lines;

    public Problem init(String path) throws FileNotFoundException {
        final var classLoader = getClass().getClassLoader();
        final var testInput = new File(classLoader.getResource(path).getFile());
        try(final var sc = new Scanner(testInput)) {
            this.lines = new ArrayList<>();
            while (sc.hasNextLine()) {
                lines.add(sc.nextLine());
            }
            setupData();
        }
        return this;
    }

    public void setupData() {
    }

    public abstract Object rule1();

    public abstract Object rule2();
}
