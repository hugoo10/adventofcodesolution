package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;
import org.apache.commons.lang3.Range;

import java.util.*;
import java.util.stream.Collectors;

public class Problem16 extends Problem<Long> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem16.txt", Problem16.class);
    }

    List<Rule> rules;
    List<Ticket> nearbyTickets;
    Ticket myTicket;

    @Override
    public void setupData() {
        this.rules = new ArrayList<>();
        this.nearbyTickets = new ArrayList<>();
        int i = 0;
        for (; i < input.size(); ++i) {
            if (input.get(i).isEmpty()) {
                i += 2;
                break;
            }
            this.rules.add(new Rule(input.get(i)));
        }
        this.myTicket = new Ticket(input.get(i));
        i += 3;
        for (; i < input.size(); ++i) {
            this.nearbyTickets.add(new Ticket(input.get(i)));
        }
    }

    @Override
    public Long rule1() {
        List<Integer> notValids = new ArrayList<>();
        this.nearbyTickets.forEach(ticket -> notValids.addAll(ticket.validate(this.rules)));
        return (long) notValids.stream().reduce(0, Integer::sum);
    }

    @Override
    public Long rule2() {
        List<Ticket> validTickets = this.nearbyTickets.stream()
                .filter(ticket -> ticket.validate(this.rules).isEmpty())
                .collect(Collectors.toList());
        Map<Integer, List<Integer>> list = new HashMap<>();
        for (int i = 0; i < this.rules.size(); ++i) {
            Rule rule = this.rules.get(i);
            for (int j = 0; j < validTickets.get(i).values.size(); ++j) {
                final int idx = j;
                if (validTickets.parallelStream().allMatch(t -> rule.respect(t.values.get(idx)))) {
                    list.putIfAbsent(idx, new ArrayList<>());
                    list.get(idx).add(i);
                }
            }
        }
        Map<Integer, Integer> linked = new HashMap<>();
        List<Map.Entry<Integer, List<Integer>>> entries = new ArrayList<>(list.entrySet());
        do {
            entries.sort(Comparator.comparingInt(e -> e.getValue().size()));
            Map.Entry<Integer, List<Integer>> entry = entries.get(0);
            if (entry.getValue().size() == 1) {
                int val = entry.getValue().get(0);
                linked.put(val, entry.getKey());
                entries.remove(0);
                entries.forEach(e -> e.getValue().remove((Integer) val));
            } else {
                throw new RuntimeException("not solo " + entry.getKey());
            }
        } while (entries.stream().anyMatch(e -> !e.getValue().isEmpty()));

        long result = 1;
        for (int i = 0; i < this.rules.size(); ++i) {
            if (this.rules.get(i).name.startsWith("departure")) {

                result *= this.myTicket.values.get(linked.get(i));
            }
        }
        return result;
    }

    static class Rule {
        String name;
        List<Range<Integer>> ranges;

        public Rule(String s) {
            this.name = s.split(":")[0];
            this.ranges = Arrays.stream(s.split(":")[1].strip().split("or")).map(range -> {
                int from = Integer.parseInt(range.split("-")[0].strip());
                int to = Integer.parseInt(range.split("-")[1].strip());
                return Range.between(from, to);
            }).collect(Collectors.toList());
        }

        boolean respect(int value) {
            boolean isOk = false;
            int i = 0;
            do {
                isOk = isOk || this.ranges.get(i).contains(value);
                i++;
            } while (i < this.ranges.size() && !isOk);
            return isOk;
        }
    }


    static class Ticket {
        List<Integer> values;

        public Ticket(String s) {
            this.values = Arrays.stream(s.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        }

        List<Integer> validate(List<Rule> rules) {
            List<Integer> notValids = new ArrayList<>();
            for (int i = 0; i < values.size(); ++i) {
                final int val = values.get(i);
                if (rules.stream().noneMatch(r -> r.respect(val))) {
                    notValids.add(val);
                }
            }
            return notValids;
        }
    }
}
