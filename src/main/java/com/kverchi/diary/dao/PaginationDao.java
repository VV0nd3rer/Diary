package com.kverchi.diary.dao;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.custom.exception.DatabaseException;

public interface PaginationDao {
    int getNumOfPosts(Map<String, Object> search_criteria) throws DatabaseException;
    List getLimitPosts(int limit, int offset, Map<String, Object> search_criteria) throws DatabaseException;
}
