package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.CountryService;
import com.kverchi.diary.service.PostService;

@RestController
public class PostController {
	final static Logger logger = Logger.getLogger(PostController.class);
	String message = "Welcome";
	@Autowired
	PostService postService;
	@Autowired
	CountryService countryService;
	/*@Autowired(required=true)
	@Qualifier(value="postService")
	public void setPostService(PostService postService) {
		this.postService = postService;
	}*/
	
	@RequestMapping("/test-me")
	public ModelAndView showTestPage() {
		ModelAndView mv = new ModelAndView("test_page");
		return mv;
	}
	@RequestMapping("/main")
	public ModelAndView showMain(
			@RequestParam(value="name", required=false, defaultValue="Guest") String name) {
		ModelAndView mv = new ModelAndView("main");
		mv.addObject("message", message);
		mv.addObject("name", name);
		return mv;
	}
	
	@RequestMapping("/map")
	public ModelAndView showTestBootstrapModal() {
		ModelAndView mv = new ModelAndView("map");
		return mv;
	}
	@RequestMapping("/country")
	public ModelAndView country(@RequestParam("country_code") String code) {
		Country country = countryService.getCountryById(code);
		Set<CountriesSight> country_sights = country.getCountriesSight();
		
		ModelAndView mv = new ModelAndView("country");
		mv.addObject("country", country);
		mv.addObject("country_sights", country_sights);
		return mv;
	}
	@RequestMapping("/sight_posts")
	public ModelAndView showSightPosts(@RequestParam("sight_id") int sight_id) {
		List<Post> sight_posts = null;
		sight_posts = postService.getSightPosts(sight_id);
		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("posts", sight_posts);
		return mv;
	}
	@RequestMapping("/posts")
	public ModelAndView showPosts() {
		List<Post> all_posts = postService.getAllPosts();
		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("post", new Post());
		mv.addObject("posts", all_posts);
		return mv;
	}
	@RequestMapping("/single-post/{post_id}")
	public ModelAndView showSinglePost(@PathVariable("post_id") int post_id) {
		ModelAndView mv = new ModelAndView("single-post");
		Post post = postService.getPostById(post_id);
		mv.addObject("post", post);
		return mv;
	}
	@RequestMapping(value = "/list-posts", /*method = RequestMethod.GET,*/ produces = "application/json")
	public List<Post> firstPost() {
		List<Post> all_posts = postService.getAllPosts();
		return all_posts;
	}
	@RequestMapping("/posts/edit/{post_id}")
    public Post editPost(@PathVariable("post_id") int post_id, Model model){
       Post post = postService.getPostById(post_id);
       return post;
    }
	@RequestMapping("/posts/remove/{post_id}")
	public String removePost(@PathVariable("post_id") int post_id) {
		postService.deletePost(post_id);
		String res = "OK";
		return res;
	}
	@RequestMapping(value="/add-post", method = RequestMethod.POST)
	public Post addPost(@RequestBody Post post) {
		Post added_post;
		logger.debug("post ID: " + post.getPost_id());
		if(post.getPost_id() == -1) {
		   added_post = postService.addPost(post);
		}
		else {
		   added_post = postService.updatePost(post);
		}
		logger.debug("added post: " + added_post.getTitle());
		return added_post;
	}
	
	@RequestMapping("/media")
	public ModelAndView showMedia() {
		List<Post> all_posts = postService.getAllPosts();
		ModelAndView mv = new ModelAndView("media");
		mv.addObject("all_posts", all_posts);
		return mv;
	}
	
	@RequestMapping(value="/new-post") 
	public ModelAndView newPost() {
		ModelAndView mv = new ModelAndView("new-post");
		mv.addObject("post", new Post());
		return mv;
	}
}
