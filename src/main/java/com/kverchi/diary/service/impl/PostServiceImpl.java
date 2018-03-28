package com.kverchi.diary.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kverchi.diary.domain.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.UserService;

@Service
public class PostServiceImpl implements PostService {
	final static Logger logger = Logger.getLogger(PostServiceImpl.class);
	
	@Autowired
	private PostDao postDao;
	@Autowired
	private UserService userService;
	@Autowired
	private CountriesSightService countriesSightService;
	
	public List<Post> getAllPosts() {
		return postDao.getAllRecords();
		//murrr
	}
	public Post getPostById(int post_id) {
		return postDao.getById(post_id);
	}
	
	public ServiceResponse addPost(Post post) {
		ServiceResponse response = 
				new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
		
		//Get current user id
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String loggedInUserName = auth.getName();
		if(loggedInUserName.equals("anonymousUser")) {
			logger.debug("It's anonymousUser. No created new post, return back...");
			response.setRespCode(HttpStatus.PRECONDITION_FAILED);
			response.setRespMsg(ServiceMessageResponse.NO_USER_WITH_USERNAME.toString());
			return response;
		}
		User loggedInUser = userService.getUserByUsername(loggedInUserName);
		if(loggedInUser == null) {
			logger.debug("No user with username " + loggedInUserName + ". No created new post, return back...");
			response.setRespCode(HttpStatus.PRECONDITION_FAILED);
			response.setRespMsg(ServiceMessageResponse.NO_USER_WITH_USERNAME.toString());
			return response;
		}
		post.setUser(loggedInUser);
		
		//int added_id = (Integer)postDao.create(post);
		Post added_post = (Post)postDao.persist(post);
		//Post added_post = postDao.getById(added_id);
		if(added_post == null) {
			logger.debug("Transaction problem.");
			response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
			return response;
		}
		response.setRespCode(HttpStatus.OK);
		response.setRespMsg(ServiceMessageResponse.OK.toString());
		return response;
	}

	public ServiceResponse updatePost(Post post) {
		ServiceResponse response = 
				new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
		//TODO 
		//Here is lazy load:
		//List of posts - no user loaded
		//Single post - user loaded
		//How is it better to develop this thing?
		Post postNeedToUpd = postDao.getById(post.getPostId());
		//Check if user is owner of this post
		User currentUser = userService.getUserFromSession();
		String postAuth = postNeedToUpd.getUser().getUsername();
		boolean isAuthor = currentUser.getUsername().equals(postAuth);
		if(!isAuthor) {
			return response;
		}
		//

		postNeedToUpd.setTitle(post.getTitle());
		postNeedToUpd.setDescription(post.getDescription());
		postNeedToUpd.setText(post.getText());
		
		boolean isPostUpdated = postDao.update(postNeedToUpd);
		if(isPostUpdated) {
			response.setRespCode(HttpStatus.OK);
			response.setRespMsg(ServiceMessageResponse.OK.toString());
			return response;
		}
		else {
			response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
			return response;
		}
	}
	
	public void deletePost(int post_id) {
		Post postToDel = postDao.getById(post_id);
	    postDao.delete(postToDel);
	}
	@Override
	public List<Post> getSightPosts(int sight_id) {
		  List<Post> sightPosts = postDao.getSightPosts(sight_id);
		  return sightPosts;
	}

	@Override
	public PostSearchResults search(PostSearchAttributes searchAttributes) {
		PostSearchResults searchResults = new PostSearchResults();
		//Pagination pagination = new Pagination(searchAttributes.getPageSize(), searchAttributes.getCurrentPage());
		Pagination pagination = searchAttributes.getPagination();
		Map<PostSearchAttributes.PostSearchType, Object> searchCriteria = searchAttributes.getSearchCriteria();

		Map<String, Object> hasAttributes = new HashMap<>();
		Map<String, String> includingAttributes = new HashMap<>();
		Map<String, Object> choosingAttributes = new HashMap<>();
		if (searchCriteria != null && !searchCriteria.isEmpty()) {
			for (Map.Entry<PostSearchAttributes.PostSearchType, Object> entry : searchCriteria.entrySet()) {
				switch (entry.getKey()) {
					case BY_USER_ID:
						hasAttributes.put("userId", entry.getValue());
						break;
					case BY_SIGHT_ID:
						hasAttributes.put("sightId", entry.getValue());
						break;
					case BY_TEXT:
						choosingAttributes.put("description", entry.getValue());
						choosingAttributes.put("text", entry.getValue());
						choosingAttributes.put("title", entry.getValue());
						break;
					case IN_TITLE_ONLY:
						includingAttributes.put("title", entry.getValue().toString());
						break;
				}
			}
		}

		int totalRows;
		totalRows = postDao.getRowsNumberWithAttributes(hasAttributes, includingAttributes, choosingAttributes);

		pagination.setTotalRows(totalRows);
		//searchResults.setTotalPages(pagination.getTotalPages());
		searchResults.setPagination(pagination);
		List results;
		PostSearchAttributes.PostSortType sortType = searchAttributes.getPostSortType();
		logger.debug("sortType: " + sortType);

		if(sortType != null) {
			results = postDao.searchAndSortWithAttributes(hasAttributes, includingAttributes,
					choosingAttributes, sortType.getSortType(), pagination);
		} else {
			results = postDao.searchWithAttributes(hasAttributes, includingAttributes, choosingAttributes,
					pagination);
		}

		searchResults.setResults(results);
		return searchResults;
	}
}
