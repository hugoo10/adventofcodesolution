package fr.kahlouch.coding._common.regex;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexFinder {
    private final Matcher matcher;

    public RegexFinder(String input, Pattern pattern) {
        this.matcher = pattern.matcher(input);
    }

    public Optional<String> group(int i) {
        if (matcher.find()) {
            return Optional.ofNullable(matcher.group(i));
        }
        return Optional.empty();
    }

    public Optional<Double> groupDouble(int i) {
        return group(i)
                .map(Double::parseDouble);
    }

    public Optional<Long> groupLong(int i) {
        return group(i)
                .map(Long::parseLong);
    }
}
