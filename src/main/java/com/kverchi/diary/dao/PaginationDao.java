package com.kverchi.diary.dao;

import java.util.List;

public interface PaginationDao {
    
    int getNumOfPosts();
    List getLimitPosts(int limit, int offset);
}
