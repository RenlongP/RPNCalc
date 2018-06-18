package com.airwallex.operate;

import com.airwallex.mamo.MamoPad;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static java.lang.System.err;
import static java.lang.System.out;

public enum Operator {
    ADD("+", 2) {
        @Override
        public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            BigDecimal operand1 = workingDir.pop();
            return workingDir.pop().add(operand1, mc);
        }
    },
    SUBTRACT("-", 2) {
        @Override
        public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            BigDecimal operand1 = workingDir.pop();
            return workingDir.pop().subtract(operand1, mc);
        }
    },
    MULTIPLY("*", 2) {
        @Override
        public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            BigDecimal operand1 = workingDir.pop();
            return workingDir.pop().multiply(operand1, mc);
        }
    },
    DIVIDE("/", 2) {
        @Override
        public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            BigDecimal operand1 = workingDir.pop();
            if (operand1.compareTo(BigDecimal.ZERO) == 0) {
                out.print("warning: cannot divide 0.");
            }
            return workingDir.pop().divide(operand1, mc);
        }
    },
    SQRT("sqrt", 1) {
        @Override
        public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            return workingDir.pop().sqrt(mc);
        }
    },
    UNDO("undo", 0) {
        @Override
        public boolean executable(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            if (mamoPad.getSize() < 2) {
                err.println("Undo operation failed since no enough record");
                return false;
            }
            return true;
        }

        @Override
        public Stack<BigDecimal> getResultStack(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            return mamoPad.readLatest();
        }
    },
    CLEAR("clear", 0) {
        @Override
        public Stack<BigDecimal> getResultStack(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
            workingDir.clear();
            return workingDir;
        }
    };

    private final String operatorText;
    private final int operand;
    private static final Map<String, Operator> map;
    private static final MathContext mc = new MathContext(15, RoundingMode.HALF_UP);

    static {
        map = new HashMap<>();
        for (Operator opr : Operator.values()) {
            map.put(opr.operatorText, opr);
        }
    }

    public static boolean isValidOperator(String ele) {
        return map.containsKey(ele);
    }

    public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
        return new BigDecimal(0);
    }

    Operator(String operatorText, int operand) {
        this.operatorText = operatorText;
        this.operand = operand;
    }

    public static Operator find(String opr) {
        return map.get(opr);
    }

    public String getOperatorText() {
        return operatorText;
    }

    public int getOperand() {
        return operand;
    }

    public boolean executable(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
        return this.operand <= workingDir.size();
    }

    public Stack<BigDecimal> getResultStack(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
        return workingDir;
    }
}
