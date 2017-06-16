package com.kverchi.diary.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.service.PaginationService;
import com.kverchi.diary.service.PostService;

@RestController
//@RequestMapping("pagination")
public class PaginationController {
	@Autowired
	PaginationService paginationService;
	@Autowired
	PostService postService;
	
	@RequestMapping(value = "/pagination", method = RequestMethod.POST)
	public ModelAndView getPagRecord(@RequestBody Pagination pag_req) {
		Pagination pagination = paginationService.getPaginatedPage(pag_req.getPage_index(), pag_req.getPagination_type(), pag_req.getSearch_criteria());
		ModelAndView mv = new ModelAndView("fragments :: page");
		mv.addObject("posts", pagination.getPagePosts());
		mv.addObject("pages_total_num", pagination.getPages_total_num());
		return mv;
	}

}
