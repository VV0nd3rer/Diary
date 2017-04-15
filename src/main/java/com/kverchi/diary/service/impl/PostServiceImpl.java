package com.kverchi.diary.service.impl;

import java.time.ZonedDateTime;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.dao.impl.PostDaoImpl;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
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
	
	/*public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}*/
	
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
		Post postNeedToUpd = postDao.getById(post.getPost_id());
		CountriesSight updSightForPost = countriesSightService.getSightById(post.getSight().getSight_id());
		
		postNeedToUpd.setSight(updSightForPost);
		postNeedToUpd.setTitle(post.getTitle());
		postNeedToUpd.setDescription(post.getDescription());
		postNeedToUpd.setText(post.getText());
		
		boolean isPostUpdated = postDao.update(postNeedToUpd);
		if(isPostUpdated) {
			Post updated_post = postDao.getById(post.getPost_id());
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

}
