package com.kverchi.diary.dao;

import com.kverchi.diary.custom.exception.DatabaseException;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.SearchAttributes;

import java.util.List;
import java.util.Map;

public interface SearchDao {
    /*int getRowsNumber(Map <String, Object> hasAttributes, Map<String, String> containsAttributes);
    List search(Map<String, Object> hasAttributes, Map<String, String> containsAttributes, Pagination pagination);*/

    int getRowsNumberWithExactAttributesOnly(Map <String, Object> hasAttributes);
    int getRowsNumberWithStringAttributes(Map <String, Object> hasAttributes, Map <String, String> includingAttributes);
    int getRowsNumberWithStringAttributes(Map <String, Object> hasAttributes, Map <String, String> includingAttributes,
                                          Map <String, String> choosingAttributes);

    List searchExactAttributesOnly(Map<String, Object> hasAttributes, Pagination pagination);
    List searchWithStringAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                                    Pagination pagination);
    List searchWithStringAttributes(Map<String, Object> hasAttributes, Map<String, String> includingAttributes,
                                    Map<String, String> choosingAttributes, Pagination pagination);
}
