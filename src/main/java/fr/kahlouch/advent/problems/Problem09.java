package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;


public class Problem09 extends Problem {
    public static void main(String[] args) {
        Problem.solve(Problem09.class);
    }

    private List<Sequence> sequences;

    @Override
    public void setupData() {
        this.sequences = this.lines.stream()
                .map(Sequence::parse)
                .toList();
    }

    @Override
    public Object rule1() {
        return this.sequences.parallelStream()
                .map(Sequence::computeNext)
                .mapToLong(Nombre::value)
                .sum();
    }


    @Override
    public Object rule2() {
        return this.sequences.parallelStream()
                .map(Sequence::computePrevious)
                .mapToLong(Nombre::value)
                .sum();
    }

    private record Sequence(List<Nombre> nombres) {
        public Sequence derive() {
            final var subList = new ArrayList<Nombre>();
            for (var i = 0; i < this.nombres.size() - 1; ++i) {
                subList.add(nombres.get(i).diff(nombres.get(i + 1)));
            }
            return new Sequence(subList);
        }

        public Nombre computeNext() {
            final var subSequence = new ArrayList<Sequence>();
            subSequence.add(this);
            var sequence = this;
            while (sequence.areNotAllEquals()) {
                sequence = sequence.derive();
                subSequence.add(sequence);
            }
            var toApply = new Nombre(0);
            for (int i = subSequence.size() - 1; i >= 0; --i) {
                toApply = subSequence.get(i).nombres.getLast().add(toApply);
            }
            return toApply;
        }

        public Nombre computePrevious() {
            final var subSequence = new ArrayList<Sequence>();
            subSequence.add(this);
            var sequence = this;
            while (sequence.areNotAllEquals()) {
                sequence = sequence.derive();
                subSequence.add(sequence);
            }
            var toApply = new Nombre(0);
            for (int i = subSequence.size() - 1; i >= 0; --i) {
                toApply = subSequence.get(i).nombres.getFirst().substract(toApply);
            }
            return toApply;
        }

        private boolean areNotAllEquals() {
            return this.nombres.stream().distinct().count() > 1;
        }

        public static Sequence parse(String input) {
            final var nombres = Arrays.stream(input.split(" "))
                    .map(s -> s.strip().trim())
                    .filter(Predicate.not(String::isEmpty))
                    .map(Long::parseLong)
                    .map(Nombre::new)
                    .toList();
            return new Sequence(nombres);
        }
    }

    private record Nombre(long value) {
        public Nombre diff(Nombre nextNumber) {
            Objects.requireNonNull(nextNumber);
            return new Nombre(nextNumber.value - this.value);
        }

        public Nombre add(Nombre nombre) {
            Objects.requireNonNull(nombre);
            return new Nombre(this.value + nombre.value);
        }

        public Nombre substract(Nombre nombre) {
            Objects.requireNonNull(nombre);
            return new Nombre(this.value - nombre.value);
        }
    }
}
