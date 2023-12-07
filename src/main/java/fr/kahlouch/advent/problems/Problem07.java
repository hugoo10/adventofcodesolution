package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.function.Function;


public class Problem07 extends Problem {
    public static void main(String[] args) {
        Problem.solve(Problem07.class);
    }

    private List<Hand> hands;
    private List<Hand> handsWithJoker;

    @Override
    public void setupData() {
        this.hands = this.lines.stream().map(Hand::parse).toList();
        this.handsWithJoker = this.lines.stream().map(Hand::parseWithJoker).toList();
    }

    @Override
    public Object rule1() {
        final var ordred = this.hands.stream().sorted().toList();
        var sum = 0L;
        for (var i = 0; i < ordred.size(); ++i) {
            final var multiplier = i + 1;
            final var bidValue = ordred.get(i).bid.value;
            sum += bidValue * multiplier;
        }
        return sum;
    }

    @Override
    public Object rule2() {
        final var comparator = new HandWithJokerComparator();
        final var ordred = this.handsWithJoker.stream().sorted(comparator).toList();
        var sum = 0L;
        for (var i = 0; i < ordred.size(); ++i) {
            final var multiplier = i + 1;
            final var bidValue = ordred.get(i).bid.value;
            sum += bidValue * multiplier;
        }
        return sum;
    }

    record Card(String figure) implements Comparable<Card> {
        private static final List<Card> cardOrder = List.of(
                new Card("2"),
                new Card("3"),
                new Card("4"),
                new Card("5"),
                new Card("6"),
                new Card("7"),
                new Card("8"),
                new Card("9"),
                new Card("T"),
                new Card("J"),
                new Card("Q"),
                new Card("K"),
                new Card("A")
        );


        @Override
        public int compareTo(Card o) {
            Objects.requireNonNull(o);
            return cardOrder.indexOf(this) - cardOrder.indexOf(o);
        }
    }

    record Hand(List<Card> cards, HandType handType, Bid bid) implements Comparable<Hand> {

        static Hand parse(String input) {
            return parse(input, HandType::determineType);
        }

        static Hand parseWithJoker(String input) {
            return parse(input, HandType::determineTypeWithJoker);
        }
        private static Hand parse(String input, Function<Map<Card, Integer>, HandType> handTypeDerterminer) {
            final var split = input.split(" ");
            final var bid = Bid.parse(split[1]);
            final var count = new HashMap<Card, Integer>();
            final var cardList = new ArrayList<Card>();
            split[0].chars().mapToObj(c -> "" + (char) c)
                    .map(Card::new)
                    .forEach(card -> {
                        count.putIfAbsent(card, 0);
                        count.computeIfPresent(card, (c, v) -> v + 1);
                        cardList.add(card);
                    });
            final var handType = handTypeDerterminer.apply(count);
            return new Hand(cardList, handType, bid);
        }

        @Override
        public int compareTo(Hand o) {
            Objects.requireNonNull(o);
            if (this.handType == o.handType) {
                for (var i = 0; i < cards.size(); ++i) {
                    final var compare = this.cards.get(i).compareTo(o.cards.get(i));
                    if (compare == 0) {
                        continue;
                    }
                    return compare;
                }
                return 0;
            }
            return this.handType.ordinal() - o.handType.ordinal();
        }
    }

    static class HandWithJokerComparator implements Comparator<Hand> {
        final CardJokerComparator cardComparator = new CardJokerComparator();

        @Override
        public int compare(Hand o1, Hand o2) {
            Objects.requireNonNull(o1);
            Objects.requireNonNull(o2);
            if (o1.handType == o2.handType) {
                for (var i = 0; i < o1.cards.size(); ++i) {
                    final var compare = cardComparator.compare(o1.cards.get(i), o2.cards.get(i));
                    if (compare == 0) {
                        continue;
                    }
                    return compare;
                }
                return 0;
            }
            return o1.handType.ordinal() - o2.handType.ordinal();
        }
    }

    static class CardJokerComparator implements Comparator<Card> {
        private static final List<Card> cardOrderWithJoker = List.of(
                new Card("J"),
                new Card("2"),
                new Card("3"),
                new Card("4"),
                new Card("5"),
                new Card("6"),
                new Card("7"),
                new Card("8"),
                new Card("9"),
                new Card("T"),
                new Card("Q"),
                new Card("K"),
                new Card("A")
        );

        @Override
        public int compare(Card o1, Card o2) {
            Objects.requireNonNull(o1);
            Objects.requireNonNull(o2);
            return cardOrderWithJoker.indexOf(o1) - cardOrderWithJoker.indexOf(o2);
        }
    }

    enum HandType {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND;

        static HandType determineType(Map<Card, Integer> count) {
            final var countList = new ArrayList<>(count.values());

            if (countList.contains(5)) {
                return FIVE_OF_A_KIND;
            }
            if (countList.contains(4)) {
                return FOUR_OF_A_KIND;
            }
            if (countList.contains(3)) {
                if (countList.contains(2)) {
                    return FULL_HOUSE;
                }
                return THREE_OF_A_KIND;
            }
            if (countList.contains(2)) {
                if (countList.stream().filter(v -> v.equals(2)).count() == 2) {
                    return TWO_PAIR;
                }
                return ONE_PAIR;
            }
            return HIGH_CARD;
        }

        static HandType determineTypeWithJoker(Map<Card, Integer> count) {
            final var joker = new Card("J");
            final var countList = new ArrayList<>(count.entrySet().stream().filter(e -> !e.getKey().equals(joker)).map(Map.Entry::getValue).toList());
            final var jCount = Optional.ofNullable(count.get(joker)).orElse(0);

            if (countList.contains(5)) {
                return FIVE_OF_A_KIND;
            }
            if (countList.contains(4)) {
                if (jCount == 1) {
                    return FIVE_OF_A_KIND;
                }
                return FOUR_OF_A_KIND;
            }
            if (countList.contains(3)) {
                if (countList.contains(2)) {
                    return FULL_HOUSE;
                }
                if (jCount == 1) {
                    return FOUR_OF_A_KIND;
                }
                if (jCount == 2) {
                    return FIVE_OF_A_KIND;
                }
                return THREE_OF_A_KIND;
            }
            if (countList.contains(2)) {
                if (countList.stream().filter(v -> v.equals(2)).count() == 2) {
                    if (jCount == 1) {
                        return FULL_HOUSE;
                    }
                    return TWO_PAIR;
                }
                if (jCount == 3) {
                    return FIVE_OF_A_KIND;
                }
                if (jCount == 2) {
                    return FOUR_OF_A_KIND;
                }
                if (jCount == 1) {
                    return THREE_OF_A_KIND;
                }
                return ONE_PAIR;
            }

            if (jCount == 5) {
                return FIVE_OF_A_KIND;
            }
            if (jCount == 4) {
                return FIVE_OF_A_KIND;
            }
            if (jCount == 3) {
                return FOUR_OF_A_KIND;
            }
            if (jCount == 2) {
                return THREE_OF_A_KIND;
            }
            if (jCount == 1) {
                return ONE_PAIR;
            }
            return HIGH_CARD;
        }
    }


    record Bid(long value) {
        static Bid parse(String input) {
            return new Bid(Long.parseLong(input.strip().trim()));
        }
    }
}
