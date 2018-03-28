package com.kverchi.diary.domain;

import java.util.List;

/**
 * Created by Liudmyla Melnychuk on 30.8.2017.
 */
public abstract class SearchResults<T> {
    private boolean isPaginated = true;
    private Pagination pagination = null;

    private List<T> results;

    public boolean isPaginated() {
        return isPaginated;
    }

    public void setPaginated(boolean paginated) {
        isPaginated = paginated;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

}
