package fr.kahlouch.advent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Problem {
    protected List<String> input;

    public Problem init(String path) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File testInput = new File(classLoader.getResource(path).getFile());
        Scanner sc = new Scanner(testInput);
        this.input = new ArrayList<>();
        while (sc.hasNextLine()) {
            input.add(sc.nextLine());
        }
        setupData();
        return this;
    }

    public void setupData() {
    }

    public abstract Object rule1();

    public abstract Object rule2();
}
