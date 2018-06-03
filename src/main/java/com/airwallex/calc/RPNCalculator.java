package com.airwallex.calc;

import java.io.Console;
import java.util.Arrays;
import java.util.List;

public class RPNCalculator {
	private static final List<String> EXIT_CMD = Arrays.asList("quit", "q");

	public static void main(String[] args) {
		Console console = System.console();
		if (console == null) {
			String err = "Unable to fetch console";
			System.out.println(err);
			return;
		}
		boolean isDone = false;
		RPN rpn = new RPN();
		while (!isDone) {
			String inputStr = console.readLine();
			if (EXIT_CMD.contains(inputStr)) {
				isDone = true;
				System.out.println("Exit!");
			} else {
				rpn.exec(inputStr);
			}
		}
	}
}
