package com.kverchi.diary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;


@RestController	
@RequestMapping("media")
public class MediaController {
	
	//TODO media service
	@Autowired
	PostService postService;
	
	@RequestMapping("/list")
	public ModelAndView showMedia() {
		List<Post> all_posts = postService.getAllPosts();
		ModelAndView mv = new ModelAndView("media");
		mv.addObject("all_posts", all_posts);
		return mv;
	}
}
