package com.airwallex.calc;

import com.airwallex.mamo.MamoPad;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public enum Operator {
	ADD("+", 2) {
		@Override
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			BigDecimal tmp = workingDir.pop();
			return workingDir.pop().add(tmp, mc);
		}
	},
	SUBTRACT("-", 2) {
		@Override
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			BigDecimal tmp = workingDir.pop();
			return workingDir.pop().subtract(tmp, mc);
		}
	},
	MULTIPLY("*", 2) {
		@Override
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			BigDecimal tmp = workingDir.pop();
			return workingDir.pop().multiply(tmp, mc);
		}
	},
	DIVIDE("/", 2) {
		@Override
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			BigDecimal tmp = workingDir.pop();
			return workingDir.pop().divide(tmp, mc);
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
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			return null;
		}
	},
	CLEAR("clear", 0) {
		@Override
		public BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad) {
			workingDir.clear();
			return null;
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

	public abstract BigDecimal apply(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad);

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

	public boolean executable(Stack<BigDecimal> workingDir, MamoPad<Stack<BigDecimal>> mamoPad){
		return this.operand <= workingDir.size();
	};
}
