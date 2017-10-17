package com.kverchi.diary.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Liudmyla Melnychuk on 30.8.2017.
 */
public class PostSearchAttributes extends SearchAttributes {
    public enum PostSearchType {
        BY_USER_ID,
        BY_SIGHT_ID,
        BY_TEXT,
        IN_TITLE_ONLY
    };
    public enum PostFilterType {
        BY_WISHES,
        BY_VISITS
    };
    private Map<PostSearchType, Object> searchCriteria = new HashMap<>();
    private PostFilterType filterType;

    private int user_id;
    private int sight_id;
    private String text;

    public Map<PostSearchType, Object> getSearchCriteria() {
        return searchCriteria;
    }

    public PostFilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(PostFilterType filterType) {
        this.filterType = filterType;
    }

    public void setSearchCriteria(Map<PostSearchType, Object> searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getSight_id() {
        return sight_id;
    }

    public void setSight_id(int sight_id) {
        this.sight_id = sight_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
