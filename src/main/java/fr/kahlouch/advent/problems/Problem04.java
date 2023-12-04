package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class Problem04 extends Problem {
    private Map<CardId, ScratchCard> scratchCards;

    public static void main(String[] args) {
        Problem.solve(Problem04.class);
    }

    @Override
    public void setupData() {
        this.scratchCards = this.lines.stream()
                .map(ScratchCard::parse)
                .collect(Collectors.toMap(ScratchCard::cardId, Function.identity()));
    }

    @Override
    public Object rule1() {
        return this.scratchCards.values().stream()
                .map(ScratchCard::matchingNumbers)
                .mapToLong(Set::size)
                .filter(i -> i > 0)
                .map(i -> Math.round(Math.pow(2, i - 1)))
                .sum();
    }

    @Override
    public Object rule2() {
        final var processed = new TreeMap<CardId, Integer>();
        final var toProcess = new TreeMap<CardId, Integer>();
        this.scratchCards.keySet().forEach(cardId -> toProcess.put(cardId, 1));
        for (final var cardId : this.scratchCards.keySet()) {
            process(this.scratchCards.get(cardId), toProcess, processed);
        }
        return processed.values()
                .stream()
                .mapToLong(Long::valueOf)
                .sum();
    }

    static void process(ScratchCard scratchCard, Map<CardId, Integer> toProcess, Map<CardId, Integer> processed) {
        final var nbCard = toProcess.remove(scratchCard.cardId);
        processed.put(scratchCard.cardId, nbCard);
        final var nextToScratch = scratchCard.nextCardToScratch();
        nextToScratch.forEach(cardId ->
                toProcess.computeIfPresent(cardId, (id, v) -> v + nbCard)
        );
    }


    record ScratchCard(CardId cardId, WinningNumbers winningNumbers, ScratchedNumbers scratchedNumbers) {
        static ScratchCard parse(String input) {
            final var splitId = input.split(":");
            final var cardId = CardId.parse(splitId[0]);
            final var splitNumbers = splitId[1].split("\\|");
            final var winningNumbers = WinningNumbers.parse(splitNumbers[0]);
            final var scratchedNumbers = ScratchedNumbers.parse(splitNumbers[1]);
            return new ScratchCard(cardId, winningNumbers, scratchedNumbers);
        }

        public Set<WinningNumber> matchingNumbers() {
            return this.winningNumbers.winningNumbers
                    .stream()
                    .filter(w -> w.isWon(this.scratchedNumbers.scratchedNumbers))
                    .collect(Collectors.toSet());
        }

        public SortedSet<CardId> nextCardToScratch() {
            final var nbWinning = matchingNumbers().size();
            final var cardIdsToScratch = new TreeSet<CardId>();
            for (var i = 0; i < nbWinning; i++) {
                cardIdsToScratch.add(new CardId(this.cardId.id + i + 1));
            }
            return cardIdsToScratch;
        }
    }

    record WinningNumbers(SortedSet<WinningNumber> winningNumbers) {
        static WinningNumbers parse(String input) {
            final var winningNumbers = Arrays.stream(input.strip().trim().split("\\s"))
                    .filter(Predicate.not(String::isEmpty))
                    .map(Integer::parseInt)
                    .map(WinningNumber::new)
                    .toList();
            return new WinningNumbers(new TreeSet<>(winningNumbers));
        }
    }

    record WinningNumber(int value) implements Comparable<WinningNumber> {
        @Override
        public int compareTo(WinningNumber o) {
            return this.value - o.value;
        }

        public boolean isWon(SortedSet<ScratchedNumber> scratchedNumbers) {
            if (scratchedNumbers.getFirst().value > this.value) {
                return false;
            }
            if (scratchedNumbers.getLast().value < this.value) {
                return false;
            }
            for (final var scratchedNumber : scratchedNumbers) {
                if (scratchedNumber.value == this.value) {
                    return true;
                } else if (scratchedNumber.value > this.value) {
                    return false;
                }
            }
            return false;
        }
    }

    record ScratchedNumbers(SortedSet<ScratchedNumber> scratchedNumbers) {
        static ScratchedNumbers parse(String input) {
            final var scratchedNumbers = Arrays.stream(input.strip().trim().split("\\s"))
                    .filter(Predicate.not(String::isEmpty))
                    .map(Integer::parseInt)
                    .map(ScratchedNumber::new)
                    .toList();
            return new ScratchedNumbers(new TreeSet<>(scratchedNumbers));
        }
    }

    record ScratchedNumber(int value) implements Comparable<ScratchedNumber> {
        @Override
        public int compareTo(ScratchedNumber o) {
            return this.value - o.value;
        }
    }

    record CardId(int id) implements Comparable<CardId> {
        private static final Pattern CARD_PATTERN = Pattern.compile("Card\\s");

        static CardId parse(String input) {
            final var cleaned = CARD_PATTERN.matcher(input)
                    .replaceAll("")
                    .trim()
                    .strip();
            return new CardId(Integer.parseInt(cleaned));
        }

        @Override
        public int compareTo(CardId o) {
            return this.id - o.id;
        }
    }
}
