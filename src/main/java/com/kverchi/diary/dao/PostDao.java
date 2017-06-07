package com.kverchi.diary.dao;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kverchi.diary.domain.Post;

public interface PostDao extends GenericDao<Post> {
	List<Post> getSightPosts(int sight_id);
	int getNumOfPosts();
	int getNumOfPosts(int sight_id);
	List<Post> getLimitPosts(int limit, int offset);
	/*public List<Post> getAllPosts();
	public Post getPostById(int post_id);
	public int addPost(Post post);
	public void updatePost(Post post);
	public void deletePost(int post_id);*/
}
