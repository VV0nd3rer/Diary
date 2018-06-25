package com.kverchi.diary.service;

import java.util.List;
import com.kverchi.diary.model.Pagination;
import com.kverchi.diary.model.ServiceResponse;
import com.kverchi.diary.model.entity.Post;

public interface PostService {
	List<Post> getAllPosts();
	List<Post> getAllPosts(Pagination pagination);
	Post getPostById(int postId);
	ServiceResponse addPost(Post post);
	ServiceResponse updatePost(Post post);
	void deletePost(int post_id);
	List<Post> getSightPosts(int sightId);

}
