package fr.kahlouch.advent.problems;

import fr.kahlouch.advent.Problem;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Stream;

public class Problem13 extends Problem {
    List<Pair> pairs = new ArrayList<>();

    interface Value extends Comparable<Value> {


        static int getClosingBracketIdx(String str, int startIdx) {
            int count = 0;
            for (var i = startIdx; i < str.length(); ++i) {
                if (str.charAt(i) == '[') {
                    ++count;
                } else if (str.charAt(i) == ']') {
                    --count;
                }
                if (count == 0) return i;
            }
            return -1;
        }
    }

    record Pair(ArrayValue left, ArrayValue right) {
        boolean isRightOrder() {
            return left.compareTo(right) < 0;
        }
    }

    record ArrayValue(List<Value> values) implements Value {
        @Override
        public int compareTo(Value right) {
            if (right instanceof SimpleValue rightSimpleValue) {
                return this.compareTo(rightSimpleValue.toArrayValue());
            } else if (right instanceof ArrayValue rightArrayValue) {
                int maxSize = Math.max(this.values.size(), rightArrayValue.values.size());
                for (var i = 0; i < maxSize; ++i) {
                    if (this.values.size() <= i) return -1;
                    if (rightArrayValue.values.size() <= i) return 1;
                    var compare = this.values.get(i).compareTo(rightArrayValue.values.get(i));
                    if (compare != 0) return compare;
                }
                return 0;
            }
            throw new RuntimeException();
        }

        static ArrayValue parse(String str) {
            var arrayValue = new ArrayValue(new ArrayList<>());
            for (var i = 0; i < str.length(); ++i) {
                Value value;
                if (str.charAt(i) == '[') {
                    var endIdx = Value.getClosingBracketIdx(str, i);
                    if (endIdx == i + 1) {
                        value = new ArrayValue(new ArrayList<>());
                    } else {
                        value = ArrayValue.parse(str.substring(i + 1, endIdx));
                    }
                    i = endIdx + 1;
                } else {
                    var nextCommaIdx = str.indexOf(',', i);
                    if (nextCommaIdx == -1) {
                        value = new SimpleValue(Integer.parseInt(str.substring(i)));
                    } else {
                        try {
                            value = new SimpleValue(Integer.parseInt(str.substring(i, nextCommaIdx)));
                        } catch (Exception e) {
                            value = null;
                        }
                        i = nextCommaIdx;
                    }
                }
                arrayValue.values.add(value);
            }
            return arrayValue;
        }
    }

    record SimpleValue(int value) implements Value {
        ArrayValue toArrayValue() {
            return new ArrayValue(List.of(this));
        }

        @Override
        public int compareTo(Value right) {
            if (right instanceof SimpleValue rightSimpleValue) {
                return Integer.compare(this.value, rightSimpleValue.value);
            } else if (right instanceof ArrayValue rightArrayValue) {
                return this.toArrayValue().compareTo(rightArrayValue);
            }
            throw new RuntimeException();
        }
    }

    @Override
    public void setupData() {
        for (var i = 0; i < lines.size(); i += 3) {
            pairs.add(new Pair(
                    ArrayValue.parse(lines.get(i)),
                    ArrayValue.parse(lines.get(i + 1))
            ));
        }
    }

