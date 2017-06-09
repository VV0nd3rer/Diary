package com.kverchi.diary.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.service.PaginationService;

@RestController
//@RequestMapping("pagination")
public class PaginationController {
	@Autowired
	PaginationService paginationService;
	
	@RequestMapping("/pagination/{page_index}/{pagination_type}")
	public Pagination getPaginationRecord(@PathVariable("page_index") int page_index, @PathVariable("pagination_type") String pagination_type) {
		Pagination pagination = paginationService.getPaginatedPage(page_index, pagination_type);
		return pagination;
	}
}
