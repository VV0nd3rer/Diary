package com.kverchi.diary.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.dao.impl.PostDaoImpl;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;

public class PostServiceImpl implements PostService {
	private PostDao postDao;
	
	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}
	@Transactional
	public List<Post> getAllPosts() {
		return postDao.getAllPosts();
		//murrr
	}
	@Transactional
	public Post getPostById(int post_id) {
		return postDao.getPostById(post_id);
	}
	@Transactional
	public Post addPost(Post post) {
		int added_id = postDao.addPost(post);
		Post added_post = postDao.getPostById(added_id);
		return added_post;
	}
	@Transactional
	public Post updatePost(Post post) {
		postDao.updatePost(post);
		Post updated_post = postDao.getPostById(post.getPost_id());
		return updated_post;
	}
	@Transactional
	public void deletePost(int post_id) {
	    postDao.deletePost(post_id);
	}

}
