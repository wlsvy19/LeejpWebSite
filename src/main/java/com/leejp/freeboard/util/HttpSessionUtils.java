package com.leejp.freeboard.util;

import javax.servlet.http.HttpSession;

import com.leejp.freeboard.domain.Question;
import com.leejp.freeboard.domain.User;

public class HttpSessionUtils {
	public static final String USER_SESSION_KEY = "sessionUser";

	public static boolean isLoginUser(HttpSession session) {
		Object sessionUser = session.getAttribute(USER_SESSION_KEY);
		if (sessionUser == null) {
			System.out.println("SessionUser is NULL");
			return false;
		}
		return true;
	}

	public static User getUserFromSession(HttpSession session) { //
		if (!isLoginUser(session)) {
			return null;
		}
		return (User) session.getAttribute(USER_SESSION_KEY);
	}

	public static Result valid(HttpSession session, Question question) {
		if (!HttpSessionUtils.isLoginUser(session)) {
			return Result.fail("로그인이 필요합니다.");
		}
		User loginUser = HttpSessionUtils.getUserFromSession(session);
		if (!question.isSameWriter(loginUser)) {
			return Result.fail("자신의 쓴 글만 수정, 삭제 가능 합니다.");
		}
		return Result.ok();
	}
}
