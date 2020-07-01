package com.leejp.freeboard.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;


@Entity
@Table(name="web_question")
public class Question {
	@Id
	@GeneratedValue
	@JsonProperty
	private Long id;

	@ManyToOne // 한 사람이 많은 글
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	@JsonProperty
	private User writer;

	@JsonProperty
	private String title;

	@Lob // varchar 255 over
	@JsonProperty
	private String contents;

	@JsonProperty
	private Integer countOfAnswer = 0; // 댓글 수 초기화

	private LocalDateTime createDate;

	@OneToMany(mappedBy = "question")
	@OrderBy("id DESC") // 답변이 ID를기준으로 오름차순으로 댓글이 달림
	private List<Answer> answers;

	public Question() {
	}

	public Question(User writer, String title, String contents) {
		super();
		this.writer = writer;
		this.title = title;
		this.contents = contents;
		this.createDate = LocalDateTime.now();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFormattedCreateDate() {
		if (createDate == null) {
			return "";
		}
		return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}

	public boolean isSameWriter(User loginUser) {
		return this.writer.getId().equals(loginUser.getId());
	}

	public void addAnswer() {
		this.countOfAnswer += 1;
	}

	public void deleteAnswer() {
		this.countOfAnswer -= 1;
	}

}
