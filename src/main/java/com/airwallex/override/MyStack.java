package com.airwallex.override;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Stack;

import com.airwallex.common.Constant;

public class MyStack<E> extends Stack<E> {
	private static final long serialVersionUID = 6941628553522107289L;

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Object ele : this.elementData) {
			if (ele != null) {
				if (ele instanceof BigDecimal) {
					BigDecimal d = new BigDecimal(ele.toString());
					DecimalFormat format = new DecimalFormat("#.##########");
					sb.append(format.format(d.setScale(10, RoundingMode.DOWN)));
				} else {
					sb.append(ele.toString());
				}
				sb.append(Constant.PARAM_DELIMITER);
			}
		}
		if (sb.length() < 1) {
			return "";
		} else {
			return sb.deleteCharAt(sb.length() - 1).toString();
		}
	}
}
