package com.kverchi.diary.dao;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Post;

public interface PostDao extends GenericDao<Post>, PaginationDao {
	List<Post> getSightPosts(int sight_id) throws DatabaseException;
}
