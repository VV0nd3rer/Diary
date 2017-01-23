package com.kverchi.diary.dao.impl;


import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.PostDao;
import com.kverchi.diary.domain.Post;

@Repository
public class PostDaoImpl extends GenericDaoImpl<Post> implements PostDao {
	/*@Autowired
	private SessionFactory sessionFactory; */
	
	final static Logger logger = Logger.getLogger(PostDaoImpl.class);
	
	
	/*public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}*/

	//private List<Post> all_posts;
	/*public PostDaoImpl() {
		all_posts = new ArrayList<Post>();
		Post city = new Post(1, "NY", "Kerry knows everyting about... ");
		Post nature = new Post(2, "Forest", "It's so fun hiding in the forest.");
		all_posts.add(city);
		all_posts.add(nature);
	}*/
	/*@Transactional
	public List<Post> getAllPosts() {
		Session session = this.sessionFactory.getCurrentSession();
		logger.debug("is session connected? : " + session.isConnected());
		List<Post> postsList = session.createQuery("from Post").list();
		logger.debug("posts list: " + postsList);
		return postsList;
	}
	@Transactional
	public Post getPostById(int post_id) {
		Session session = sessionFactory.getCurrentSession();
		Post post = (Post)session.get(Post.class, new Integer(post_id));
		logger.debug("Post loaded successfully, details="+post);
		return post;
	}
	@Transactional
	public int addPost(Post post) {
		Session session = sessionFactory.getCurrentSession();
		Integer id = (Integer)session.save(post);
		logger.debug("hibernate save returned id: " + id);
		return id;
	}
	@Transactional
	public void updatePost(Post post) {
		Session session = sessionFactory.getCurrentSession();
		session.update(post);
		logger.info("Post updated successfully, Post Details="+post);
	}
	@Transactional
	public void deletePost(int post_id) {
		Session session = sessionFactory.getCurrentSession();
		Post postToDel = (Post)session.load(Post.class, new Integer(post_id));
		if(postToDel != null) {
			session.delete(postToDel);
		}
	}*/

}
