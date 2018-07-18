package com.kverchi.diary.service.impl;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.model.PostSearchRequest;
import com.kverchi.diary.model.entity.Post;
import com.kverchi.diary.model.ServiceResponse;
import com.kverchi.diary.model.enums.PostSearchCriteria;
import com.kverchi.diary.repository.PostRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.kverchi.diary.service.PostService;

import static com.kverchi.diary.repository.predicates.PostPredicates.*;

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
	public Page<Post> getAllPosts(int currentPage) {
		Pageable pageable = createPageableObject(currentPage);
		Page<Post> page =  postRepository.findAll(pageable);
		return page;
	}

	@Override
	public Page<Post> searchPosts(PostSearchRequest postSearchRequest) {
		Predicate predicate = searchPost(postSearchRequest.getSearchAttributes());
		Pageable pageable = createPageableObject(postSearchRequest.getCurrentPage());
		Page<Post> page = postRepository.findAll(predicate, pageable);
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
		return postRepository.findByCountriesSightSightId(sightId);
	}

	@Override
	public Page<Post> getSightPosts(Pageable pageable) {
		return null;
	}
	private Pageable createPageableObject(int currentPage) {
		return PageRequest.of(currentPage-1, 5);
	}
}
