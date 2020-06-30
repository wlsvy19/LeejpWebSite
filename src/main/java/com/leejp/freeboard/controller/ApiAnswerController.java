package com.leejp.freeboard.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.leejp.freeboard.controller.repository.AnswerRepository;
import com.leejp.freeboard.controller.repository.QuestionRepository;
import com.leejp.freeboard.domain.Answer;
import com.leejp.freeboard.domain.Question;
import com.leejp.freeboard.domain.User;
import com.leejp.freeboard.util.HttpSessionUtils;
import com.leejp.freeboard.util.Result;



@SpringBootApplication
@RestController // json data로 응답
@RequestMapping("/api/boards/{questionId}/answers") // 종속적
public class ApiAnswerController {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("") // 댓글 조회
    public List<Answer> list() {

        List<Answer> list = answerRepository.findAll();
        return list;
    }

    @PostMapping("") // 댓글 달기
    public Answer create(@PathVariable Long questionId, String contents, Model model, HttpSession session) {
        if (!HttpSessionUtils.isLoginUser(session)) { //로그인 유효성 검사
            return null;
        }

        User loginUser = HttpSessionUtils.getUserFromSession(session);
        Question question = questionRepository.findOne(questionId);
        Answer answer = new Answer(loginUser, question, contents);

        question.addAnswer(); //댓글수 +1
        return answerRepository.save(answer);
    }

    @DeleteMapping("/{id}") // 댓글 삭제
    public Result delete(@PathVariable Long questionId, @PathVariable Long id, HttpSession session) {
        System.out.println("in delete");
        if (!HttpSessionUtils.isLoginUser(session)) { //로그인 유효성 검사
            return Result.fail("로그인이 필요합니다.");
        }

        Answer answer = answerRepository.findOne(id);
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if (!answer.isSameWriter(loginUser)) {
            return Result.fail("자신의 글만 삭제할 수 있습니다.");
        }

        answerRepository.delete(id);

        Question question = questionRepository.findOne(questionId);
        question.deleteAnswer(); //댓글수 -1
        questionRepository.save(question); //댓글수 줄어든거 db반영
        return Result.ok();
    }

    @PutMapping("/{id}") //댓글 수정
    public Result update(@PathVariable Long questionId, @PathVariable Long id, String contents, HttpSession session) {
        System.out.println("in update");
        if(!HttpSessionUtils.isLoginUser(session)) {
            return Result.fail("로그인이 필요합니다.");
        }
        Answer answer = answerRepository.findOne(id);
        User loginUser = HttpSessionUtils.getUserFromSession(session);
        if(!answer.isSameWriter(loginUser)) {
            return Result.fail("자신의 글만 수정할 수 있습니다.");
        }
        answer.update(contents);
        answerRepository.save(answer);

        return Result.ok();
    }

}
