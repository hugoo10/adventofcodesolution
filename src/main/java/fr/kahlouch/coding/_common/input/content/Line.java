package fr.kahlouch.coding._common.input.content;

import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.string.StringUtils;

import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public record Line(String content) {

    public Stream<String> chars() {
        return StringUtils.charStream(this.content);
    }

    public <T> Stream<T> chars(Parser<T> parser) {
        final var idx = new AtomicLong(0);
        return chars().map(input -> parser.parse(input, idx.getAndIncrement()));
    }
}
