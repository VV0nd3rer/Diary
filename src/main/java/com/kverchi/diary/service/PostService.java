package com.kverchi.diary.service;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.domain.*;
import org.springframework.stereotype.Service;

import com.kverchi.diary.custom.exception.DatabaseException;

public interface PostService {
	List<Post> getAllPosts();
	Post getPostById(int post_id);
	ServiceResponse addPost(Post post);
	ServiceResponse updatePost(Post post);
	void deletePost(int post_id);
	List<Post> getSightPosts(int sight_id);

	PostSearchResults search(PostSearchAttributes searchAttributes);
	int getPagesNum();
}
