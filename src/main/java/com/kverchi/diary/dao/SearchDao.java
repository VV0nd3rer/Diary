package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.SearchAttributes;

import java.util.List;
import java.util.Map;

public interface SearchDao {


    int getRowsNumberWithAttributes(Map <String, Object> hasAttributes, Map <String, String> includingAttributes,
                                    Map <String, Object> choosingAttributes);



    List searchWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                              Map<String, Object> choosingAttributes, Pagination pagination);
    List searchAndSortWithAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                                     Map<String, Object> choosingAttributes,
                                     String sortType, Pagination pagination);
}
