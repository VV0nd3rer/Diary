package com.kverchi.diary.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.kverchi.diary.custom.exception.DatabaseException;

public interface GenericDao<T> {
	//Serializable create(final T t);
	T persist(T t) throws DatabaseException;
    T getById(final Serializable id) throws DatabaseException;
    boolean update(final T t) throws DatabaseException;   
    void delete(final T t) throws DatabaseException;
    List<T> getAllRecords() throws DatabaseException;
}
