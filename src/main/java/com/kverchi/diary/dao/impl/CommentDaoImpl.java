package com.kverchi.diary.dao.impl;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.kverchi.diary.dao.CommentDao;
import com.kverchi.diary.domain.Comment;


@Repository
public class CommentDaoImpl extends GenericDaoImpl<Comment> implements CommentDao {
	
}
