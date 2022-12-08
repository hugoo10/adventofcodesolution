package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class Problem08 extends Problem {
    Integer[][] forest;

    @Override
    public void setupData() {
        forest = new Integer[lines.get(0).length()][lines.size()];
        for (var y = 0; y < lines.size(); ++y) {
            var splited = lines.get(y).split("");
            for (var x = 0; x < lines.get(y).length(); ++x) {
                forest[x][y] = Integer.parseInt(splited[x]);
            }
        }
    }

    boolean checkLeft(int x, int y) {
        var viewable = true;
        for (var i = x - 1; i >= 0; --i) {
            if (this.forest[i][y] >= this.forest[x][y]) {
                viewable = false;
                break;
            }
        }
        return viewable;
    }

    boolean checkRight(int x, int y) {
        var viewable = true;
        for (var i = x + 1; i < this.forest.length; ++i) {
            if (this.forest[i][y] >= this.forest[x][y]) {
                viewable = false;
                break;
            }
        }
        return viewable;
    }

    boolean checkUp(int x, int y) {
        var viewable = true;
        for (var j = y - 1; j >= 0; --j) {
            if (this.forest[x][j] >= this.forest[x][y]) {
                viewable = false;
                break;
            }
        }
        return viewable;
    }

    boolean checkDown(int x, int y) {
        var viewable = true;
        for (var j = y + 1; j < forest[x].length; ++j) {
            if (this.forest[x][j] >= this.forest[x][y]) {
                viewable = false;
                break;
            }
        }
        return viewable;
    }

    long scenicLeft(int x, int y) {
        var count = 0;
        for (var i = x - 1; i >= 0; --i) {
            count++;
            if (this.forest[i][y] >= this.forest[x][y]) {
                break;
            }
        }
        return count;
    }

    long scenicRight(int x, int y) {
        var count = 0;
        for (var i = x + 1; i < this.forest.length; ++i) {
            count++;
            if (this.forest[i][y] >= this.forest[x][y]) {
                break;
            }
        }
        return count;
    }

    long scenicUp(int x, int y) {
        var count = 0;
        for (var j = y - 1; j >= 0; --j) {
            count++;
            if (this.forest[x][j] >= this.forest[x][y]) {
                break;
            }
        }
        return count;
    }

    long scenicDown(int x, int y) {
        var count = 0;
        for (var j = y + 1; j < forest[x].length; ++j) {
            count++;
            if (this.forest[x][j] >= this.forest[x][y]) {
                break;
            }
        }
        return count;
    }

    int countViewableTrees() {
        return Stream.iterate(0, x -> x < forest.length, x -> x + 1)
                .toList()
                .parallelStream()
                .mapToInt(x ->
                        Stream.iterate(0, y -> y < forest[x].length, y -> y + 1)
                                .toList()
                                .parallelStream()
                                .map(y -> Stream.of(
                                                (Supplier<Boolean>) () -> checkDown(x, y),
                                                () -> checkUp(x, y),
                                                () -> checkLeft(x, y),
                                                () -> checkRight(x, y)
                                        ).map(Supplier::get).filter(b -> b)
                                        .findFirst()
                                        .orElse(false))
                                .mapToInt(b -> b ? 1 : 0)
                                .sum()
                ).sum();
    }

    long findHighestScenic() {
        return  Stream.iterate(0, x -> x < forest.length, x -> x + 1)
                .toList()
                .parallelStream()
                .flatMapToLong(x -> {
                            var res= Stream.iterate(0, y -> y < forest[x].length, y -> y + 1)
                                    .toList()
                                    .parallelStream()
                                    .mapToLong(y -> {
                                        var scenic = Stream.of(
                                                        (Supplier<Long>) () -> scenicDown(x, y),
                                                        () -> scenicUp(x, y),
                                                        () -> scenicRight(x, y),
                                                        () -> scenicLeft(x, y)
                                                ).parallel().map(Supplier::get).mapToLong(l-> l).
                                                reduce(1L, (l1, l2) -> l1 * l2);
                                        return scenic;
                                    });
                            return res;
                        }
                ).max().orElseThrow();
    }


    /*
    --- Day 8: Treetop Tree House ---
    The expedition comes across a peculiar patch of tall trees all planted carefully in a grid. The Elves explain that a previous expedition planted these trees as a reforestation effort. Now, they're curious if this would be a good location for a tree house.

    First, determine whether there is enough tree cover here to keep a tree house hidden. To do this, you need to count the number of trees that are visible from outside the grid when looking directly along a row or column.

    The Elves have already launched a quadcopter to generate a map with the height of each tree (your puzzle input). For example:

    30373
    25512
    65332
    33549
    35390
    Each tree is represented as a single digit whose value is its height, where 0 is the shortest and 9 is the tallest.

    A tree is visible if all of the other trees between it and an edge of the grid are shorter than it. Only consider trees in the same row or column; that is, only look up, down, left, or right from any given tree.

    All of the trees around the edge of the grid are visible - since they are already on the edge, there are no trees to block the view. In this example, that only leaves the interior nine trees to consider:

    The top-left 5 is visible from the left and top. (It isn't visible from the right or bottom since other trees of height 5 are in the way.)
    The top-middle 5 is visible from the top and right.
    The top-right 1 is not visible from any direction; for it to be visible, there would need to only be trees of height 0 between it and an edge.
    The left-middle 5 is visible, but only from the right.
    The center 3 is not visible from any direction; for it to be visible, there would need to be only trees of at most height 2 between it and an edge.
    The right-middle 3 is visible from the right.
    In the bottom row, the middle 5 is visible, but the 3 and 4 are not.
    With 16 trees visible on the edge and another 5 visible in the interior, a total of 21 trees are visible in this arrangement.

    Consider your map; how many trees are visible from outside the grid?
     */
    @Override
    public Object rule1() {
        return countViewableTrees();
    }

    /*
    --- Part Two ---
    Content with the amount of tree cover available, the Elves just need to know the best spot to build their tree house: they would like to be able to see a lot of trees.

    To measure the viewing distance from a given tree, look up, down, left, and right from that tree; stop if you reach an edge or at the first tree that is the same height or taller than the tree under consideration. (If a tree is right on the edge, at least one of its viewing distances will be zero.)

    The Elves don't care about distant trees taller than those found by the rules above; the proposed tree house has large eaves to keep it dry, so they wouldn't be able to see higher than the tree house anyway.

    In the example above, consider the middle 5 in the second row:

    30373
    25512
    65332
    33549
    35390
    Looking up, its view is not blocked; it can see 1 tree (of height 3).
    Looking left, its view is blocked immediately; it can see only 1 tree (of height 5, right next to it).
    Looking right, its view is not blocked; it can see 2 trees.
    Looking down, its view is blocked eventually; it can see 2 trees (one of height 3, then the tree of height 5 that blocks its view).
    A tree's scenic score is found by multiplying together its viewing distance in each of the four directions. For this tree, this is 4 (found by multiplying 1 * 1 * 2 * 2).

    However, you can do even better: consider the tree of height 5 in the middle of the fourth row:

    30373
    25512
    65332
    33549
    35390
    Looking up, its view is blocked at 2 trees (by another tree with a height of 5).
    Looking left, its view is not blocked; it can see 2 trees.
    Looking down, its view is also not blocked; it can see 1 tree.
    Looking right, its view is blocked at 2 trees (by a massive tree of height 9).
    This tree's scenic score is 8 (2 * 2 * 1 * 2); this is the ideal spot for the tree house.

    Consider each tree on your map. What is the highest scenic score possible for any tree?
     */
    @Override
    public Object rule2() {
        return findHighestScenic();
    }
}
