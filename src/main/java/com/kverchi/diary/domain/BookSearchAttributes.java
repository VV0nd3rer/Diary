package com.kverchi.diary.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liudmyla Melnychuk on 30.8.2017.
 */
public class BookSearchAttributes extends SearchAttributes {
    public enum BookSearchType {
        BY_AUTHOR_ID,
        BY_TEXT,
        IN_TITLE_ONLY
    };
    private Map<BookSearchType, Object> searchCriteria = new HashMap<>();
    private int authorId;
    private String text;

    public Map<BookSearchType, Object> getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(Map<BookSearchType, Object> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
