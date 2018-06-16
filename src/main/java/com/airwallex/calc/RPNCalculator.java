package com.airwallex.calc;

import com.airwallex.common.Constant;
import com.airwallex.common.ErrorCode;
import com.airwallex.override.MyStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.out;

public class RPNCalculator {

	private MyStack<BigDecimal> numbers = new MyStack<>();
	private LinkedList<MyStack<BigDecimal>> history = new LinkedList<>();
	private static final String DISPLAY_FORMAT = "#.##########";
	private static final List<String> EXIT_CMD = Arrays.asList("quit", "q");

	public int exec(String input) {

		if(EXIT_CMD.contains(input.toLowerCase())) {
			out.println("Exiting RPN Calculator. Bye!");
			return Constant.EXIT_CODE_NORMAL_QUIT;
		}

		List<String> errorMsgs = new LinkedList<>();
		if(!isValidInputString(input, errorMsgs)){
			for (String errorMsg : errorMsgs) {
				out.println(errorMsg);
			}
			return 0;
		}

		int execPos = 0;
		for (String ele : input.split(Constant.PARAM_DELIMITER)) {
			Operator opr = Operator.find(ele);
			try {
				if (opr != null) {
					calc(numbers, opr, execPos);
				} else {
					numbers.push(new BigDecimal(ele));
				}
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
			logHistory((MyStack<BigDecimal>) numbers.clone());
			execPos += ele.length() + Constant.PARAM_DELIMITER.length();
		}
		printstack();

		return 0;
	}

	private boolean isValidInputString(String input, List<String> resultMsg) {
		boolean isValid = true;
		if(StringUtils.isEmpty(input)){
			isValid = false;
		}else {
			List<String> invalidEles = new LinkedList<>();
			for (String ele : input.split(Constant.PARAM_DELIMITER)) {
				if(!Operator.isValidOperator(ele) && !NumberUtils.isNumber(ele)){
					invalidEles.add(ele);
				}
			}
			if (!invalidEles.isEmpty()) {
				resultMsg.add(ErrorCode.WRONG_INPUT.getMessage(invalidEles));
				isValid = false;
			}
		}
		return isValid;
	}

	private void printstack() {
		String info = "stack: " + getNumbers().toString();
		out.println(info);
	}

	public MyStack<BigDecimal> calc(MyStack<BigDecimal> numbers, Operator opr, int execPos) {
		List<BigDecimal> calcArgs = new ArrayList<>();
		if (numbers.size() < opr.getOperand()) {
			String err = ErrorCode.PARAMETER_INSUFFICIENT.getMessage(opr.getOperatorText(), execPos + 1);
			out.println(err);
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
			out.println(err);
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
