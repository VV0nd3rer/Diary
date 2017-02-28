package com.kverchi.diary.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.dao.impl.PostDaoImpl;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;

@Service
public class PostServiceImpl implements PostService {
	@Autowired
	private PostDao postDao;
	
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
	
	public Post addPost(Post post) {
		int added_id = (Integer)postDao.create(post);
		Post added_post = postDao.getById(added_id);
		return added_post;
	}

	public Post updatePost(Post post) {
		postDao.update(post);
		Post updated_post = postDao.getById(post.getPost_id());
		return updated_post;
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
