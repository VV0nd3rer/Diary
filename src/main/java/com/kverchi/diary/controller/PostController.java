package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.security.UserDetailsImpl;
import com.kverchi.diary.service.CommentService;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.CountryService;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.UserService;

@RestController
@RequestMapping("posts")
public class PostController {
	final static Logger logger = Logger.getLogger(PostController.class);
	
	final static String LOGIN = "login";
	final static String POSTS = "posts";
	final static String NEW_POST = "new-post";
	final static String SINGLE_POST = "single-post";
	final static String REDIRECT_TO_POSTS = "redirect:/posts/list";
	
	String message = "Welcome";
	@Autowired
	PostService postService;
	@Autowired 
	CommentService commentService;
	@Autowired
	CountryService countryService;
	@Autowired
	CountriesSightService countriesSightService;
	@Autowired
	UserService userService;
	
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
		ModelAndView mv = new ModelAndView(POSTS);
		mv.addObject("post", new Post());
		mv.addObject("posts", all_posts);
		return mv;
	}
	@RequestMapping("/single-post/{post_id}")
	public ModelAndView showSinglePost(@PathVariable("post_id") int post_id) {
		ModelAndView mv = new ModelAndView(SINGLE_POST);
		Post post = postService.getPostById(post_id);
		Set<Comment> comments = post.getPost_comments();
		CountriesSight sight =  countriesSightService.getSightById(post.getSight_id()); //post.getSight();
		mv.addObject("post", post);
		mv.addObject("sight", sight);
		mv.addObject("comments", comments);
		boolean isAuthor = false;
		User currentUser = userService.getUserFromSession();
		if(currentUser != null) {
			isAuthor = currentUser.getUsername().equals(post.getUser().getUsername());
		}
		mv.addObject("isAuthor", isAuthor);
		return mv;
	}
	@RequestMapping(value = "/list-posts", /*method = RequestMethod.GET,*/ produces = "application/json")
	public List<Post> firstPost() {
		List<Post> all_posts = postService.getAllPosts();
		return all_posts;
	}
	
	@RequestMapping("/edit/{post_id}")
    public ModelAndView editPost(@PathVariable("post_id") int post_id){
       Post post = postService.getPostById(post_id);
       
       User currentUser = userService.getUserFromSession();
	   if(currentUser == null) {
			return new ModelAndView(LOGIN);
	   }
	   
	   boolean isAuthor = currentUser.getUsername().equals(post.getUser().getUsername());
	   if(!isAuthor) {
		   return new ModelAndView(REDIRECT_TO_POSTS);
	   }
       
       List<CountriesSight> sightList = null;
       
       String country_code = null;
	   country_code = getSessionAttribute("country_code");
	   if(country_code != null) {
		   sightList = countriesSightService.getCountrySights(country_code);
	   }
	   CountriesSight sight =  countriesSightService.getSightById(post_id);
	   
       ModelAndView mv = new ModelAndView(NEW_POST);
       mv.addObject("post", post);
       mv.addObject("sight", sight);
       mv.addObject("sightList", sightList);
       return mv;
    }
	
	@RequestMapping("/remove/{post_id}")
	public String removePost(@PathVariable("post_id") int post_id) {
		String res = "NOT OK";
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return res;
		}
		Post postFromDB = postService.getPostById(post_id);
		String postAuth = postFromDB.getUser().getUsername();
		
		boolean isAuthor = currentUser.getUsername().equals(postAuth);
		if(!isAuthor) {
			return res;
		}
		
		//TODO handling db exception
		postService.deletePost(post_id);
		res = "OK";
		return res;
	}
	@RequestMapping(value="/add-post", method = RequestMethod.POST)
	public ServiceResponse addPost(@RequestBody Post post) {
		ServiceResponse response = 
				new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
		
		//TODO Cross-Site Request Forgery (CSRF) 
		
		//Anonymous user
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return response;
		}
		
		//New post
		if(post.getPost_id() == 0) {
			response = postService.addPost(post);
		}
		//Update existing post
		else {
			//Check if user is owner of this post
			Post postFromDB = postService.getPostById(post.getPost_id());
			String postAuth = postFromDB.getUser().getUsername();
			boolean isAuthor = currentUser.getUsername().equals(postAuth);
			if(!isAuthor) {
				return response;
			}
			response = postService.updatePost(post);
		}
		return response;
	}
	
	@RequestMapping(value="/new-post") 
	public ModelAndView newPost() {
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return new ModelAndView(LOGIN);
		}
		String country_code = null;
		country_code = getSessionAttribute("country_code");
		List<CountriesSight> sightList = null;
		if(country_code != null) {
			   sightList = countriesSightService.getCountrySights(country_code);
		}
		ModelAndView mv = new ModelAndView(NEW_POST);
		mv.addObject("post", new Post());
		mv.addObject("sightList", sightList);
		return mv;
	}
	@RequestMapping(value="/add-comment", method = RequestMethod.POST)
	public ServiceResponse addComment(@RequestBody Comment comment) {
		ServiceResponse response = new ServiceResponse<Comment>();
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return response;
		}
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
