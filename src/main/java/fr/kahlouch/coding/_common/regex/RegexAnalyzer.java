package fr.kahlouch.coding._common.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexAnalyzer {
    private final Matcher matcher;

    public RegexAnalyzer(String input, Pattern pattern) {
        this.matcher = pattern.matcher(input);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Input does not match regex: '" + input + "'");
        }
    }

    public String group(int i) {
        return matcher.group(i);
    }
}
