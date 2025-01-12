package fr.kahlouch.coding._common.string;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    public static Stream<String> charStream(String str) {
        return str.chars().mapToObj(i -> "" + (char) i);
    }


    public interface ReplacementResult {
    }

    public record ReplacedString(String newString, int foundIndex) implements ReplacementResult {
    }

    public enum NotFound implements ReplacementResult {INSTANCE}


    public static ReplacementResult replaceFirstFrom(String string, String from, String to, int startIndex) {
        Objects.requireNonNull(string);
        final var index = string.indexOf(from, startIndex);
        if (index == -1) {
            return NotFound.INSTANCE;
        }


        final var left = string.substring(0, index);
        final var right = string.substring(index + from.length());


        return new ReplacedString(left + to + right, index);
    }
}
