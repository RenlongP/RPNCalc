package com.airwallex.calc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.airwallex.common.Constant;
import com.airwallex.common.ErrorCode;
import com.airwallex.override.MyStack;

public class RPN {

	private MyStack<BigDecimal> numbers = new MyStack<>();
	private LinkedList<MyStack<BigDecimal>> history = new LinkedList<>();

	@SuppressWarnings("unchecked")
	public RPN exec(String input) {
		int execPos = 0;
		for (String number : input.split(Constant.PARAM_DELIMITER)) {
			Operator opr = Operator.find(number);
			try {
				if (opr != null) {
					calc(numbers, opr, execPos);
				} else {
					numbers.push(new BigDecimal(number));
				}
			} catch (NumberFormatException e) {
				String err = ErrorCode.WRONG_INPUT.getMessage(number);
				System.out.println(err);
				break;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			logHistory((MyStack<BigDecimal>) numbers.clone());
			execPos += number.length() + Constant.PARAM_DELIMITER.length();
		}
		printstack();
		return this;
	}

	private void printstack() {
		String info = "stack: " + getNumbers().toString();
		System.out.println(info);
	}

	public MyStack<BigDecimal> calc(MyStack<BigDecimal> numbers, Operator opr, int execPos) {
		List<BigDecimal> calcArgs = new ArrayList<>();
		if (numbers.size() < opr.getOperand()) {
			String err = ErrorCode.PARAMETER_INSUFFICIENT.getMessage(opr.getOperatorText(), execPos + 1);
			System.out.println(err);
			throw new IllegalStateException(err);
		}
		for (int i = 0; i < opr.getOperand(); i++) {
			calcArgs.add(numbers.pop());
		}
		if (opr.equals(Operator.UNDO)) {
			undo();
		} else if (opr.equals(Operator.CLEAR)) {
			numbers.clear();
		} else {
			numbers.push(opr.apply(calcArgs));
		}
		return numbers;
	}

	private void logHistory(MyStack<BigDecimal> numbers) {
		this.history.addFirst(numbers);
	}

	private void undo() {
		if (this.history.size() < 2) {
			String err = "Undo operation failed since no enough history";
			System.out.println(err);
			return;
		}

		this.setNumbers(this.history.get(1));
		this.history.removeFirst();
		this.history.removeFirst();
	}

	public MyStack<BigDecimal> getNumbers() {
		return numbers;
	}

	public void setNumbers(MyStack<BigDecimal> numbers) {
		this.numbers = numbers;
	}

}
