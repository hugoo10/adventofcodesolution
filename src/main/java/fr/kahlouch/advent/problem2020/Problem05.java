package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class Problem05 extends Problem<Integer> {
    private static int[] initArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; ++i) {
            array[i] = i;
        }
        return array;
    }

    private static int[] backOrRight(int[] rowOrColumn) {
        return Arrays.copyOfRange(rowOrColumn, rowOrColumn.length / 2, rowOrColumn.length);
    }

    private static int[] frontOrLeft(int[] rowOrColumn) {
        return Arrays.copyOfRange(rowOrColumn, 0, rowOrColumn.length / 2);
    }

    private static Map<Character, Function<int[], int[]>> getActionMap() {
        return Map.ofEntries(
                Map.entry('F', Problem05::frontOrLeft),
                Map.entry('L', Problem05::frontOrLeft),
                Map.entry('B', Problem05::backOrRight),
                Map.entry('R', Problem05::backOrRight)
        );
    }

    @Override
    public Integer rule1() {
        Map<Character, Function<int[], int[]>> actionMap = getActionMap();
        int max = 0;
        for (String line : input) {
            int[] rows = initArray(128);
            int[] columns = initArray(8);
            for (int i = 0; i < 7; ++i) {
                rows = actionMap.get(line.charAt(i)).apply(rows);
            }
            for (int i = 7; i < 10; ++i) {
                columns = actionMap.get(line.charAt(i)).apply(columns);
            }
            int result = rows[0] * 8 + columns[0];
            if (max < result) {
                max = result;
            }
        }
        return max;
    }

    @Override
    public Integer rule2() {
        Map<Character, Function<int[], int[]>> actionMap = getActionMap();
        List<Integer> ids = new ArrayList<>();
        for (String line : input) {
            int[] rows = initArray(128);
            int[] columns = initArray(8);
            for (int i = 0; i < 7; ++i) {
                rows = actionMap.get(line.charAt(i)).apply(rows);
            }
            for (int i = 7; i < 10; ++i) {
                columns = actionMap.get(line.charAt(i)).apply(columns);
            }
            ids.add(rows[0] * 8 + columns[0]);
        }

        ids.sort(Integer::compareTo);
        for(int i=0; i<ids.size()-1; ++i) {
            if(ids.get(i+1) != ids.get(i) + 1) {
                return (ids.get(i) + 1);
            }
        }
        return null;
    }
}
