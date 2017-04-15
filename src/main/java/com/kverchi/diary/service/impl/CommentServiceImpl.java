package com.kverchi.diary.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.kverchi.diary.dao.CommentDao;
import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.dao.UserDao;
import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService {
	final static Logger logger = Logger.getLogger(CommentServiceImpl.class);
	@Autowired
	private UserDao userDao;
	@Autowired
	private CommentDao commentDao;
	@Autowired
	private PostDao postDao;
	@Override
	public ServiceResponse addComment(Comment comment) {
		
		ServiceResponse<Comment> response = 
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
		User loggedInUser = userDao.getUserByUsername(loggedInUserName);
		if(loggedInUser == null) {
			logger.debug("No user with username " + loggedInUserName + ". No created new post, return back...");
			response.setRespCode(HttpStatus.PRECONDITION_FAILED);
			response.setRespMsg(ServiceMessageResponse.NO_USER_WITH_USERNAME.toString());
			return response;
		}
		comment.setUser(loggedInUser);
		
		//int added_id = (Integer)commentDao.create(comment);
		Comment addedComment = (Comment)commentDao.persist(comment);
		if(addedComment == null) {
			logger.debug("Transaction problem.");
			response.setRespCode(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setRespMsg(ServiceMessageResponse.TRANSACTION_PROBLEM.toString());
			return response;
		}
		//Comment addedComment = commentDao.getById(added_id);
		response.setRespCode(HttpStatus.OK);
		response.setRespMsg(ServiceMessageResponse.OK.toString());
		response.setResponseObject(addedComment);
		return response;
	}

}
