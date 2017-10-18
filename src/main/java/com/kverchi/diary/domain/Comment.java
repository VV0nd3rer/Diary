package com.kverchi.diary.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;


@Entity
@Table(name="comments")
public class Comment {
	@Id
	@Column(name="comment_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int commentId;
	@Column(name="comment_datetime")
	private ZonedDateTime commentDatetime;
	private String text;
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	/*@ManyToOne(cascade=CascadeType.ALL)

	private Post post;*/
	@Column(name="post_id")
	private int postId;
	
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public ZonedDateTime getCommentDatetime() {
		DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
		String text = commentDatetime.format(formatter);
		commentDatetime = ZonedDateTime.parse(text, formatter);
		return commentDatetime;
	}
	
	public void setCommentDatetime(ZonedDateTime commentDatetime) {
		this.commentDatetime = commentDatetime;
	}
	@PrePersist
	public void setComment_datetime() {
		this.commentDatetime = ZonedDateTime.now();
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
	public int getPostId() {
		return postId;
	}
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	
}
