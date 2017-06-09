package com.kverchi.diary.domain;

import java.util.List;
//context factory - id of name -> return concrete class
public class Pagination {
	private List pagePosts;
	private int pages_total_num;
	public Pagination(){}
	public Pagination(List pagePosts, int pages_total_num) {
		this.pagePosts = pagePosts;
		this.pages_total_num = pages_total_num;
	}
	public List<Post> getPagePosts() {
		return pagePosts;
	}
	public void setPagePosts(List<Post> pagePosts) {
		this.pagePosts = pagePosts;
	}
	public int getPages_total_num() {
		return pages_total_num;
	}
	public void setPages_total_num(int pages_total_num) {
		this.pages_total_num = pages_total_num;
	}
	
}
