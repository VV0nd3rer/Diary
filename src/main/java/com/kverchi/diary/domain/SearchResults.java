package com.kverchi.diary.domain;

import java.util.List;

/**
 * Created by Kverchi on 30.8.2017.
 */
public abstract class SearchResults<T> {
    private int totalPages = 5;
    private List<T> results;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}
