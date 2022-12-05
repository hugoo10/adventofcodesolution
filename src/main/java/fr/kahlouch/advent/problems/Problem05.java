package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class Problem05 extends Problem {
    List<Command> commands = new ArrayList<>();
    List<CrateStack> stacks = new ArrayList<>();

    record Command(int nb, int from, int to) {
        public Command(String command) {
            this(parseCommand(command));
        }

        public Command(List<Integer> list) {
            this(list.get(0), list.get(1), list.get(2));
        }

        static List<Integer> parseCommand(String command) {
            return Arrays.stream(command.replace("move ", "")
                            .replace(" from ", ";")
                            .replace(" to ", ";")
                            .split(";"))
                    .map(Integer::parseInt)
                    .toList();
        }

        public void execute(List<CrateStack> stacks) {
            var fromCrate = stacks.get(from - 1);
            var toCrate = stacks.get(to - 1);
            fromCrate.moveXTo(nb, toCrate);
        }

        public void execute9001(List<CrateStack> stacks) {
            var fromCrate = stacks.get(from - 1);
            var toCrate = stacks.get(to - 1);
            fromCrate.moveXTo9001(nb, toCrate);
        }
    }

    record CrateStack(Stack<String> stack) {
        public CrateStack() {
            this(new Stack<>());
        }

        String getTop() {
            return this.stack.peek();
        }

        void moveXTo(int nb, CrateStack crateStack) {
            for (var i = 0; i < nb; ++i) {
                crateStack.addCrate(this.stack.pop());
            }
        }

        void moveXTo9001(int nb, CrateStack crateStack) {
            var tmpStack = new Stack<String>();
            for (var i = 0; i < nb; ++i) {
                tmpStack.push(this.stack.pop());
            }
            for (var i = 0; i < nb; ++i) {
                crateStack.addCrate(tmpStack.pop());
            }
        }

        void addCrate(String crate) {
            this.stack.push(crate);
        }
    }

    @Override
    public void setupData() {
        stacks = new ArrayList<>();
        var cs1 = new CrateStack();
        cs1.addCrate("Q");
        cs1.addCrate("F");
        cs1.addCrate("M");
        cs1.addCrate("R");
        cs1.addCrate("L");
        cs1.addCrate("W");
        cs1.addCrate("C");
        cs1.addCrate("V");
        var cs2 = new CrateStack();
        cs2.addCrate("D");
        cs2.addCrate("Q");
        cs2.addCrate("L");
        var cs3 = new CrateStack();
        cs3.addCrate("P");
        cs3.addCrate("S");
        cs3.addCrate("R");
        cs3.addCrate("G");
        cs3.addCrate("W");
        cs3.addCrate("C");
        cs3.addCrate("N");
        cs3.addCrate("B");
        var cs4 = new CrateStack();
        cs4.addCrate("L");
        cs4.addCrate("C");
        cs4.addCrate("D");
        cs4.addCrate("H");
        cs4.addCrate("B");
        cs4.addCrate("Q");
        cs4.addCrate("G");
        var cs5 = new CrateStack();
        cs5.addCrate("V");
        cs5.addCrate("G");
        cs5.addCrate("L");
        cs5.addCrate("F");
        cs5.addCrate("Z");
        cs5.addCrate("S");
        var cs6 = new CrateStack();
        cs6.addCrate("D");
        cs6.addCrate("G");
        cs6.addCrate("N");
        cs6.addCrate("P");
        var cs7 = new CrateStack();
        cs7.addCrate("D");
        cs7.addCrate("Z");
        cs7.addCrate("P");
        cs7.addCrate("V");
        cs7.addCrate("F");
        cs7.addCrate("C");
        cs7.addCrate("W");
        var cs8 = new CrateStack();
        cs8.addCrate("C");
        cs8.addCrate("P");
        cs8.addCrate("D");
        cs8.addCrate("M");
        cs8.addCrate("S");
        var cs9 = new CrateStack();
        cs9.addCrate("Z");
        cs9.addCrate("N");
        cs9.addCrate("W");
        cs9.addCrate("T");
        cs9.addCrate("V");
        cs9.addCrate("M");
        cs9.addCrate("C");
        cs9.addCrate("P");
        stacks.add(cs1);
        stacks.add(cs2);
        stacks.add(cs3);
        stacks.add(cs4);
        stacks.add(cs5);
        stacks.add(cs6);
        stacks.add(cs7);
        stacks.add(cs8);
        stacks.add(cs9);
        commands = lines.stream().skip(10).map(Command::new).toList();
    }

    /*
    --- Day 5: Supply Stacks ---
    The expedition can depart as soon as the final supplies have been unloaded from the ships. Supplies are stored in stacks of marked crates, but because the needed supplies are buried under many other crates, the crates need to be rearranged.

    The ship has a giant cargo crane capable of moving crates between stacks. To ensure none of the crates get crushed or fall over, the crane operator will rearrange them in a series of carefully-planned steps. After the crates are rearranged, the desired crates will be at the top of each stack.

    The Elves don't want to interrupt the crane operator during this delicate procedure, but they forgot to ask her which crate will end up where, and they want to be ready to unload them as soon as possible so they can embark.

    They do, however, have a drawing of the starting stacks of crates and the rearrangement procedure (your puzzle input). For example:

        [D]
    [N] [C]
    [Z] [M] [P]
     1   2   3

    move 1 from 2 to 1
    move 3 from 1 to 3
    move 2 from 2 to 1
    move 1 from 1 to 2
    In this example, there are three stacks of crates. Stack 1 contains two crates: crate Z is on the bottom, and crate N is on top. Stack 2 contains three crates; from bottom to top, they are crates M, C, and D. Finally, stack 3 contains a single crate, P.

    Then, the rearrangement procedure is given. In each step of the procedure, a quantity of crates is moved from one stack to a different stack. In the first step of the above rearrangement procedure, one crate is moved from stack 2 to stack 1, resulting in this configuration:

    [D]
    [N] [C]
    [Z] [M] [P]
     1   2   3
    In the second step, three crates are moved from stack 1 to stack 3. Crates are moved one at a time, so the first crate to be moved (D) ends up below the second and third crates:

            [Z]
            [N]
        [C] [D]
        [M] [P]
     1   2   3
    Then, both crates are moved from stack 2 to stack 1. Again, because crates are moved one at a time, crate C ends up below crate M:

            [Z]
            [N]
    [M]     [D]
    [C]     [P]
     1   2   3
    Finally, one crate is moved from stack 1 to stack 2:

            [Z]
            [N]
            [D]
    [C] [M] [P]
     1   2   3
    The Elves just need to know which crate will end up on top of each stack; in this example, the top crates are C in stack 1, M in stack 2, and Z in stack 3, so you should combine these together and give the Elves the message CMZ.

    After the rearrangement procedure completes, what crate ends up on top of each stack?
     */
    @Override
    public Object rule1() {
        this.commands.forEach(c -> c.execute(stacks));
        return this.stacks.stream().map(CrateStack::stack).map(Stack::peek).collect(Collectors.joining());
    }

    /*
    --- Part Two ---
    As you watch the crane operator expertly rearrange the crates, you notice the process isn't following your prediction.

    Some mud was covering the writing on the side of the crane, and you quickly wipe it away. The crane isn't a CrateMover 9000 - it's a CrateMover 9001.

    The CrateMover 9001 is notable for many new and exciting features: air conditioning, leather seats, an extra cup holder, and the ability to pick up and move multiple crates at once.

    Again considering the example above, the crates begin in the same configuration:

        [D]
    [N] [C]
    [Z] [M] [P]
     1   2   3
    Moving a single crate from stack 2 to stack 1 behaves the same as before:

    [D]
    [N] [C]
    [Z] [M] [P]
     1   2   3
    However, the action of moving three crates from stack 1 to stack 3 means that those three moved crates stay in the same order, resulting in this new configuration:

            [D]
            [N]
        [C] [Z]
        [M] [P]
     1   2   3
    Next, as both crates are moved from stack 2 to stack 1, they retain their order as well:

            [D]
            [N]
    [C]     [Z]
    [M]     [P]
     1   2   3
    Finally, a single crate is still moved from stack 1 to stack 2, but now it's crate C that gets moved:

            [D]
            [N]
            [Z]
    [M] [C] [P]
     1   2   3
    In this example, the CrateMover 9001 has put the crates in a totally different order: MCD.

    Before the rearrangement process finishes, update your simulation so that the Elves know where they should stand to be ready to unload the final supplies. After the rearrangement procedure completes, what crate ends up on top of each stack?
     */
    @Override
    public Object rule2() {
        this.commands.forEach(c -> c.execute9001(stacks));
        return this.stacks.stream().map(CrateStack::stack).map(Stack::peek).collect(Collectors.joining());
    }
}
