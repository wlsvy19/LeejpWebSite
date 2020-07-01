package com.leejp.freeboard.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.leejp.freeboard.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //User DB CRUD, Long은 User의 pk
	User findByUserId(String userId); // select~from~where query
}
