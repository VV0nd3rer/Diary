package com.kverchi.diary.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kverchi on 30.8.2017.
 */
public class BookSearchAttributes extends SearchAttributes {
    public enum BookSearchType {
        BY_AUTHOR_ID,
        BY_TEXT
    };
    private Map<BookSearchType, Object> searchCriteria = new HashMap<>();
    private int author_id;
    private String text;

    public Map<BookSearchType, Object> getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(Map<BookSearchType, Object> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
