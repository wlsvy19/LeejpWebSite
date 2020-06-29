package com.leejp.freeboard.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leejp.freeboard.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
}
