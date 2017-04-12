package com.kverchi.diary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;

public interface PostService {
	public List<Post> getAllPosts();
	public Post getPostById(int post_id);
	public ServiceResponse addPost(Post post);
	public ServiceResponse updatePost(Post post);
	public void deletePost(int post_id);
	public List<Post> getSightPosts(int sight_id);
}
