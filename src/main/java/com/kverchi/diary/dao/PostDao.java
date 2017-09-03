package com.kverchi.diary.dao;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Post;

public interface PostDao extends GenericDao<Post>, SearchDao {
	List<Post> getSightPosts(int sight_id) throws DatabaseException;
	int getTestRowsNum(Map<String, String> containsAttributes) throws DatabaseException;
}
