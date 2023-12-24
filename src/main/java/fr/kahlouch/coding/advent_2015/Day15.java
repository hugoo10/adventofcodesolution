package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;
import java.util.*;

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
        final var test = buildAllPossibilitiesComplementary(ingredients, new HashMap<>(), 100);
        return null;
    }

    private <T> List<Map<T, Long>> buildAllPossibilitiesComplementary(List<T> ingredients, Map<T, Long> map, long total) {
        final var list = new LinkedList<>(ingredients);
        final var next = list.poll();
        if (list.isEmpty()) {
            final var newMap = new HashMap<>(map);
            newMap.put(next, total);
            return List.of(newMap);
        }
        final var result = new ArrayList<Map<T, Long>>();
        for (long i = 0; i <= total; ++i) {
            final var newMap = new HashMap<>(map);
            newMap.put(next, i);
            result.addAll(buildAllPossibilitiesComplementary(list, newMap, total - i));
        }
        return result;
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        return null;
    }


    private enum IngredientParser implements Parser<Ingredient> {
        INSTANCE;
        private static final Regex INGREDIENT_REGEX = new Regex("^(\\S+): capacity (-?\\d+), durability (-?\\d+), flavor (-?\\d+), texture (-?\\d+), calories (-?\\d+)$");

        @Override
        public Ingredient parse(String input, long idx) {
            final var analyzer = INGREDIENT_REGEX.analyze(input);
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
    }

    private static class IngredientWithQuantity {
        private final Ingredient ingredient;
        private final long quantity;
        private final long capacity;
        private final long durability;
        private final long flavor;
        private final long texture;
        private final long calories;

        public IngredientWithQuantity(Ingredient ingredient, long quantity) {
            this.ingredient = ingredient;
            this.quantity = quantity;
            this.capacity = quantity * ingredient.capacity();
            this.durability = quantity * ingredient.durability();
            this.flavor = quantity * ingredient.flavor();
            this.texture = quantity * ingredient.texture();
            this.calories = quantity * ingredient.calories();
        }
    }
}
