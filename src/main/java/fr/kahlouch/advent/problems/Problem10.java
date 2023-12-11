package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;


public class Problem10 extends Problem {
    private Map<Position, Pipe> pipeMap = new HashMap<>();
    private Map<Position, Pipe> pipeMapWithGround = new HashMap<>();
    private Pipe startPipe;
    private List<Pipe> pipeLoop = new ArrayList<>();

    public static void main(String[] args) {
        Problem.solve(Problem10.class);
    }

    @Override
    public void setupData() {
        for (var y = this.lines.size() - 1; y >= 0; --y) {
            final var line = this.lines.get(y);
            for (var x = 0; x < line.length(); x++) {
                final var type = PipeType.parse(line.substring(x, x + 1));
                final var position = new Position(x, this.lines.size() - y - 1);
                final var pipe = Pipe.of(position, type);

                if (pipe.type == PipeType.START) {
                    this.startPipe = pipe;
                }
                if (pipe.type != PipeType.GROUND) {
                    this.pipeMap.put(position, pipe);
                }
                this.pipeMapWithGround.put(position, pipe);
            }
        }

        var previous = startPipe;
        var loopNext = startPipe.findPointingTo(this.pipeMap).get();
        pipeLoop.add(loopNext);
        while (!loopNext.equals(startPipe)) {
            final var next = loopNext.getNext(previous, pipeMap);
            previous = loopNext;
            loopNext = next;
            if (loopNext.equals(startPipe)) {
                final var beforeStart = pipeLoop.getLast().position;
                final var afterStart = pipeLoop.getFirst().position;
                final PipeType type;
                if (beforeStart.x == afterStart.x) {
                    type = PipeType.VERTICAL;
                } else if (beforeStart.y == afterStart.y) {
                    type = PipeType.HORIZONTAL;
                } else {
                    final Position sameLine;
                    final Position otherLine;
                    if (beforeStart.y == startPipe.position.y) {
                        sameLine = beforeStart;
                        otherLine = afterStart;
                    } else {
                        sameLine = afterStart;
                        otherLine = beforeStart;
                    }
                    if(sameLine.x < startPipe.position.x) {
                        if(otherLine.y < startPipe.position.y) {
                            type = PipeType.NORTH_WEST;
                        } else {
                            type = PipeType.SOUTH_WEST;
                        }
                    } else  {
                        if(otherLine.y < startPipe.position.y) {
                            type = PipeType.NORTH_EAST;
                        } else {
                            type = PipeType.NORTH_WEST;
                        }
                    }
                }
                final var startPipeWithType = Pipe.of(startPipe.position, type);
                pipeLoop.add(startPipeWithType);
                this.pipeMapWithGround.put(startPipeWithType.position, startPipeWithType);
            } else {
                pipeLoop.add(loopNext);
            }
        }

    }

    @Override
    public Object rule1() {
        return Math.ceil(this.pipeLoop.size() / 2.0);
    }

    @Override
    public Object rule2() {
        final var positionLoop = this.pipeLoop.stream().map(Pipe::position).toList();
        return this.pipeMapWithGround.values().stream()
                .filter(pipe -> pipe.type == PipeType.GROUND)
                .map(Pipe::position)
                .filter(position -> position.isInsideLoop(positionLoop, this.pipeMapWithGround))
                .count();
    }

    enum PipeType {
        VERTICAL, HORIZONTAL, NORTH_EAST, NORTH_WEST, SOUTH_EAST, SOUTH_WEST, START, GROUND;

        static PipeType parse(String input) {
            Objects.requireNonNull(input);
            if (input.length() > 1) {
                throw new IllegalArgumentException("size must be 1");
            }
            return switch (input) {
                case "|" -> VERTICAL;
                case "-" -> HORIZONTAL;
                case "L" -> NORTH_EAST;
                case "J" -> NORTH_WEST;
                case "7" -> SOUTH_WEST;
                case "F" -> SOUTH_EAST;
                case "." -> GROUND;
                case "S" -> START;
                default -> throw new IllegalArgumentException("unknown type: " + input);
            };
        }

    }

