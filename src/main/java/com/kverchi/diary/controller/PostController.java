package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.service.CommentService;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.CountryService;
import com.kverchi.diary.service.PostService;

@RestController
@RequestMapping("posts")
public class PostController {
	final static Logger logger = Logger.getLogger(PostController.class);
	String message = "Welcome";
	@Autowired
	PostService postService;
	@Autowired 
	CommentService commentService;
	@Autowired
	CountryService countryService;
	@Autowired
	CountriesSightService countriesSightService;
	
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
	
	@RequestMapping("/list")
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
		Set<Comment> comments = post.getPost_comments();
		CountriesSight sight =  countriesSightService.getSightById(post.getSight_id()); //post.getSight();
		mv.addObject("post", post);
		mv.addObject("sight", sight);
		mv.addObject("comments", comments);
		
		return mv;
	}
	@RequestMapping(value = "/list-posts", /*method = RequestMethod.GET,*/ produces = "application/json")
	public List<Post> firstPost() {
		List<Post> all_posts = postService.getAllPosts();
		return all_posts;
	}
	@RequestMapping("/edit/{post_id}")
    public ModelAndView editPost(@PathVariable("post_id") int post_id, Model model){
       Post post = postService.getPostById(post_id);
       List<CountriesSight> sightList = null;
       
       String country_code = null;
	   country_code = getSessionAttribute("country_code");
	   if(country_code != null) {
		   sightList = countriesSightService.getCountrySights(country_code);
	   }
	   CountriesSight sight =  countriesSightService.getSightById(post_id);
       ModelAndView mv = new ModelAndView("new-post");
       mv.addObject("post", post);
       mv.addObject("sight", sight);
       mv.addObject("sightList", sightList);
       return mv;
    }
	@RequestMapping("/remove/{post_id}")
	public String removePost(@PathVariable("post_id") int post_id) {
		postService.deletePost(post_id);
		String res = "OK";
		return res;
	}
	@RequestMapping(value="/add-post", method = RequestMethod.POST)
	public ServiceResponse addPost(@RequestBody Post post) {
		ServiceResponse response = new ServiceResponse();
		logger.debug("post ID: " + post.getPost_id());
		
		if(post.getPost_id() == 0) {
			response = postService.addPost(post);
		}
		else {
		   response = postService.updatePost(post);
		}
		return response;
	}
	
	
	
	@RequestMapping(value="/new-post") 
	public ModelAndView newPost() {
		String country_code = null;
		country_code = getSessionAttribute("country_code");
		List<CountriesSight> sightList = null;
		if(country_code != null) {
			   sightList = countriesSightService.getCountrySights(country_code);
		}
		ModelAndView mv = new ModelAndView("new-post");
		mv.addObject("post", new Post());
		mv.addObject("sightList", sightList);
		return mv;
	}
	@RequestMapping(value="/add-comment", method = RequestMethod.POST)
	public ServiceResponse addComment(@RequestBody Comment comment) {
		ServiceResponse response = new ServiceResponse<Comment>();
		response = commentService.addComment(comment);
		return response;
	}
	//TODO ServletRequestAttribute or Spring security...
	private String getSessionAttribute(String arg) {
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		String value = (String)session.getAttribute(arg);
		logger.debug("country_code from session: " + value);
		return value;
	}
}
