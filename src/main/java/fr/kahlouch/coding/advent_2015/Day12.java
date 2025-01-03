package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.problem.AdventProblem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

class Day12 extends AdventProblem {
    Day12() {
        super(11);
    }

    public static void main(String[] args) {
        new Day12();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        final var numberPattern = Pattern.compile("(-?\\d+)");
        final var content = Input.of(inputPath).line().content();
        final var matcher = numberPattern.matcher(content);
        if (matcher.find()) {
            final var group = Long.parseLong(matcher.group());
            return matcher.results()
                    .map(matchResult -> content.substring(matchResult.start(), matchResult.end()))
                    .mapToLong(Long::parseLong)
                    .sum() + group;
        }
        return 0;
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var content = Input.of(inputPath).line().content();
        final var json = new JSONObject("{\"content\":"+content+"}");
        return extractNumbers(json).stream().mapToLong(i -> i).sum();
    }

    private static List<Long> extractNumbers(JSONObject object) {
        final var list = new ArrayList<Long>();
        for (var key : object.keySet()) {
            final var value = object.get(key);
            try {
                list.addAll(extractNumbers(value, true));
            } catch (RedException re) {
                return List.of();
            }
        }
        return list;
    }

    private static List<Long> extractNumbers(JSONArray array) {
        final var list = new ArrayList<Long>();
        for (var value : array.toList()) {
            try {
                list.addAll(extractNumbers(value, false));
            } catch (RedException re) {
                throw new RuntimeException("unexpected", re);
            }
        }
        return list;
    }

    private static List<Long> extractNumbers(Object value, boolean fromObject) throws RedException {
        return switch (value) {
            case JSONArray array -> extractNumbers(array);
            case JSONObject object -> extractNumbers(object);
            case String str -> {
                if (fromObject && "red".equals(str)) {
                    throw new RedException();
                }
                yield List.of();
            }
            case ArrayList<?> list -> extractNumbers(new JSONArray(list));
            case HashMap<?, ?> map -> extractNumbers(new JSONObject(map));
            case Integer integer -> List.of((long) integer);
            default -> List.of();
        };
    }

    private static class RedException extends Exception {

    }
}
