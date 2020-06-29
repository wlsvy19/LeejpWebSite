package com.leejp.freeboard.util;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class Result {
	private boolean valid;

	private String errorMessage;

	private Result(boolean valid, String errorMessage) {
		this.valid = valid;
		this.errorMessage = errorMessage;
	}

	public boolean isValid() {
		return valid;
	}

	public static Result ok() {
		return new Result(true, null);
	}

	public static Result fail(String errorMessage) {
		return new Result(false, errorMessage);
	}

	public static void message(String message, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(message);
        out.flush();
	}


}
