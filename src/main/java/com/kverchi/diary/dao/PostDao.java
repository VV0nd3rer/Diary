package com.kverchi.diary.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kverchi.diary.domain.Post;

public interface PostDao extends GenericDao<Post> {
	List<Post> getSightPosts(int sight_id);
	/*public List<Post> getAllPosts();
	public Post getPostById(int post_id);
	public int addPost(Post post);
	public void updatePost(Post post);
	public void deletePost(int post_id);*/
}
