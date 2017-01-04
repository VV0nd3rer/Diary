package com.kverchi.diary.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.impl.PostServiceImpl;

@Controller
public class DiaryController {
	String message = "Welcome";
	PostService postService;
	@Autowired(required=true)
	@Qualifier(value="postService")
	public void setPostService(PostService postService) {
		this.postService = postService;
	}
	/*@RequestMapping("/main")
	public ModelAndView showMain(
			@RequestParam(value="name", required=false, defaultValue="Guest") String name) {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}*/
	@RequestMapping("/main")
    public String showMain(@RequestParam(value="name", required=false, defaultValue="Guest") String name, Model model){
        model.addAttribute("message", message);
        model.addAttribute("name", name);
        return "main";
    }
	@RequestMapping("/posts")
	public ModelAndView showPosts() {
		List<Post> all_posts = postService.getAllPosts();
		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("post", new Post());
		mv.addObject("all_posts", all_posts);
		return mv;
	}
	
	@RequestMapping(value="/post/add", method=RequestMethod.POST)
	public String addPost(@ModelAttribute("post") Post post) {
		if(post.getPost_id() == 0) {
			postService.addPost(post);
		}
		else {
			postService.updatePost(post);
		}
		return "redirect:/posts";
	}
	@RequestMapping("/posts/remove/{post_id}")
	public String removePost(@PathVariable("post_id") int post_id) {
		postService.deletePost(post_id);
		return "redirect:/posts";
	}
	@RequestMapping("/posts/edit/{post_id}")
    public String editPost(@PathVariable("post_id") int post_id, Model model){
        model.addAttribute("post", this.postService.getPostById(post_id));
        model.addAttribute("all_posts", this.postService.getAllPosts());
        return "posts";
    }
}
