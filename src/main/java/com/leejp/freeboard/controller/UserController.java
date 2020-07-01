package com.leejp.freeboard.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.leejp.freeboard.controller.repository.UserRepository;
import com.leejp.freeboard.domain.User;
import com.leejp.freeboard.util.HttpSessionUtils;
import com.leejp.freeboard.util.Result;

@SpringBootApplication
@Controller
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/form") // 회원가입 화면 이동
	public String form() {
		System.out.println("user form");
		return "/user/form";
	}

	@PostMapping("/create") // 회원가입 완료
	public String create(User user, HttpServletResponse response) throws Exception {
		userRepository.save(user);
		//Result.message("<script>alert('회원가입이 완료되었습니다.');</script>", response);
		return "redirect:/main";
	}

	@GetMapping("/list") // 사용자 목록 조회
	public String list(Model model) {
		System.out.println("user list");
		model.addAttribute("users", userRepository.findAll());
		return "/user/list";
	}

	@GetMapping("/loginForm") // 로그인 화면 이동
	public String loginForm() {
		return "/user/login";
	}

	@PostMapping("/login") // 로그인
	public String login(String userId, String userPassword, HttpSession session, HttpServletResponse response) throws Exception {
		User user = userRepository.findByUserId(userId);

		if (user == null) { // 사용자 id가 없는 경우
			Result.message("<script>alert('아이디를 확인해주세요.');</script>", response);
			return "/user/login";
		}

		if (!user.matchPassword(userPassword)) {// 패스워드가 일치하지 않는경우
			Result.message("<script>alert('비밀번호를 확인해주세요.');</script>", response);
			return "/user/login";
		}

		session.setAttribute(HttpSessionUtils.USER_SESSION_KEY, user); // 세션 설정
		return "redirect:/main";
	}

	@GetMapping("/logout") // 로그아웃
	public String logout(HttpSession session, HttpServletResponse response) throws Exception {
		session.removeAttribute(HttpSessionUtils.USER_SESSION_KEY);
		Result.message("<script>alert('로그아웃 되었습니다.');</script>", response);
		return "/main";
	}

	@GetMapping("/{id}/form") // 개인정보 수정 화면 이동
	public String updateForm(@PathVariable Long id, Model model, HttpSession session, HttpServletResponse response) throws Exception {
		User sessionedUser = HttpSessionUtils.getUserFromSession(session);
		if (!HttpSessionUtils.isLoginUser(session)) { // 로그인 여부
			Result.message("<script>alert('로그인이 필요합니다.');</script>", response);
			return "/user/login";
		}
		if (!id.equals(sessionedUser.getId())) {
			Result.message("<script>alert('다른 사용자의 정보는 수정할 수 없습니다.'); history.go(-1);</script>", response);
		}

		User user = userRepository.getOne(id);
		model.addAttribute("user", user);
		return "/user/updateForm";
	}

	@PutMapping("/{id}") // 개인정보 수정 완료
	public String update(@PathVariable Long id, User updatedUser, HttpSession session) {
		User user = userRepository.getOne(id);
		user.update(updatedUser);
		userRepository.save(user);
		return "redirect:/users/list";
	}

}
