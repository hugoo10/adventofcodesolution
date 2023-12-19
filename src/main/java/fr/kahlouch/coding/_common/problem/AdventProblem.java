package fr.kahlouch.coding._common.problem;

import java.nio.file.Path;

public abstract class AdventProblem extends Problem {
    protected AdventProblem(int nbTests) {
        super(nbTests);
    }

    protected AdventProblem() {
        super(1);
    }

    @Override
    protected Object resolve(Path inputPath) {
        return Responses.of(
                resolve1(inputPath),
                resolve2(inputPath)
        );
    }

    protected abstract Object resolve1(Path inputPath);

    protected abstract Object resolve2(Path inputPath);
}
