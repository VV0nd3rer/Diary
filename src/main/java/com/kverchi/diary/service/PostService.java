package com.kverchi.diary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Post;

public interface PostService {
	public List<Post> getAllPosts();
	public Post getPostById(int post_id);
	public Post addPost(Post post);
	public Post updatePost(Post post);
	public void deletePost(int post_id);
	public List<Post> getSightPosts(int sight_id);
}
