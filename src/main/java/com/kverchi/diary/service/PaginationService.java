package com.kverchi.diary.service;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.enums.PaginationContentHandler;

public interface PaginationService {
	Pagination getPaginatedPage(int page_index, PaginationContentHandler pagination_type, Map<String, Object> search_criteria);
}
