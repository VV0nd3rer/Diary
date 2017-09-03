package com.kverchi.diary.service.impl;

import java.util.List;
import java.util.Map;

import com.kverchi.diary.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PaginationService;
import com.kverchi.diary.enums.PaginationContentHandler;

@Service
public class PaginationServiceImpl implements PaginationService {

	@Override
	public Pagination calculatePagination(Pagination pagination) {
		int totalPages = pagination.getTotalRows()/pagination.getPageSize();
		if(pagination.getTotalRows() % pagination.getPageSize() != 0) {
			totalPages += 1;
		}
		int offset = pagination.getPageSize() * pagination.getCurrentPage() - pagination.getPageSize();
		if(totalPages > 0) {
			pagination.setTotalPages(totalPages);
		}
		pagination.setOffset(offset);
		return pagination;
	}
}
