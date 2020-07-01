package com.leejp.freeboard.controller;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.leejp.freeboard.controller.repository.QuestionRepository;
import com.leejp.freeboard.domain.Question;
import com.leejp.freeboard.domain.User;
import com.leejp.freeboard.util.HttpSessionUtils;
import com.leejp.freeboard.util.Result;

@SpringBootApplication
@Controller
@RequestMapping("/boards")
public class QuestionController {
	@Autowired
	private QuestionRepository questionRepository;

	@GetMapping("/form") // 글쓰기 폼 이동
	public String question(HttpSession session, HttpServletResponse response) throws Exception {
		if (!HttpSessionUtils.isLoginUser(session)) { // 로그인 여부
			Result.message("<script>alert('로그인이 필요합니다.');</script>", response);
			return "/user/login";
		}

		return "/board/form";
	}

	@PostMapping("/questions") // 질문 등록
	public String create(String title, String contents, HttpSession session, Model model) throws Exception {
		User sessionUser = HttpSessionUtils.getUserFromSession(session);
		Question newQuestion = new Question(sessionUser, title, contents);
		questionRepository.save(newQuestion);
		return "redirect:/main";
	}

	@GetMapping("/{id}") // ID에 해당하는 게시글 내용
	public String show(@PathVariable Long id, Model model) {
		model.addAttribute("question", questionRepository.getOne(id));
		return "/board/show";
	}

	@GetMapping("/{id}/form") // ID에 해당하는 수정 폼 이동
	public String updateForm(@PathVariable Long id, Model model, HttpSession session, HttpServletResponse response)
			throws Exception {
		Question question = questionRepository.getOne(id);
		Result result = HttpSessionUtils.valid(session, question);
		if (!result.isValid()) {
			Result.message("<script>alert('자신이 쓴 글만 수정할 수 있습니다.'); history.go(-1);</script>", response);
		}
		model.addAttribute("question", question);
		return "/board/updateForm";
	}

	@PutMapping("/{id}") // ID에 해당하는 글 수정
	public String update(@PathVariable Long id, String title, String contents, Model model, HttpSession session,
			HttpServletResponse response) throws Exception {
		Question question = questionRepository.getOne(id);
		question.update(title, contents);
		questionRepository.save(question);
		return String.format("redirect:/boards/%d", id);
	}

	@DeleteMapping("/{id}") // ID에 해당하는 글 삭제
	public String delete(@PathVariable Long id, Model model, HttpSession session, HttpServletResponse response) throws Exception {
		Question question = questionRepository.getOne(id);
		Result result = HttpSessionUtils.valid(session, question);
		if (!result.isValid()) {
			Result.message("<script>alert('자신이 쓴 글만 수정할 수 있습니다.'); history.go(-1);</script>", response);
			return "/user/login";
		}

		questionRepository.deleteById(id);
		return "redirect:/main";
	}

}
