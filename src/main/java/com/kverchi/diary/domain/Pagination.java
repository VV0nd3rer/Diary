package com.kverchi.diary.domain;

import java.util.List;
import java.util.Map;
//context factory - id of name -> return concrete class
public class Pagination {
	private List pagePosts;
	private int pages_total_num;
	private Map<String, Object> search_criteria;
	private int page_index;
	private String pagination_type;
	
	
	public Pagination(){}
	public Pagination(List pagePosts, int pages_total_num) {
		this.pagePosts = pagePosts;
		this.pages_total_num = pages_total_num;
	}
	public List<Post> getPagePosts() {
		return pagePosts;
	}
	public void setPagePosts(List pagePosts) {
		this.pagePosts = pagePosts;
	}
	public int getPages_total_num() {
		return pages_total_num;
	}
	public void setPages_total_num(int pages_total_num) {
		this.pages_total_num = pages_total_num;
	}
	public Map<String, Object> getSearch_criteria() {
		return search_criteria;
	}
	public void setSearch_criteria(Map<String, Object> search_criteria) {
		this.search_criteria = search_criteria;
	}
	public int getPage_index() {
		return page_index;
	}
	public void setPage_index(int page_index) {
		this.page_index = page_index;
	}
	public String getPagination_type() {
		return pagination_type;
	}
	public void setPagination_type(String pagination_type) {
		this.pagination_type = pagination_type;
	}
}
