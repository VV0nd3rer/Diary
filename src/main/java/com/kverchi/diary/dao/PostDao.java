package com.kverchi.diary.dao;
import java.util.List;

import com.kverchi.diary.domain.Post;

public interface PostDao {
	public List<Post> getAllPosts();
	public Post getPost(int post_id);
	public boolean updatePost(Post post);
	public boolean deletePost(int post_id);
}
