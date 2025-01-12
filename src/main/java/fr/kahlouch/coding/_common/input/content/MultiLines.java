package fr.kahlouch.coding._common.input.content;

import fr.kahlouch.coding._common.input.parse.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class MultiLines {
    private final List<Line> lines;

    public MultiLines(List<Line> lines) {
        this.lines = lines;
    }

    public Stream<String> lines() {
        return this.lines.stream().map(Line::content);
    }

    public <T> Stream<T> lines(Parser<T> parser) {
        final var idx = new AtomicLong(0);
        return lines().map(str -> parser.parse(str, idx.getAndIncrement()));
    }

    public List<MultiLines> splitOnEmptyLine() {
        final List<MultiLines> multiLines = new ArrayList<>();
        List<Line> lines = new ArrayList<>();

        for (var line : this.lines) {
            if (line.content().isEmpty()) {
                multiLines.add(new MultiLines(lines));
                lines = new ArrayList<>();
                continue;
            }
            lines.add(line);
        }

        multiLines.add(new MultiLines(lines));
        return multiLines;
    }
}
