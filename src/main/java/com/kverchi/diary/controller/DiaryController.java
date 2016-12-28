package com.kverchi.diary.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.impl.PostServiceImpl;

@Controller
public class DiaryController {
	String message = "Welcome";
	PostService postService = new PostServiceImpl();
	@RequestMapping("/main")
	public ModelAndView showMain(
			@RequestParam(value="name", required=false, defaultValue="Guest") String name) {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}
	@RequestMapping("/posts")
	public ModelAndView showPosts() {
		List<Post> all_posts = postService.getAllPosts();
		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("all_posts", all_posts);
		return mv;
	}
}
