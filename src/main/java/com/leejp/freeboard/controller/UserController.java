package com.leejp.freeboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

	@GetMapping("/userForm.do") //회원가입 화면 이동
	public String form() {
		return "/user/userForm";
	}

	@GetMapping("/login.do") //로그인 화면 이동
	public String login() {
		return "/user/login";
	}

	@GetMapping("logout.do") //로그아웃
	public String logout() {
		return "redirect:/main.do";
	}
}
