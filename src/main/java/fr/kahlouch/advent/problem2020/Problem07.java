package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Problem07 extends Problem<Integer> {
    public static Map<String, Bag> getBags(List<String> input) {
        Map<String, Bag> bags = new HashMap<>();
        input.stream().map(Bag::new).forEach(b -> bags.put(b.color, b));
        return bags;
    }

    @Override
    public Integer rule1() {
        Map<String, Bag> bags = getBags(input);
        int count = 0;
        for (Map.Entry<String, Bag> entry : bags.entrySet()) {
            if (contains(entry.getValue(), "shiny gold", bags)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public Integer rule2() {
        Map<String, Bag> bags = getBags(input);
        Bag shiny = bags.get("shiny gold");
        return count(shiny, bags, 1);
    }

    public static int count(Bag bag, Map<String, Bag> bags, int nbParent) {
        int count = 0;
        if (!bag.subBag.isEmpty()) {
            for(Map.Entry<Integer, String> entry: bag.subBag) {
                count += entry.getKey() * nbParent;
                count += count(bags.get(entry.getValue()), bags, entry.getKey()) * nbParent;
            }
        }
        return count;
    }

    public static boolean contains(Bag bag, String color, Map<String, Bag> bags) {
        if (bag.color.equals(color)) return false;
        if (bag.subBag.isEmpty()) return false;
        if (bag.subBag.stream().map(Map.Entry::getValue).anyMatch(v -> v.equals(color))) return true;
        return bag.subBag.stream().anyMatch(entry -> contains(bags.get(entry.getValue()), color, bags));
    }

    static class Bag {
        String color;
        List<Map.Entry<Integer, String>> subBag;

        public Bag(String line) {
            this.subBag = new ArrayList<>();
            String[] splitted = line.replace("bags", "").replace("bag", "").replace(".", "").split("contain");
            this.color = splitted[0].strip();
            if (!splitted[1].contains("no other")) {
                String[] subBags = splitted[1].split(",");
                for (String subBag : subBags) {
                    int indexOfFirstSpace = subBag.strip().indexOf(" ");
                    int nb = Integer.parseInt(subBag.strip().substring(0, indexOfFirstSpace));
                    String colorSub = subBag.strip().substring(indexOfFirstSpace + 1);
                    this.subBag.add(Map.entry(nb, colorSub));
                }
            }
        }
    }
}