    /*
    --- Day 13: Distress Signal ---
    You climb the hill and again try contacting the Elves. However, you instead receive a signal you weren't expecting: a distress signal.

    Your handheld device must still not be working properly; the packets from the distress signal got decoded out of order. You'll need to re-order the list of received packets (your puzzle input) to decode the message.

    Your list consists of pairs of packets; pairs are separated by a blank line. You need to identify how many pairs of packets are in the right order.

    For example:

    [1,1,3,1,1]
    [1,1,5,1,1]

    [[1],[2,3,4]]
    [[1],4]

    [9]
    [[8,7,6]]

    [[4,4],4,4]
    [[4,4],4,4,4]

    [7,7,7,7]
    [7,7,7]

    []
    [3]

    [[[]]]
    [[]]

    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
    Packet data consists of lists and integers. Each list starts with [, ends with ], and contains zero or more comma-separated values (either integers or other lists). Each packet is always a list and appears on its own line.

    When comparing two values, the first value is called left and the second value is called right. Then:

    If both values are integers, the lower integer should come first. If the left integer is lower than the right integer, the inputs are in the right order. If the left integer is higher than the right integer, the inputs are not in the right order. Otherwise, the inputs are the same integer; continue checking the next part of the input.
    If both values are lists, compare the first value of each list, then the second value, and so on. If the left list runs out of items first, the inputs are in the right order. If the right list runs out of items first, the inputs are not in the right order. If the lists are the same length and no comparison makes a decision about the order, continue checking the next part of the input.
    If exactly one value is an integer, convert the integer to a list which contains that integer as its only value, then retry the comparison. For example, if comparing [0,0,0] and 2, convert the right value to [2] (a list containing 2); the result is then found by instead comparing [0,0,0] and [2].
    Using these rules, you can determine which of the pairs in the example are in the right order:

    == Pair 1 ==
    - Compare [1,1,3,1,1] vs [1,1,5,1,1]
      - Compare 1 vs 1
      - Compare 1 vs 1
      - Compare 3 vs 5
        - Left side is smaller, so inputs are in the right order

    == Pair 2 ==
    - Compare [[1],[2,3,4]] vs [[1],4]
      - Compare [1] vs [1]
        - Compare 1 vs 1
      - Compare [2,3,4] vs 4
        - Mixed types; convert right to [4] and retry comparison
        - Compare [2,3,4] vs [4]
          - Compare 2 vs 4
            - Left side is smaller, so inputs are in the right order

    == Pair 3 ==
    - Compare [9] vs [[8,7,6]]
      - Compare 9 vs [8,7,6]
        - Mixed types; convert left to [9] and retry comparison
        - Compare [9] vs [8,7,6]
          - Compare 9 vs 8
            - Right side is smaller, so inputs are not in the right order

    == Pair 4 ==
    - Compare [[4,4],4,4] vs [[4,4],4,4,4]
      - Compare [4,4] vs [4,4]
        - Compare 4 vs 4
        - Compare 4 vs 4
      - Compare 4 vs 4
      - Compare 4 vs 4
      - Left side ran out of items, so inputs are in the right order

    == Pair 5 ==
    - Compare [7,7,7,7] vs [7,7,7]
      - Compare 7 vs 7
      - Compare 7 vs 7
      - Compare 7 vs 7
      - Right side ran out of items, so inputs are not in the right order

    == Pair 6 ==
    - Compare [] vs [3]
      - Left side ran out of items, so inputs are in the right order

    == Pair 7 ==
    - Compare [[[]]] vs [[]]
      - Compare [[]] vs []
        - Right side ran out of items, so inputs are not in the right order

    == Pair 8 ==
    - Compare [1,[2,[3,[4,[5,6,7]]]],8,9] vs [1,[2,[3,[4,[5,6,0]]]],8,9]
      - Compare 1 vs 1
      - Compare [2,[3,[4,[5,6,7]]]] vs [2,[3,[4,[5,6,0]]]]
        - Compare 2 vs 2
        - Compare [3,[4,[5,6,7]]] vs [3,[4,[5,6,0]]]
          - Compare 3 vs 3
          - Compare [4,[5,6,7]] vs [4,[5,6,0]]
            - Compare 4 vs 4
            - Compare [5,6,7] vs [5,6,0]
              - Compare 5 vs 5
              - Compare 6 vs 6
              - Compare 7 vs 0
                - Right side is smaller, so inputs are not in the right order
    What are the indices of the pairs that are already in the right order? (The first pair has index 1, the second pair has index 2, and so on.) In the above example, the pairs in the right order are 1, 2, 4, and 6; the sum of these indices is 13.

    Determine which pairs of packets are already in the right order. What is the sum of the indices of those pairs?
     */
    @Override
    public Object rule1() {
        var sum =0;
        for(var i = 0; i<pairs.size();++i) {
            if(pairs.get(i).isRightOrder()){
                sum += i + 1;
            }
        }
        return sum;
    }

    /*
    --- Part Two ---
    Now, you just need to put all of the packets in the right order. Disregard the blank lines in your list of received packets.

    The distress signal protocol also requires that you include two additional divider packets:

    [[2]]
    [[6]]
    Using the same rules as before, organize all packets - the ones in your list of received packets as well as the two divider packets - into the correct order.

    For the example above, the result of putting the packets in the correct order is:

    []
    [[]]
    [[[]]]
    [1,1,3,1,1]
    [1,1,5,1,1]
    [[1],[2,3,4]]
    [1,[2,[3,[4,[5,6,0]]]],8,9]
    [1,[2,[3,[4,[5,6,7]]]],8,9]
    [[1],4]
    [[2]]
    [3]
    [[4,4],4,4]
    [[4,4],4,4,4]
    [[6]]
    [7,7,7]
    [7,7,7,7]
    [[8,7,6]]
    [9]
    Afterward, locate the divider packets. To find the decoder key for this distress signal, you need to determine the indices of the two divider packets and multiply them together. (The first packet is at index 1, the second packet is at index 2, and so on.) In this example, the divider packets are 10th and 14th, and so the decoder key is 140.

    Organize all of the packets into the correct order. What is the decoder key for the distress signal?
     */
    @Override
    public Object rule2() {
        var set = new TreeSet<>(pairs.stream().flatMap(pair -> Stream.of(pair.left, pair.right)).toList());
        var packet1 = ArrayValue.parse("[[2]]");
        var packet2 = ArrayValue.parse("[[6]]");
        set.add(packet1);
        set.add(packet2);
        var a = new ArrayList<>(set);
        return (a.indexOf(packet1) + 1) * (a.indexOf(packet2) + 1);
    }
}
