package com.kverchi.diary.dao;

import java.util.List;
import java.util.Map;

public interface PaginationDao {
    
    int getNumOfPosts(Map<String, Object> search_criteria);
    List getLimitPosts(int limit, int offset, Map<String, Object> search_criteria);
}
