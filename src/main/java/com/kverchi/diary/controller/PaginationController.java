package com.kverchi.diary.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.service.PaginationService;

@RestController
//@RequestMapping("pagination")
public class PaginationController {
	@Autowired
	PaginationService paginationService;
	
	@RequestMapping(value = "/pagination", method = RequestMethod.POST)
	public Pagination getPaginationRecord(@RequestBody Pagination pag_req) {
		Pagination pagination = paginationService.getPaginatedPage(pag_req.getPage_index(), pag_req.getPagination_type(), pag_req.getSearch_criteria());
		return pagination;
	}
	
	
}
