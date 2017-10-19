package com.kverchi.diary.domain;

import com.kverchi.diary.enums.PaginationContentHandler;

import java.util.List;
import java.util.Map;

public class Pagination {

	private int totalPages = 1;
	private int offset;
	private int totalRows;
	private int pageSize = 5;
	private int currentPage = 1;

	public Pagination() {};
	public Pagination(int pageSize, int currentPage) {
		this.pageSize = pageSize;
		this.currentPage = currentPage;
	}
	public Pagination(int currentPage) {
		this.currentPage = currentPage;
	}
	private void calculatePagesAndOffset() {
		totalPages = totalRows/pageSize;
		if(totalRows % pageSize != 0) {
			totalPages += 1;
		}
		offset = pageSize * currentPage - pageSize;
		//if(totalPages > 0) {
		//pagination.setTotalPages(totalPages);
		//}
	}
	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
		calculatePagesAndOffset();
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
}
