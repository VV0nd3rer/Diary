package com.kverchi.diary.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kverchi.diary.model.*;
import com.kverchi.diary.model.entity.Post;

import com.kverchi.diary.repository.PostRepository;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.kverchi.diary.model.enums.ServiceMessageResponse;
import com.kverchi.diary.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	final static Logger logger = LogManager.getLogger(PostServiceImpl.class);

	@Autowired
	PostRepository postRepository;

	@Override
	public List<Post> getAllPosts() {
		return postRepository.findAll();
	}

	@Override
	public Page<Post> getAllPosts(Pageable pageable) {
		Page<Post> page =  postRepository.findAll(pageable);
		return page;
	}

	@Override
	public Post getPostById(int postId) {
		return null;
	}

	@Override
	public ServiceResponse addPost(Post post) {
		return null;
	}

	@Override
	public ServiceResponse updatePost(Post post) {
		return null;
	}

	@Override
	public void deletePost(int post_id) {

	}

	@Override
	public List<Post> getSightPosts(int sightId) {
		return null;
	}
}
