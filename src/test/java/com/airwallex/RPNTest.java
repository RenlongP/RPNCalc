package com.airwallex;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.airwallex.calc.RPN;
import com.airwallex.common.ErrorCode;

public class RPNTest {
	private RPN rpn;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Before
	public void initRPN() {
		rpn = new RPN();
	}

	private void assertResultEq(String expect, RPN rpn) {
		assertEquals(expect, rpn.getNumbers().toString());
	}

	@Test
	public void example1() {
		rpn.exec("5 2");
		assertResultEq("5 2", rpn);
	}

	@Test
	public void example2() {
		rpn.exec("2 sqrt");
		assertResultEq("1.4142135623", rpn);
		rpn.exec("clear 9 sqrt");
		assertResultEq("3", rpn);
	}

	@Test
	public void example3() {
		rpn.exec("5 2 -");
		assertResultEq("3", rpn);
		rpn.exec("3 -");
		assertResultEq("0", rpn);
	}

	@Test
	public void example4() {
		rpn.exec("5 4 3 2");
		assertResultEq("5 4 3 2", rpn);
		rpn.exec("undo undo *");
		assertResultEq("20", rpn);
		rpn.exec("5 *");
		assertResultEq("100", rpn);
		rpn.exec("undo");
		assertResultEq("20 5", rpn);
	}

	@Test
	public void example5() {
		rpn.exec("7 12 2 /");
		assertResultEq("7 6", rpn);
		rpn.exec("*");
		assertResultEq("42", rpn);
		rpn.exec("4 /");
		assertResultEq("10.5", rpn);
	}

	@Test
	public void example6() {
		rpn.exec("1 2 3 4 5");
		assertResultEq("1 2 3 4 5", rpn);
		rpn.exec("*");
		assertResultEq("1 2 3 20", rpn);
		rpn.exec("clear 3 4 -");
		assertResultEq("-1", rpn);
	}

	@Test
	public void example7() {
		rpn.exec("1 2 3 4 5");
		assertResultEq("1 2 3 4 5", rpn);
		rpn.exec("* * * *");
		assertResultEq("120", rpn);
	}

	@Test
	public void example8() {
		rpn.exec("1 2 3 * 5 + * * 6 5");
		assertResultEq("11", rpn);
	}

	@Test
	public void testHugeAmount() {
		rpn.exec("1024 1024 1024 * *");
		assertResultEq("1073741824", rpn);
	}

	@Test
	public void testTooMuchUndo() {
		rpn.exec("1 2 3").exec("undo undo").exec("undo");
		assertResultEq("1", rpn);
	}

	@Test
	public void testWrongInput() {
		rpn.exec("1 2 3 4 5+");
		assertResultEq("1 2 3 4", rpn);
	}

	@Test
	public void testNoParam4ErrorCode() {
		assertEquals("Operator {} (position: {}): insufficient parameters.",
				ErrorCode.PARAMETER_INSUFFICIENT.getMessage());
	}
}
