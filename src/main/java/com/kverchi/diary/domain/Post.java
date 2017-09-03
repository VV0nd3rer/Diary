package com.kverchi.diary.domain;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.type.descriptor.java.ZonedDateTimeJavaDescriptor;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="posts")
//http://www.greggbolinger.com/ignoring-hibernate-garbage-via-jsonignoreproperties/
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Post {
	@Id
	@Column(name="post_id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int post_id;
	private ZonedDateTime post_datetime;
	private String title;
	private String description;
	@Column(name="text")
	private String text;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="sight_id")
	private CountriesSight countriesSight;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User user;
	@OneToMany(mappedBy="post_id", fetch=FetchType.LAZY, orphanRemoval=true)
	//@JoinColumn(name="post_id")
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
		DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;
		String text = post_datetime.format(formatter);
		post_datetime = ZonedDateTime.parse(text, formatter);
		return post_datetime;
	}
	public void setPost_datetime(ZonedDateTime post_datetime) {
		this.post_datetime = post_datetime;
	}
	@PrePersist
	@PreUpdate
	public void setPost_datetime() {
		this.post_datetime = ZonedDateTime.now();
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
	public CountriesSight getCountriesSight() {
		return countriesSight;
	}

	public void setCountriesSight(CountriesSight countriesSight) {
		this.countriesSight = countriesSight;
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