    record Position(int x, int y) {
        Position south() {
            return new Position(x, y - 1);
        }

        Position north() {
            return new Position(x, y + 1);
        }

        Position east() {
            return new Position(x + 1, y);
        }

        Position west() {
            return new Position(x - 1, y);
        }

        boolean isInsideLoop(List<Position> loop, Map<Position, Pipe> pipeMap) {
            var countWest = countCrossingPoints(loop, pipeMap, Position::west, List.of(PipeType.VERTICAL, PipeType.NORTH_WEST, PipeType.NORTH_EAST));
            if (countWest % 2 == 0) return false;
            var countEast = countCrossingPoints(loop, pipeMap, Position::west, List.of(PipeType.VERTICAL, PipeType.SOUTH_EAST, PipeType.SOUTH_WEST));
            if (countEast % 2 == 0) return false;

            return true;
        }

        int countCrossingPoints(List<Position> loop, Map<Position, Pipe> pipeMapWithGround, Function<Position, Position> next, List<PipeType> toAccept) {
            var tmpPos = this;
            var count = 0;
            while (pipeMapWithGround.containsKey(tmpPos)) {
                final var pipe = pipeMapWithGround.get(tmpPos);
                if(pipe.type == PipeType.GROUND && count > 0) {
                    return count;
                }
                if (loop.contains(tmpPos) && toAccept.contains(pipe.type)) {
                    count++;
                }
                tmpPos = next.apply(tmpPos);
            }
            return count;
        }


    }


    record Pipe(PipeType type, Position position, List<Position> pointTo) {
        public static Pipe of(Position position, PipeType type) {
            return switch (type) {
                case GROUND, START -> new Pipe(type, position, List.of());
                case VERTICAL -> {
                    final var pointTo = List.of(
                            position.north(),
                            position.south()
                    );
                    yield new Pipe(type, position, pointTo);
                }
                case HORIZONTAL -> {
                    final var pointTo = List.of(
                            position.west(),
                            position.east()
                    );
                    yield new Pipe(type, position, pointTo);
                }
                case NORTH_EAST -> {
                    final var pointTo = List.of(
                            position.north(),
                            position.east()
                    );
                    yield new Pipe(type, position, pointTo);
                }
                case NORTH_WEST -> {
                    final var pointTo = List.of(
                            position.north(),
                            position.west()
                    );
                    yield new Pipe(type, position, pointTo);
                }
                case SOUTH_WEST -> {
                    final var pointTo = List.of(
                            position.south(),
                            position.west()
                    );
                    yield new Pipe(type, position, pointTo);
                }
                case SOUTH_EAST -> {
                    final var pointTo = List.of(
                            position.south(),
                            position.east()
                    );
                    yield new Pipe(type, position, pointTo);
                }
            };
        }

        Optional<Pipe> findPointingTo(Map<Position, Pipe> pipeMap) {
            return Stream.of(this.position.north(),
                            this.position.south(),
                            this.position.east(),
                            this.position.west()
                    ).map(pipeMap::get)
                    .filter(Objects::nonNull)
                    .filter(pipe -> pipe.isPointingTo(this.position))
                    .findFirst();
        }

        boolean isPointingTo(Position position) {
            return this.pointTo.contains(position);
        }

        public Pipe getNext(Pipe previous, Map<Position, Pipe> pipeMap) {
            final var next = this.pointTo.stream()
                    .filter(position -> !position.equals(previous.position))
                    .map(pos -> {
                        if (pipeMap.containsKey(pos)) {
                            return pipeMap.get(pos);
                        }
                        throw new RuntimeException("next position doesnt exists");
                    })
                    .findFirst();
            if (next.isPresent()) {
                return next.get();
            }
            throw new RuntimeException("Next pipe not found");
        }
    }

}
