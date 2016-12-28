package com.kverchi.diary.domain;

public class Post {
	private int post_id;
	private String title;
	private String text;
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
}
