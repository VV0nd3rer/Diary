package com.kverchi.diary.domain;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.type.descriptor.java.ZonedDateTimeJavaDescriptor;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="posts")
public class Post {
	@Id
	@Column(name="post_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int post_id;
	private ZonedDateTime post_datetime;
	private String title;
	private String description;
	private String text;
	private Integer sight_id;
	@ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="user_id")
	private User user;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="post_id")
	private Set<Comment> post_comments;
	
	public Post() {};
	public Post(int post_id, String title, String text) {
		this.post_id = post_id;
		this.title = title;
		this.text = text;
	}
	public int getPost_id() {
		return post_id;
	}
	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}
	public ZonedDateTime getPost_datetime() {
		return post_datetime;
	}
	public void setPost_datetime(ZonedDateTime post_datetime) {
		this.post_datetime = post_datetime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSight_id() {
		return sight_id;
	}
	public void setSight_id(Integer sight_id) {
		this.sight_id = sight_id;
	}
	public Set<Comment> getPost_comments() {
		return post_comments;
	}
	public void setPost_comments(Set<Comment> post_comments) {
		this.post_comments = post_comments;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	@Override
	public String toString(){
		return "id="+post_id+", title="+title+", text="+text;
	}
	
}
