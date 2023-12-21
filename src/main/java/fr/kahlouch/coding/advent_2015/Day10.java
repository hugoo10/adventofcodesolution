package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.string.StringUtils;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

class Day10 extends AdventProblem {
    public static void main(String[] args) {
        new Day10();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        var tmp = Input.of(inputPath).line().content();
        for (var i = 0; i < 40; ++i) {
            tmp = StringUtils.charStream(tmp)
                    .collect(NumberAccCollector.INSTANCE)
                    .stream().map(NumberAcc::toString)
                    .collect(Collectors.joining());
        }
        return tmp.length();
    }

    @Override
    protected Object resolve2(Path inputPath) {
        var tmp = Input.of(inputPath).line().content();
        for (var i = 0; i < 50; ++i) {
            tmp = StringUtils.charStream(tmp)
                    .collect(NumberAccCollector.INSTANCE)
                    .stream().map(NumberAcc::toString)
                    .collect(Collectors.joining());
        }
        return tmp.length();
    }

    private record NumberAcc(String number, AtomicInteger occurence) {
        @Override
        public String toString() {
            return occurence + number;
        }
    }

    private enum NumberAccCollector implements Collector<String, List<NumberAcc>, List<NumberAcc>> {
        INSTANCE;

        @Override
        public Supplier<List<NumberAcc>> supplier() {
            return ArrayList::new;
        }

        @Override
        public BiConsumer<List<NumberAcc>, String> accumulator() {
            return (list, str) -> {
                final var last = list.isEmpty() ? null : list.getLast();
                if (last != null && last.number.equals(str)) {
                    last.occurence.incrementAndGet();
                } else {
                    list.add(new NumberAcc(str, new AtomicInteger(1)));
                }
            };
        }

        @Override
        public BinaryOperator<List<NumberAcc>> combiner() {
            return null;
        }

        @Override
        public Function<List<NumberAcc>, List<NumberAcc>> finisher() {
            return Function.identity();
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Set.of(Characteristics.IDENTITY_FINISH);
        }
    }
}
