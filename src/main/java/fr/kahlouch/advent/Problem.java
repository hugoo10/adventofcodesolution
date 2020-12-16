package fr.kahlouch.advent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class Problem<RETURN_TYPE> {
    protected List<String> input;

    public Problem<RETURN_TYPE> init(String path) throws FileNotFoundException {
        ClassLoader classLoader = getClass().getClassLoader();
        File testInput = new File(classLoader.getResource(path).getFile());
        Scanner sc = new Scanner(testInput);
        this.input = new ArrayList<>();
        while (sc.hasNextLine()) {
            input.add(sc.nextLine());
        }
        return this;
    }

    public abstract RETURN_TYPE rule1();

    public abstract RETURN_TYPE rule2();
}
