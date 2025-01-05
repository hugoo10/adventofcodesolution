package fr.kahlouch.coding._common.regex;

import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class Regex {
    private final Pattern pattern;

    public Regex(String patternStr) {
        this.pattern = Pattern.compile(patternStr);
    }

    public RegexMatcher match(String input) {
        return new RegexMatcher(input, pattern);
    }

    public RegexFinder find(String input) {
        return new RegexFinder(input, pattern);
    }

    public Predicate<String> toPredicate() {
        return str -> pattern.matcher(str).matches();
    }

}
