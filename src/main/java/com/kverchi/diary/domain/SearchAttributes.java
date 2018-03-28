package com.kverchi.diary.domain;

/**
 * Created by Liudmyla Melnychuk on 30.8.2017.
 */
public abstract class SearchAttributes {
    private boolean isPaginated = true;
    private Pagination pagination = null;

    private String sortOrder = "DESC";

    public boolean isPaginated() {
        return isPaginated;
    }

    public void setPaginated(boolean paginated) {
        this.isPaginated = paginated;
    }

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
