package fr.kahlouch.coding._common.regex;

import java.util.regex.Pattern;

public final class Regex {
    private final Pattern pattern;

    public Regex(String patternStr) {
        this.pattern = Pattern.compile(patternStr);
    }

    public RegexAnalyzer analyze(String input) {
        return new RegexAnalyzer(input, pattern);
    }


}
