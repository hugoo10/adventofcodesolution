package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;

class Day16 extends AdventProblem {
    public Day16() {
        super(0);
    }

    public static void main(String[] args) {
        new Day16();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        return Input.of(inputPath)
                .multiLines()
                .lines(AuntSueParser.INSTANCE)
                .filter(auntSue ->
                        auntSue.children.canMatch(3) &&
                                auntSue.cats.canMatch(7) &&
                                auntSue.samoyeds.canMatch(2) &&
                                auntSue.pomeranians.canMatch(3) &&
                                auntSue.akitas.canMatch(0) &&
                                auntSue.vizslas.canMatch(0) &&
                                auntSue.goldfish.canMatch(5) &&
                                auntSue.trees.canMatch(3) &&
                                auntSue.cars.canMatch(2) &&
                                auntSue.perfumes.canMatch(1)
                )
                .findFirst()
                .orElseThrow()
                .aunt;
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        return Input.of(inputPath)
                .multiLines()
                .lines(AuntSueParser.INSTANCE)
                .filter(auntSue ->
                        auntSue.children.canMatchRealRules(3) &&
                                auntSue.cats.canMatchRealRules(7) &&
                                auntSue.samoyeds.canMatchRealRules(2) &&
                                auntSue.pomeranians.canMatchRealRules(3) &&
                                auntSue.akitas.canMatchRealRules(0) &&
                                auntSue.vizslas.canMatchRealRules(0) &&
                                auntSue.goldfish.canMatchRealRules(5) &&
                                auntSue.trees.canMatchRealRules(3) &&
                                auntSue.cars.canMatchRealRules(2) &&
                                auntSue.perfumes.canMatchRealRules(1)
                )
                .findFirst()
                .orElseThrow()
                .aunt;
    }

    private sealed interface Value permits NonRememberedValue, RememberedValue {
        boolean canMatch(long value);

        boolean canMatchRealRules(long value);
    }

    private record NonRememberedValue() implements Value {

        @Override
        public boolean canMatch(long value) {
            return true;
        }

        @Override
        public boolean canMatchRealRules(long value) {
            return true;
        }

        @Override
        public String toString() {
            return "N/A";
        }
    }

    private non-sealed static class RememberedValue implements Value {
        protected final long value;

        public RememberedValue(long value) {
            this.value = value;
        }

        @Override
        public boolean canMatch(long value) {
            return this.value == value;
        }

        @Override
        public boolean canMatchRealRules(long value) {
            return this.value == value;
        }

        @Override
        public String toString() {
            return "" + value;
        }
    }

    private static class GreaterThanValue extends RememberedValue {

        public GreaterThanValue(long value) {
            super(value);
        }

        @Override
        public boolean canMatchRealRules(long value) {
            return this.value > value;
        }
    }

    private static class FewerThanValue extends RememberedValue {

        public FewerThanValue(long value) {
            super(value);
        }

        @Override
        public boolean canMatchRealRules(long value) {
            return this.value < value;
        }
    }

    private enum AuntSueParser implements Parser<AuntSue> {
        INSTANCE;
        private static final Regex CHILDREN_REGEX = new Regex("children: (\\d+)");
        private static final Regex CATS_REGEX = new Regex("cats: (\\d+)");
        private static final Regex SAMOYED_REGEX = new Regex("samoyeds: (\\d+)");
        private static final Regex POMERANIANS_REGEX = new Regex("pomeranians: (\\d+)");
        private static final Regex AKITAS_REGEX = new Regex("akitas: (\\d+)");
        private static final Regex VIZSLAS_REGEX = new Regex("vizslas: (\\d+)");
        private static final Regex GOLDFISH_REGEX = new Regex("goldfish: (\\d+)");
        private static final Regex TREES_REGEX = new Regex("trees: (\\d+)");
        private static final Regex CARS_REGEX = new Regex("cars: (\\d+)");
        private static final Regex PERFUMES_REGEX = new Regex("perfumes: (\\d+)");

        @Override
        public AuntSue parse(String input, long idx) {
            final var children = CHILDREN_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());
            final var cats = CATS_REGEX.find(input).groupLong(1).<Value>map(GreaterThanValue::new).orElse(new NonRememberedValue());
            final var samoyeds = SAMOYED_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());
            final var pomeranians = POMERANIANS_REGEX.find(input).groupLong(1).<Value>map(FewerThanValue::new).orElse(new NonRememberedValue());
            final var akitas = AKITAS_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());
            final var vizslas = VIZSLAS_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());
            final var goldfish = GOLDFISH_REGEX.find(input).groupLong(1).<Value>map(FewerThanValue::new).orElse(new NonRememberedValue());
            final var trees = TREES_REGEX.find(input).groupLong(1).<Value>map(GreaterThanValue::new).orElse(new NonRememberedValue());
            final var cars = CARS_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());
            final var perfumes = PERFUMES_REGEX.find(input).groupLong(1).<Value>map(RememberedValue::new).orElse(new NonRememberedValue());

            return new AuntSue(idx + 1, children, cats, samoyeds, pomeranians, akitas, vizslas, goldfish, trees, cars, perfumes);
        }
    }

    private record AuntSue(long aunt, Value children, Value cats, Value samoyeds, Value pomeranians, Value akitas,
                           Value vizslas,
                           Value goldfish, Value trees, Value cars, Value perfumes) {
    }
}
