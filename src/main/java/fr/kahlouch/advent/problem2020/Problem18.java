package fr.kahlouch.advent.problem2020;

import fr.kahlouch.advent.Problem;

import java.util.*;

public class Problem18 extends Problem<Long> {

    @Override
    public void setupData() {
    }

    @Override
    public Long rule1() {
        return this.input.stream()
                .map(Expression::new)
                .map(ex -> ex.computeRule(0))
                .reduce(0L, Long::sum);
    }

    @Override
    public Long rule2() {
        return this.input.stream()
                .map(line -> {
                    Expression expression = new Expression(line);
                    expression.rearrangePriorityV2();
                    return expression;
                })
                .map(ex -> ex.computeRule(0))
                .reduce(0L, Long::sum);
    }

    class Expression {
        Character op;
        Long value;
        List<Expression> expressions = new ArrayList<>();

        int nextIdx;

        public Expression(List<Expression> expressions) {
            this.expressions = expressions;
        }

        Expression(String line) {
            Expression expression;
            int idx = 0;
            do {
                expression = new Expression(line, idx);
                idx = expression.nextIdx;
                expressions.add(expression);
            } while (expression.nextIdx < line.length());
        }

        Expression(String line, int idx) {
            parse(line, idx);
        }

        private void rearrangePriorityV2() {
            int idx;
            do {
                idx = -1;
                for (int i = 0; i < this.expressions.size(); ++i) {
                    this.expressions.get(i).rearrangePriorityV2();
                    if (this.expressions.get(i).op != null && this.expressions.get(i).op == '+' && this.expressions.size() > 2) {
                        idx = i;
                        Character previousOp = this.expressions.get(i - 1).op;
                        this.expressions.get(i - 1).op = null;
                        List<Expression> expressions = new ArrayList<>(List.of(this.expressions.get(i - 1), this.expressions.get(i)));
                        this.expressions.remove(i - 1);
                        this.expressions.remove(i - 1);
                        Expression newExpression = new Expression(expressions);
                        newExpression.op = previousOp;
                        this.expressions.add(i - 1, newExpression);
                    }
                }
            } while (idx > -1);
        }

        private void parse(String line, int idx) {
            final Optional<Map.Entry<Character, Integer>> operatorOpt = findOperator(line, idx);
            final int startValueIdx;
            if (operatorOpt.isPresent()) {
                this.op = operatorOpt.get().getKey();
                startValueIdx = operatorOpt.get().getValue();
            } else {
                startValueIdx = idx;
            }
            final OptionalInt endParenthesisOpt = getEndParenthesis(line, startValueIdx);
            if (endParenthesisOpt.isPresent()) {
                this.expressions.add(new Expression(line.substring(startValueIdx + 1, endParenthesisOpt.getAsInt())));
                this.nextIdx = endParenthesisOpt.getAsInt() + 2;
            } else {
                Map.Entry<Long, Integer> valueEntry = findValue(line, startValueIdx);
                this.value = valueEntry.getKey();
                this.nextIdx = valueEntry.getValue();
            }
        }

        private Optional<Map.Entry<Character, Integer>> findOperator(String line, int from) {
            int to = line.indexOf(' ', from);
            if (to == -1) {
                to = line.length();
            }
            String potentialOperator = line.substring(from, to);
            if (List.of("*", "+").contains(potentialOperator)) {
                return Optional.of(Map.entry(potentialOperator.charAt(0), to + 1));
            }
            return Optional.empty();
        }

        private Map.Entry<Long, Integer> findValue(String line, int from) {
            int to = line.indexOf(' ', from);
            if (to == -1) {
                to = line.length();
            }
            String valueStr = line.substring(from, to);
            return Map.entry(Long.parseLong(valueStr), to + 1);
        }

        private OptionalInt getEndParenthesis(String line, int from) {
            if (line.charAt(from) == '(') {
                int count = 0;
                for (int i = from; i < line.length(); ++i) {
                    if (line.charAt(i) == '(') {
                        ++count;
                    }
                    if (line.charAt(i) == ')') {
                        --count;
                    }
                    if (count == 0) {
                        return OptionalInt.of(i);
                    }
                }
            }
            return OptionalInt.empty();
        }

        public long getValue() {
            long result = 0;
            if (this.value == null) {
                for (Expression expression : this.expressions) {
                    result = expression.computeRule(result);
                }
                return result;
            }
            return this.value;
        }

        public Character getOP() {
            if (this.op == null) {
                if (this.expressions.isEmpty()) return null;
                return this.expressions.get(0).getOP();
            }
            return this.op;
        }

        public long computeRule(long previous) {
            long result = previous;
            if (this.getOP() == null) {
                result = this.getValue();
            } else {
                if (this.getOP() == '+') {
                    result = result + this.getValue();
                } else {
                    result = result * this.getValue();
                }
            }
            return result;
        }
    }
}
