package fr.kahlouch.coding._common.string;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    public static Stream<String> charStream(String str) {
        return str.chars().mapToObj(i -> "" + (char) i);
    }
}
