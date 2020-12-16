package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;
import fr.kahlouch.advent.ProblemSolver;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Problem08 extends Problem<Integer> {
    public static void main(String[] args) {
        ProblemSolver.solve("problem2020/problem08.txt", Problem08.class);
    }

    @Override
    public Integer rule1() {
        List<Command> commands = getCommands(input, -1);
        List<Command> historic = new ArrayList<>();
        int idx = 0;
        int value = 0;

        boolean ok;

        do {
            ok = true;
            Command toExecute = commands.get(idx);
            Out out = toExecute.execute();
            value += out.deltaValue;
            idx += out.deltaIdx;
            final int toTestIdx = idx;
            if (historic.stream().anyMatch(c -> c.idx == toTestIdx)) {
                ok = false;
            }
            historic.add(toExecute);
        } while (ok && idx < input.size());

        return value;
    }

    @Override
    public Integer rule2() {
        boolean ok;
        int toSwap = 0;
        int value;
        do {
            List<Command> commands = getCommands(input, toSwap);
            List<Command> historic = new ArrayList<>();
            int idx = 0;
            value = 0;
            do {
                ok = true;
                Command toExecute = commands.get(idx);
                Out out = toExecute.execute();
                value += out.deltaValue;
                idx += out.deltaIdx;
                final int toTestIdx = idx;
                if (historic.stream().anyMatch(c -> c.idx == toTestIdx)) {
                    ok = false;
                }
                historic.add(toExecute);
            } while (ok && idx < input.size());
            toSwap++;

        } while (!ok && toSwap < input.size());
        return value;
    }

    public static List<Command> getCommands(List<String> input, int toSwap) {
        List<Command> commands = new ArrayList<>();
        for (int i = 0; i < input.size(); ++i) {
            Command command = new Command(input.get(i), i);
            if (toSwap == i) {
                command = command.swap();
            }
            commands.add(command);
        }
        return commands;
    }

    static class Command {
        int idx;
        Supplier<Out> action;
        int argument;
        String actionStr;

        Command(String line, int idx) {
            this(idx, Integer.parseInt(line.split(" ")[1]), line.split(" ")[0]);
        }

        public Command(int idx, int argument, String actionStr) {
            this.idx = idx;
            this.argument = argument;
            this.actionStr = actionStr;
            this.action = switch (this.actionStr) {
                case "acc" -> this::acc;
                case "jmp" -> this::jmp;
                case "nop" -> this::nop;
                default -> throw new RuntimeException("NO COMMAND FOUND");
            };
        }

        @Override
        public String toString() {
            return "Command{" +
                    "idx=" + idx +
                    ", argument=" + argument +
                    ", actionStr='" + actionStr + '\'' +
                    '}';
        }

        public Command swap() {
            String newCommand = switch (this.actionStr) {
                case "jmp" -> "nop";
                case "nop" -> "jmp";
                case "acc" -> "acc";
                default -> throw new RuntimeException("BUG");
            };
            return this.setAction(newCommand);
        }

        private Command setAction(String actionStr) {
            return new Command(this.idx, this.argument, actionStr);
        }

        private Out acc() {
            return new Out(this.argument, 1);
        }

        private Out jmp() {
            return new Out(0, this.argument);
        }

        private Out nop() {
            return new Out(0, 1);
        }

        public Out execute() {
            return this.action.get();
        }
    }

    static class Out {
        int deltaValue;
        int deltaIdx;

        public Out(int deltaValue, int deltaIdx) {
            this.deltaValue = deltaValue;
            this.deltaIdx = deltaIdx;
        }
    }
}
