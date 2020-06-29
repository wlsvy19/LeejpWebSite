package com.leejp.freeboard.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Answer {

    @Id
    @GeneratedValue
    @JsonProperty
    private Long id;

    @ManyToOne // 한 사람이 많은 글
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_writer"))
    @JsonProperty
    private User writer;

    @ManyToOne // 한사람이 많은 댓글
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_answer_question")) // Question에 종속적
    @JsonProperty
    private Question question;

    @Lob // varchar 255 over
    @JsonProperty
    private String contents;

    private LocalDateTime createDate;

    private boolean valid;
    private String errorMessage;

    public Answer() {
    }

    public Answer(User writer, Question question, String contents) {
        this.writer = writer;
        this.question = question;
        this.contents = contents;
        this.createDate = LocalDateTime.now();
    }

    public boolean isSameWriter(User loginUser) {
        return loginUser.getId().equals(this.writer.getId());
    }

    public String getFormattedCreateDate() {
        if (createDate == null) {
            return "";
        }
        return createDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    }

    public Answer(boolean valid, String errorMessage) {
       this.valid = valid;
       this.errorMessage = errorMessage;
    }

    public static Answer fail(String errorMessage) {
        return new Answer(false, errorMessage);
    }

    public static Answer ok() {
        return new Answer(true, null);
    }

    public void update(String contents) {
        this.contents = contents;
    }

}
