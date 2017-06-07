package com.kverchi.diary.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;

public interface PostService {
	List<Post> getAllPosts();
	Post getPostById(int post_id);
	ServiceResponse addPost(Post post);
	ServiceResponse updatePost(Post post);
	void deletePost(int post_id);
	List<Post> getSightPosts(int sight_id);
	Pagination getPostsPage(int page_index, int num_posts_on_page);
}
