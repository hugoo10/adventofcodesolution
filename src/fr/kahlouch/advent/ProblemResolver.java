package fr.kahlouch.advent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;

public class ProblemResolver {
    private String testFile;
    private Function<List<String>, String>[] resolvers;

    public ProblemResolver(String testFile, Function<List<String>, String>... resolvers) {
        this.testFile = testFile;
        this.resolvers = resolvers;
    }

    public void resolve() {
        Arrays.stream(this.resolvers).forEach(resolver -> {
            try {
                doTest(resolver);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public void doTest(Function<List<String>, String> resolve) throws FileNotFoundException {

        ClassLoader classLoader = getClass().getClassLoader();
        File testInput = new File(classLoader.getResource(this.testFile).getFile());
        Scanner sc = new Scanner(testInput);
        List<String> input = new ArrayList<>();
        while (sc.hasNextLine()) {
            input.add(sc.nextLine());
        }
        String mySolution = resolve.apply(input);
        System.out.println(mySolution);
    }
}
