package com.kverchi.diary.service;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.enums.PaginationContentHandler;

public interface PaginationService {
	Pagination calculatePagination(Pagination pagination);
}
