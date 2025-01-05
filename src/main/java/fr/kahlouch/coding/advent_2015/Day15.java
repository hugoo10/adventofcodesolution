package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.optimization.brut_force.Composition;
import fr.kahlouch.coding._common.optimization.brut_force.MemoryHungryCompositionGenerator;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;

class Day15 extends AdventProblem {
    public static void main(String[] args) {
        new Day15();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var ingredients = Input.of(inputPath)
                .multiLines()
                .lines(IngredientParser.INSTANCE)
                .toList();
        return new MemoryHungryCompositionGenerator<Ingredient>().generateAllCompositions(ingredients, 100)
                .parallelStream()
                .map(Ingredient::computeCompositionValue)
                .max(Long::compareTo)
                .orElseThrow();

    }


    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var ingredients = Input.of(inputPath)
                .multiLines()
                .lines(IngredientParser.INSTANCE)
                .toList();
        return new MemoryHungryCompositionGenerator<Ingredient>().generateAllCompositions(ingredients, 100)
                .parallelStream()
                .filter(composition -> {
                    final var calories = Ingredient.computeCompositionCalories(composition);
                    return calories == 500;
                })
                .map(Ingredient::computeCompositionValue)
                .max(Long::compareTo)
                .orElseThrow();
    }


    private enum IngredientParser implements Parser<Ingredient> {
        INSTANCE;
        private static final Regex INGREDIENT_REGEX = new Regex("^(\\S+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)$");

        @Override
        public Ingredient parse(String input, long idx) {
            final var analyzer = INGREDIENT_REGEX.match(input);
            String name = analyzer.group(1);
            long capacity = analyzer.groupLong(2);
            long durability = analyzer.groupLong(3);
            long flavor = analyzer.groupLong(4);
            long texture = analyzer.groupLong(5);
            long calories = analyzer.groupLong(6);
            return new Ingredient(name, capacity, durability, flavor, texture, calories);
        }
    }

    private record Ingredient(String name, long capacity, long durability, long flavor, long texture, long calories) {
        public static long computeCompositionValue(Composition<Ingredient> composition) {
            var capacity = 0L;
            var durability = 0L;
            var flavor = 0L;
            var texture = 0L;

            for (final var entry : composition.value().entrySet()) {
                capacity += (entry.getValue() * entry.getKey().capacity());
                durability += (entry.getValue() * entry.getKey().durability());
                flavor += (entry.getValue() * entry.getKey().flavor());
                texture += (entry.getValue() * entry.getKey().texture());
            }

            return Math.max(0, capacity) * Math.max(0, durability) * Math.max(0, flavor) * Math.max(0, texture);
        }

        public static long computeCompositionCalories(Composition<Ingredient> composition) {
            return composition.value().entrySet()
                    .stream()
                    .mapToLong(entry -> entry.getValue() * entry.getKey().calories())
                    .sum();
        }
    }
}
