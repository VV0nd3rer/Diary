package com.kverchi.diary.service.impl;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PaginationService;
import com.kverchi.diary.enums.PaginationContentHandler;

@Service
public class PaginationServiceImpl implements PaginationService {
	@Autowired
	private PostDao postDao;
	@Autowired
	private BookDao bookDao;
	
	@Override
	public Pagination getPaginatedPage(int page_index, PaginationContentHandler pagination_type, Map<String, Object> search_criteria) {
		SearchDao pageDao = null;
		
		switch (pagination_type) {
			case POSTS:
				pageDao = postDao;
				break;
			/*case "books":
				pageDao = bookDao;
				break;*/
			default:		
		}
		int num_posts_on_page = 5;
		int numOfPosts = pageDao.getNumOfRows(search_criteria);
		int numOfPages = numOfPosts/num_posts_on_page;
		if(numOfPosts % num_posts_on_page != 0) {
			numOfPages += 1;
		}
		int posts_row_offset = num_posts_on_page * page_index - num_posts_on_page;
	
		List<Post> pagePosts = pageDao.searchRows(search_criteria, num_posts_on_page, posts_row_offset);
		Pagination pagination = new Pagination(pagePosts, numOfPages);
		return pagination;
	}

}
