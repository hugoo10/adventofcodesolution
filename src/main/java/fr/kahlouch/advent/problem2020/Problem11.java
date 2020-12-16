package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.util.List;

public class Problem11 extends Problem<Integer> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem11.txt", Problem11.class);
    }

    @Override
    public Integer rule1() {
        char[][] seats = getSeats(input);
        boolean hasChanged;
        do {
            hasChanged = false;
            char[][] next = copySeats(seats);
            for (int i = 0; i < seats.length; ++i) {
                for (int j = 0; j < seats[i].length; ++j) {
                    char oldSeat = seats[i][j];
                    next[i][j] = nextState(seats, i, j);
                    if (oldSeat != next[i][j]) {
                        hasChanged = true;
                    }
                }
            }
            seats = next;
        } while (hasChanged);

        int occupied = 0;

        for (int i = 0; i < seats.length; ++i) {
            for (int j = 0; j < seats[i].length; ++j) {
                if (!isEmpty(seats[i][j])) {
                    occupied++;
                }
            }
        }
        return occupied;
    }

    @Override
    public Integer rule2() {
        char[][] seats = getSeats(input);
        boolean hasChanged;
        do {
            hasChanged = false;
            char[][] next = copySeats(seats);
            for (int i = 0; i < seats.length; ++i) {
                for (int j = 0; j < seats[i].length; ++j) {
                    char oldSeat = seats[i][j];
                    next[i][j] = nextState2(seats, i, j);
                    if (oldSeat != next[i][j]) {
                        hasChanged = true;
                    }
                }
            }
            seats = next;
        } while (hasChanged);

        int occupied = 0;

        for (int i = 0; i < seats.length; ++i) {
            for (int j = 0; j < seats[i].length; ++j) {
                if (!isEmpty(seats[i][j])) {
                    occupied++;
                }
            }
        }
        return occupied;
    }

    private static char[][] getSeats(List<String> input) {
        char[][] seats = new char[input.size()][];
        for (int i = 0; i < input.size(); i++) {
            seats[i] = input.get(i).toCharArray();
        }
        return seats;
    }

    private static char[][] copySeats(char[][] previous) {
        char[][] seats = new char[previous.length][];
        for (int i = 0; i < previous.length; i++) {
            seats[i] = new char[previous[i].length];
            System.arraycopy(previous[i], 0, seats[i], 0, previous[i].length);
        }
        return seats;
    }


    private static char nextState(char[][] map, int x, int y) {
        char seat = map[x][y];
        if (isSeat(seat)) {
            int countEmpty = 0;
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i == 0 && j == 0) continue;
                    if (isEmpty(map, x + i, y + j)) {
                        countEmpty++;
                    }
                }
            }
            if (isEmpty(seat) && countEmpty == 8) {
                return '#';
            } else if (!isEmpty(seat) && countEmpty <= 4) {
                return 'L';
            }
            return map[x][y];
        }
        return '.';
    }

    private static char nextState2(char[][] map, int x, int y) {
        char seat = map[x][y];
        if (isSeat(seat)) {
            int countEmpty = 0;
            for (int i = -1; i <= 1; ++i) {
                for (int j = -1; j <= 1; ++j) {
                    if (i == 0 && j == 0) continue;
                    int mult = 0;
                    int tmpX;
                    int tmpY;
                    do {
                        mult++;
                        tmpX = x + i * mult;
                        tmpY = y + j * mult;
                    } while (!isOutBound(map, tmpX, tmpY) && !isSeat(map[tmpX][tmpY]));
                    if (isEmpty(map, tmpX, tmpY)) {
                        countEmpty++;
                    }
                }
            }
            if (isEmpty(seat) && countEmpty == 8) {
                return '#';
            } else if (!isEmpty(seat) && countEmpty <= 3) {
                return 'L';
            }
            return map[x][y];
        }
        return '.';
    }

    private static boolean isSeat(char c) {
        return c != '.';
    }

    private static boolean isOutBound(char[][] map, int x, int y) {
        return x < 0 || y < 0 || x >= map.length || y >= map[0].length;
    }

    private static boolean isEmpty(char[][] map, int x, int y) {
        if (isOutBound(map, x, y)) {
            return true;
        }
        return isEmpty(map[x][y]);
    }

    private static boolean isEmpty(char c) {
        return c != '#';
    }
}
