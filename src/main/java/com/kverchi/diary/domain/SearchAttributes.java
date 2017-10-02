package com.kverchi.diary.domain;

/**
 * Created by Liudmyla Melnychuk on 30.8.2017.
 */
public abstract class SearchAttributes {
    private boolean pagination = true;
    private int pageSize = 5;
    private int currentPage = 1;
    private String sortOrder = "DESC";

    public boolean isPagination() {
        return pagination;
    }

    public void setPagination(boolean pagination) {
        this.pagination = pagination;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
