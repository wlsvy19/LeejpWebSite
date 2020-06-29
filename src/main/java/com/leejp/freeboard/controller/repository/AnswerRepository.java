package com.leejp.freeboard.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.leejp.freeboard.domain.Answer;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
