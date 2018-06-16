package com.airwallex.common;

import org.apache.commons.lang3.StringUtils;

public enum ErrorCode {
	PARAMETER_INSUFFICIENT("Operator {} (position: {}): insufficient parameters."),
	WRONG_INPUT("The inputted element {} is neither number nor operator");

	private final String messagePattern;

	private ErrorCode(String messagePattern) {
		this.messagePattern = messagePattern;
	}

	public String getMessagePattern() {
		return messagePattern;
	}

	public String getMessage(Object... args) {
		if (StringUtils.isEmpty(this.getMessagePattern())) {
			return "";
		}
		if (args == null || args.length <= 0) {
			return this.getMessagePattern();
		}

		StringBuilder sb = new StringBuilder(this.getMessagePattern().length() + 50);
		int progress = 0;
		for (int i = 0; i < args.length; i++) {
			int j = messagePattern.indexOf(Constant.MSG_PLACE_HOLDER, progress);
			if (j < 0) {
				sb.append(messagePattern.substring(progress));
				return sb.toString();
			}
			sb.append(messagePattern.substring(progress, j)).append(args[i]);
			progress = j + Constant.MSG_PLACE_HOLDER.length();
		}
		sb.append(messagePattern.substring(progress));
		return sb.toString();
	}

}
