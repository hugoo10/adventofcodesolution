package fr.kahlouch.coding.advent_2015;

import fr.kahlouch.coding._common.input.Input;
import fr.kahlouch.coding._common.input.parse.Parser;
import fr.kahlouch.coding._common.problem.AdventProblem;
import fr.kahlouch.coding._common.regex.Regex;

import java.nio.file.Path;

class Day14 extends AdventProblem {
    public static void main(String[] args) {
        new Day14();
    }

    @Override
    protected Object resolve1(Path inputPath) {
        return Input.of(inputPath).multiLines()
                .lines(ReindeerParser.INSTANCE)
                .mapToDouble(rd -> rd.distanceAt(2503))
                .max()
                .getAsDouble();
    }

    @Override
    protected Object resolve2(Path inputPath, Object response1) {
        final var deers = Input.of(inputPath).multiLines()
                .lines(ReindeerParser.INSTANCE)
                .map(Reindeer::toDeerState).toList();
        for (var i = 0; i < 2503; ++i) {
            final var max = deers.stream().peek(DeerState::next).mapToDouble(DeerState::distance).max().getAsDouble();
            deers.forEach(r -> r.incrementScoreIfDistanceIs(max));
        }
        return deers.stream().mapToDouble(DeerState::score).max().getAsDouble();
    }

    private enum ReindeerParser implements Parser<Reindeer> {
        INSTANCE;
        private static final Regex REINDEER_REGEX = new Regex("^(\\S+)\\scan\\sfly\\s(\\d+)\\skm\\/s\\sfor\\s(\\d+)\\sseconds,\\sbut\\sthen\\smust\\srest\\sfor\\s(\\d+)\\sseconds\\.$");

        @Override
        public Reindeer parse(String input, long idx) {
            final var analyzer = REINDEER_REGEX.match(input);
            final var name = analyzer.group(1);
            final var speed = analyzer.groupDouble(2);
            final var flightDuration = analyzer.groupDouble(3);
            final var pauseDuration = analyzer.groupDouble(4);
            return new Reindeer(name, speed, flightDuration, pauseDuration);
        }
    }

    private record Reindeer(String name, double speed, double flightDuration, double pauseDuration) {
        public double distanceAt(double time) {
            double distance = 0;
            double currentTime = 0;
            boolean pause = false;
            while (currentTime < time) {
                if (pause) {
                    currentTime += pauseDuration;
                    pause = false;
                } else {
                    final double timeToCompute;
                    if (currentTime + flightDuration > time) {
                        timeToCompute = time - currentTime;
                    } else {
                        timeToCompute = flightDuration;
                    }
                    currentTime += timeToCompute;
                    distance += timeToCompute * speed;
                    pause = true;
                }
            }
            return distance;
        }

        public DeerState toDeerState() {
            return new DeerState(name, speed, flightDuration, pauseDuration);
        }
    }

    private static class DeerState {
        private String name;
        private double speed;
        private double flightDuration;
        private double pauseDuration;
        private double distance;
        private double score;
        private double flightCountdown;
        private double pauseCountdown;

        public DeerState(String name, double speed, double flightDuration, double pauseDuration) {
            this.name = name;
            this.speed = speed;
            this.flightDuration = flightDuration;
            this.pauseDuration = pauseDuration;
            this.distance = 0;
            this.score = 0;
            this.flightCountdown = this.flightDuration;
            this.pauseCountdown = 0;
        }

        public void next() {
            if (this.flightCountdown > 0) {
                this.distance += this.speed;
                if (--this.flightCountdown == 0) {
                    this.pauseCountdown = this.pauseDuration;
                }
            } else {
                if (--this.pauseCountdown == 0) {
                    this.flightCountdown = this.flightDuration;
                }
            }
        }

        public double distance() {
            return this.distance;
        }

        public double score() {
            return this.score;
        }

        public void incrementScoreIfDistanceIs(double distance) {
            if (this.distance == distance) {
                this.score++;
            }
        }


    }
}
