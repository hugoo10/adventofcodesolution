package fr.kahlouch.coding._common.input;

import fr.kahlouch.coding._common.input.content.Line;
import fr.kahlouch.coding._common.input.content.MultiLines;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Input {
    private final Path path;

    private Input(Path path) {
        this.path = path;
    }

    public static Input of(Path path) {
        return new Input(path);
    }

    public Line line() {
        try (final var lines = Files.lines(this.path)) {
            return lines.map(Line::new).findFirst().get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public MultiLines multiLines() {
        try (final var lines = Files.lines(this.path)) {
            final var lineList = lines.map(Line::new).toList();
            return new MultiLines(lineList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
