package com.leejp.freeboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// http://localhost:8090/LeejpFreeBoard/index.do

@Controller
public class HomeController {
	@GetMapping("/index.do")
	public String home() {
		System.out.println("home controller");
		return "index";
	}
}
