package com.kverchi.diary.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.BookDao;
import com.kverchi.diary.dao.GenericDao;
import com.kverchi.diary.dao.PaginationDao;
import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PaginationService;

@Service
public class PaginationServiceImpl implements PaginationService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private BookDao bookDao;
	
	@Override
	public Pagination getPaginatedPage(int page_index, String pagination_type, Map<String, Object> search_criteria) {
		PaginationDao pageDao = null;
		
		switch (pagination_type) {
			case "posts":
				pageDao = postDao;
				break;
			/*case "books":
				pageDao = bookDao;
				break;*/
			default:		
		}
		int num_posts_on_page = 5;
		int numOfPosts = pageDao.getNumOfPosts(search_criteria);
		int numOfPages = numOfPosts/num_posts_on_page;
		if(numOfPosts % num_posts_on_page != 0) {
			numOfPages += 1;
		}
		int posts_row_offset = num_posts_on_page * page_index - num_posts_on_page;
	
		List<Post> pagePosts = pageDao.getLimitPosts(num_posts_on_page, posts_row_offset, search_criteria);
		Pagination pagination = new Pagination(pagePosts, numOfPages);
		return pagination;
	}

}
