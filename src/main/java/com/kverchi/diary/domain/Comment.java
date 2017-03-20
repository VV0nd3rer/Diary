package com.kverchi.diary.domain;

import java.time.ZonedDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


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
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int comment_id) {
		this.comment_id = comment_id;
	}
	public ZonedDateTime getComment_datetime() {
		return comment_datetime;
	}
	public void setComment_datetime(ZonedDateTime comment_datetime) {
		this.comment_datetime = comment_datetime;
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
	
}
