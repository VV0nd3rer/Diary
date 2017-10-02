package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;

import javax.xml.crypto.Data;
import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 13.9.2017.
 */
public interface CounterDao<T> extends GenericDao<T> {
    int getCounterValue(int counter_obj_id) throws DatabaseException;
    boolean isValueExist(int counter_obj_id, int user_id) throws DatabaseException;
}
