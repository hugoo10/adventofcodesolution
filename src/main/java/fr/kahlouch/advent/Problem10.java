package fr.kahlouch.advent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class Problem10 extends Problem {
    @Override
    public void setupData() {

    }

    @Override
    public Object rule1() {
        long score = 0;
        List<String> corrupted = new ArrayList<>();
        BIG:
        for (var str : input) {
            Stack<Character> stack = new Stack<>();
            for (var c : str.toCharArray()) {
                if (isOpening(c)) {
                    stack.push(c);
                } else {
                    if (stack.peek() == getComplement(c)) {
                        stack.pop();
                    } else {
                        score += points(c);
                        corrupted.add(str);
                        continue BIG;
                    }
                }
            }
        }
        return score;
    }

    @Override
    public Object rule2() {
        List<Long> scores = new ArrayList<>();
        BIG:
        for (var str : input) {
            Stack<Character> stack = new Stack<>();
            for (var c : str.toCharArray()) {
                if (isOpening(c)) {
                    stack.push(c);
                } else {
                    if (stack.peek() == getComplement(c)) {
                        stack.pop();
                    } else {
                        continue BIG;
                    }
                }
            }
            var score = 0L;
            var size = stack.size();
            for (var i = 0; i < size; ++i) {
                score *= 5;
                score += points2(stack.pop());
            }
            scores.add(score);
        }
        Collections.sort(scores);
        var idx = Math.round(scores.size() / 2F) - 1;
        return scores.get(idx);
    }

    private char getComplement(char c) {
        return switch (c) {
            case '(' -> ')';
            case '{' -> '}';
            case '[' -> ']';
            case '<' -> '>';
            case '>' -> '<';
            case ']' -> '[';
            case '}' -> '{';
            case ')' -> '(';
            default -> throw new RuntimeException("Not Matching");
        };
    }

    private boolean isOpening(char c) {
        return switch (c) {
            case '(', '{', '[', '<' -> true;
            default -> false;
        };
    }

    private long points(char c) {
        return switch (c) {
            case '>' -> 25137;
            case ']' -> 57;
            case '}' -> 1197;
            case ')' -> 3;
            default -> throw new RuntimeException("Not Matching");
        };
    }

    private long points2(char c) {
        return switch (c) {
            case '(' -> 1;
            case '{' -> 3;
            case '[' -> 2;
            case '<' -> 4;
            default -> throw new RuntimeException("Not Matching");
        };
    }
}
