package com.kverchi.diary.service.impl;

import java.util.List;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.dao.impl.PostDaoImpl;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;

public class PostServiceImpl implements PostService {
	/*private PostDao postDao;
	
	public void setPostDao(PostDao postDao) {
		this.postDao = postDao;
	}*/
	private PostDao postDao = new PostDaoImpl();
	public List<Post> getAllPosts() {
		return postDao.getAllPosts();
	}

	public Post getPost(int post_id) {
		return postDao.getPost(post_id);
	}

	public boolean updatePost(Post post) {
		return postDao.updatePost(post);
	}

	public boolean deletePost(int post_id) {
		return postDao.deletePost(post_id);
	}

}
