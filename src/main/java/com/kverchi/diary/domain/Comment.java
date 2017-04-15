package com.kverchi.diary.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name="comments")
public class Comment {
	@Id
	@Column(name="comment_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int comment_id;
	@Column(name="comment_datetime")
	private ZonedDateTime comment_datetime;
	private String text;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	/*@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="post_id")
	private Post post;*/
	private int post_id;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public ZonedDateTime getComment_datetime() {
		DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
		String text = comment_datetime.format(formatter);
		comment_datetime = ZonedDateTime.parse(text, formatter);
		return comment_datetime;
	}
	
	public void setComment_datetime(ZonedDateTime comment_datetime) {
		this.comment_datetime = comment_datetime;
	}
	@PrePersist
	public void setComment_datetime() {
		this.comment_datetime = ZonedDateTime.now();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	
	
}
