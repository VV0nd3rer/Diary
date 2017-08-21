package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;

import java.util.List;
import java.util.Map;

public interface SearchDao {
    int getNumOfRows(Map<String, Object> search_criteria) throws DatabaseException;
    int getNumOfRows() throws DatabaseException;
    List searchRows(Map<String, Object> search_criteria, int limit, int offset) throws DatabaseException;
    List getLimitRows(int limit, int offset) throws DatabaseException;
}
