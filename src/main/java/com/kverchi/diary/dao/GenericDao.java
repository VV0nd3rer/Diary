package com.kverchi.diary.dao;

import java.io.Serializable;
import java.util.List;

import org.springframework.stereotype.Repository;

public interface GenericDao<T> {
	Serializable create(final T t);
    T getById(final Serializable id);
    boolean update(final T t);   
    void delete(final T t);
    List<T> getAllRecords();
}
