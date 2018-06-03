package com.airwallex.calc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum Operator {
	ADD("+", 2) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return numList.get(1).add(numList.get(0), mc);
		}
	},
	SUBTRACT("-", 2) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return numList.get(1).subtract(numList.get(0), mc);
		}
	},
	MULTIPLY("*", 2) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return numList.get(1).multiply(numList.get(0), mc);
		}
	},
	DIVIDE("/", 2) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return numList.get(1).divide(numList.get(0), mc);
		}
	},
	SQRT("sqrt", 1) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return numList.get(0).sqrt(mc);
		}
	},
	UNDO("undo", 0) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return new BigDecimal(0);
		}
	},
	CLEAR("clear", 0) {
		@Override
		public BigDecimal apply(List<BigDecimal> numList) {
			return new BigDecimal(0);
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

	public abstract BigDecimal apply(List<BigDecimal> numList);

	private Operator(String operatorText, int operand) {
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

}
