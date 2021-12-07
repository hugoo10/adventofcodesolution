package fr.kahlouch.advent;

import java.util.*;

public class Problem06 extends Problem {
    List<Fish> fishs = new ArrayList<>();

    @Override
    public void setupData() {
        for (var nbStr : input.get(0).split(",")) {
            fishs.add(new Fish(Integer.parseInt(nbStr)));
        }
    }

    @Override
    public Object rule1() {
        for (var day = 0; day < 80; ++day) {
            Collection<Fish> newFishs = fishs.stream()
                    .map(Fish::live)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();
            this.fishs.addAll(newFishs);
        }

        return this.fishs.size();
    }

    @Override
    public Object rule2() {
        Map<Integer, Long> res = new HashMap<>();
        return this.fishs.stream()
                .map(f -> {
                    if (!res.containsKey(f.life)) {
                        res.put(f.life, nbFish(f.life, 256));
                    }
                    return res.get(f.life);
                })
                .reduce(0L, Long::sum);
    }

    public static long nbFish(int life, int day) {
        return nbFish(1, life, day);
    }

    public static long nbFish(int cnt, int life, int day) {
        int rest = day - life;
        if (rest <= 0) {
            return cnt;
        } else {
            return cnt + nbFish(0, 6, rest - 1) + nbFish(1, 8, rest - 1);
        }
    }

    static class Fish {
        private int life;

        public Fish(int life) {
            this.life = life;
        }

        public Fish() {
            this(8);
        }

        public Optional<Fish> live() {
            if (life == 0) {
                life = 6;
                return Optional.of(new Fish());
            } else {
                --life;
                return Optional.empty();
            }
        }
    }
}
