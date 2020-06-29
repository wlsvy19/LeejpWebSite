package com.leejp.freeboard.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.leejp.freeboard.controller.repository.QuestionRepository;

@SpringBootApplication
@EnableAutoConfiguration
@EntityScan("com.leejp.freeboard")
@ComponentScan("com.leejp.freeboard")
@Controller
public class HomeController {
	@Autowired
	private QuestionRepository questionRepository;

	public static void main(String[] args) {
		SpringApplication.run(HomeController.class, args);
	}



	@GetMapping("/main")
	public String home(@PageableDefault Pageable pageable, Model model, String title) {
		int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1);
		pageable = new PageRequest(page, 5, new Sort(Sort.Direction.DESC, "id")); // 최근 게시물 순서로 sort

		model.addAttribute("boardList", questionRepository.findAll(pageable));

		//int firstPage = questionRepository.findAll(pageable).nextPageable().getPageNumber();
		int lastPage = questionRepository.findAll(pageable).getTotalPages();

		ArrayList<Integer> list = new ArrayList<>();
		for (int i = 2; i < lastPage; i++) {
			list.add(i);
		}

		model.addAttribute("list", list);
		model.addAttribute("firstPage", 1);
		model.addAttribute("lastPage", lastPage);

		return "index";
	}
}