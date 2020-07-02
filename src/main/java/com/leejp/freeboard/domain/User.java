package com.leejp.freeboard.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "web_user")
public class User {
	@Id // primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto increase +1
	@JsonProperty // getter를쓰지 않고 사용자정보를 json으로 가져옴
	private Long id;

	@Column(nullable = false, length = 20, unique = true) // not null
	@JsonProperty
	private String userId;

	@JsonIgnore
	private String userPassword;

	@JsonProperty
	private String userName;

	@JsonProperty
	private String userEmail;

	public Long getId() {
		return id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public boolean matchPassword(String newPassword) { // 객체에서 꺼내어비교 x
		if (newPassword == null) {
			return false;
		}

		return newPassword.equals(userPassword);
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public void update(User modifyUser) {
		this.userPassword = modifyUser.userPassword;
		this.userName = modifyUser.userName;
		this.userEmail = modifyUser.userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setId(Long id) {
		this.id = id;
	}



}
