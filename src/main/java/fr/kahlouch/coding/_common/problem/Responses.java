package fr.kahlouch.coding._common.problem;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public final class Responses {
    private List<Object> responses;

    private Responses(List<Object> responses) {
        this.responses = responses;
    }

    public static Responses of(Object... responses) {
        return new Responses(Arrays.asList(responses));
    }

    @Override
    public String toString() {
        final var idx = new AtomicInteger(1);
        return this.responses.stream()
                .map(response -> "PART" + idx.getAndIncrement() + ": " + response)
                .collect(Collectors.joining(" | "));
    }
}
