package fr.kahlouch.coding._common.stream;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Streams {
    public static Stream<Integer> iterate(int from, int toIncluded) {
        return Stream.iterate(from, i -> i <= toIncluded, i -> i + 1);
    }
}
