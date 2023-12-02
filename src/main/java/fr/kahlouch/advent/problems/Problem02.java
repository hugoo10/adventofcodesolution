package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class Problem02 extends Problem {
    private List<Game> games = new ArrayList<>();

    public static void main(String[] args) {
        Problem.solve(Problem02.class);
    }

    @Override
    public void setupData() {
        this.games = this.lines.stream()
                .map(Game::parse)
                .toList();
    }

    @Override
    public Object rule1() {
        final var actualBag = new Bag(12, 13, 14);
        return this.games.stream()
                .filter(game -> game.isPossible(actualBag))
                .mapToInt(Game::id)
                .sum();
    }

    @Override
    public Object rule2() {
        return this.games.stream()
                .map(Game::computeSmallestBag)
                .mapToInt(bag -> bag.red * bag.green * bag.blue)
                .sum();
    }

    private record Bag(int red, int green, int blue) {
        private static final Pattern RED_PATTERN = Pattern.compile("^(\\d+)\\sred$");
        private static final Pattern GREEN_PATTERN = Pattern.compile("^(\\d+)\\sgreen$");
        private static final Pattern BLUE_PATTERN = Pattern.compile("^(\\d+)\\sblue$");

        boolean canContains(Bag bag) {
            Objects.requireNonNull(bag);
            return bag.red <= this.red && bag.green <= this.green && bag.blue <= this.blue;
        }

        static Bag parse(String in) {
            Objects.requireNonNull(in);
            int red = 0;
            int green = 0;
            int blue = 0;
            for (var sub : in.split(",")) {
                var trimmed = sub.trim().strip();
                final var redMatcher = RED_PATTERN.matcher(trimmed);
                final var greenMatcher = GREEN_PATTERN.matcher(trimmed);
                final var blueMatcher = BLUE_PATTERN.matcher(trimmed);
                if (redMatcher.find()) {
                    red = Integer.parseInt(redMatcher.replaceAll("$1"));
                }
                if (greenMatcher.find()) {
                    green = Integer.parseInt(greenMatcher.replaceAll("$1"));
                }
                if (blueMatcher.find()) {
                    blue = Integer.parseInt(blueMatcher.replaceAll("$1"));
                }
            }
            return new Bag(red, green, blue);
        }
    }

    private record Game(int id, List<Bag> bags) {
        private Game {
            Objects.requireNonNull(bags);
        }

        private static final Pattern GAME_ID_PATTERN = Pattern.compile("^Game\\s(\\d+):\\s(.*)$");

        boolean isPossible(Bag actualBag) {
            Objects.requireNonNull(actualBag);
            return this.bags.stream().allMatch(actualBag::canContains);
        }

        static Game parse(String line) {
            Objects.requireNonNull(line);
            final var matcher = GAME_ID_PATTERN.matcher(line);
            final var gameId = Integer.parseInt(matcher.replaceAll("$1"));
            final var bags = Arrays.stream(GAME_ID_PATTERN.matcher(line)
                            .replaceAll("$2")
                            .split(";"))
                    .map(Bag::parse)
                    .toList();
            return new Game(gameId, bags);
        }

        Bag computeSmallestBag() {
            int red = this.bags.stream().mapToInt(Bag::red).max().orElse(0);
            int green = this.bags.stream().mapToInt(Bag::green).max().orElse(0);
            int blue = this.bags.stream().mapToInt(Bag::blue).max().orElse(0);
            return new Bag(red, green, blue);
        }
    }
}
