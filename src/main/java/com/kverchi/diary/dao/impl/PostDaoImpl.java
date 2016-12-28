package com.kverchi.diary.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Post;

public class PostDaoImpl implements PostDao {
	/*private SessionFactory sessionFactory; 
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}*/

	private List<Post> all_posts;
	public PostDaoImpl() {
		all_posts = new ArrayList<Post>();
		Post city = new Post(1, "NY", "Kerry knows everyting about... ");
		Post nature = new Post(2, "Forest", "It's so fun hiding in the forest.");
		all_posts.add(city);
		all_posts.add(nature);
	}
	public List<Post> getAllPosts() {
		return all_posts;
	}

	public Post getPost(int post_id) {
		Post res = null;
		for(Post p : all_posts) {
			if(p.getPost_id() == post_id) {
				res = p;
			}
		}
		return res;
	}

	public boolean updatePost(Post post) {
		int postIndex = -1;
		for(int i=0; i<all_posts.size(); i++) {
			Post iPost = all_posts.get(i);
			if(iPost.getPost_id() == post.getPost_id()) {
				postIndex = i;
				break;
			}
		}
		if(postIndex != -1) {
			all_posts.set(postIndex, post);
			return true;
		}
		else
			return false;
	}

	public boolean deletePost(int post_id) {
		int postIndex = -1;
		for(int i=0; i<all_posts.size(); i++) {
			Post iPost = all_posts.get(i);
			if(iPost.getPost_id() == post_id) {
				postIndex = i;
				break;
			}
		}
		if(postIndex != -1) {
			all_posts.remove(postIndex);
			return true;
		}
		else
			return false;
	}

}
