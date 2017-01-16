package com.kverchi.diary.dao;
import java.util.List;

import com.kverchi.diary.domain.Post;

public interface PostDao {
	public List<Post> getAllPosts();
	public Post getPostById(int post_id);
	public int addPost(Post post);
	public void updatePost(Post post);
	public void deletePost(int post_id);
}
