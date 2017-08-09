package com.kverchi.diary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Pagination;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.domain.User;
import com.kverchi.diary.enums.ServiceMessageResponse;
import com.kverchi.diary.service.CommentService;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.CountryService;
import com.kverchi.diary.service.PaginationService;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.UserService;

@RestController
@RequestMapping("posts")
@SessionAttributes("currentSight")
public class PostController {
	private final static Logger logger = Logger.getLogger(PostController.class);

	private final static String LOGIN = "login";
	private final static String POSTS = "posts";
	private final static String NEW_POST = "new-post";
	private final static String SINGLE_POST = "single-post";
	private final static String REDIRECT_TO_POSTS = "redirect:/posts/list";
	
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
	
	//???
	@Autowired
	PaginationService paginatonService;

	@ModelAttribute("currentSight")
	public CountriesSight getCurrentSight () {
		return new CountriesSight();
	}

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
	public ModelAndView showPosts(SessionStatus sessionStatus, @ModelAttribute("currentSight") CountriesSight currentSight) {
		if(currentSight != null) {
			logger.debug("Session attribute 'currentSight' is NOT NULL");
			logger.debug("currentSight label is " + currentSight.getSight_label());
		}
		//sessionStatus.setComplete();
		currentSight = new CountriesSight();
		logger.debug("Session status is set to complete...");
		logger.debug("Session attribute 'currentSight->label' is " + currentSight.getSight_label());
		int page_index = 1;
		Pagination pagination = paginatonService.getPaginatedPage(page_index, "posts", null);
		ModelAndView mv = new ModelAndView(POSTS);
		mv.addObject("pages_total_num", pagination.getPages_total_num());
		mv.addObject("pagination_handler", "posts");
		mv.addObject("posts", pagination.getPagePosts());
		mv.addObject("currentSight", currentSight);
		return mv;
	}

	@RequestMapping(value = "/list-test", method = RequestMethod.GET, headers="Accept=application/json")
	public List<Post> showTestPosts() {		
		List<Post> posts = postService.getAllPosts();
		return posts;
	}

	@RequestMapping("/sight/{sight_id}")
	public ModelAndView showSightPosts(@PathVariable("sight_id") int sight_id) {
		
		//List<Post> sight_posts = null;
		int page_index = 1;
		Map<String, Object> search_criteries = new HashMap<String, Object>();
		search_criteries.put("sight_id", sight_id);
		Pagination pagination = /*postService.*/paginatonService.getPaginatedPage(page_index, "posts", search_criteries);
		//sight_posts = postService.getSightPosts(sight_id);

		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("pages_total_num", pagination.getPages_total_num());
		mv.addObject("pagination_handler", "sight_posts");
		mv.addObject("posts", pagination.getPagePosts());

		CountriesSight sight = countriesSightService.getSightById(sight_id);
		mv.addObject("currentSight", sight);
		return mv;
	}
	@RequestMapping("/single-post/{post_id}")
	public ModelAndView showSinglePost(@PathVariable("post_id") int post_id, @ModelAttribute("currentSight") CountriesSight currentSight) {
		ModelAndView mv = new ModelAndView(SINGLE_POST);

		Post post = postService.getPostById(post_id);
		Set<Comment> comments = post.getPost_comments();
		//CountriesSight sight =  countriesSightService.getSightById(post.getSight_id()); //post.getSight();
		//Add sight ID to the session
		mv.addObject("post", post);
		mv.addObject("currentSight", currentSight);
		//mv.addObject("sight", sight);
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
    public ModelAndView editPostForm(@PathVariable("post_id") int post_id){
       Post post = postService.getPostById(post_id);
       
       User currentUser = userService.getUserFromSession();
	   if(currentUser == null) {
			return new ModelAndView(LOGIN);
	   }
	   
	   boolean isAuthor = currentUser.getUsername().equals(post.getUser().getUsername());
	   if(!isAuthor) {
		   return new ModelAndView(REDIRECT_TO_POSTS);
	   }
      
	   CountriesSight sight =  countriesSightService.getSightById(post.getSight_id());
	   
       ModelAndView mv = new ModelAndView(NEW_POST);
       mv.addObject("post", post);
       mv.addObject("sight", sight);
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
	@RequestMapping(value="/save-post", method = RequestMethod.POST)
	public ServiceResponse savePost(@RequestBody Post post, @ModelAttribute("currentSight") CountriesSight currentSight) {
		ServiceResponse response = 
				new ServiceResponse(HttpStatus.INTERNAL_SERVER_ERROR, ServiceMessageResponse.UKNOWN_PROBLEM.toString());
		
		//TODO Cross-Site Request Forgery (CSRF) 	
		//Anonymous user
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return response;
		}
		/*if(currentSight.getSight_label() == null) {
			logger.debug("Session sight's label is NULL");
			return  response;
		}*/
		post.setSight_id(currentSight.getSight_id());
		//New post
		if(post.getPost_id() == 0) {
			response = postService.addPost(post);
			return response;
		}
		//Update existing post
		response = postService.updatePost(post);
		return response;
	}
	
	@RequestMapping(value="/new-post") 
	public ModelAndView newPostForm(@ModelAttribute("currentSight") CountriesSight currentSight) {
		User currentUser = userService.getUserFromSession();
		/*if(currentUser == null) {
			return new ModelAndView(LOGIN);
		}*/
		if(currentSight.getSight_label() == null) {
			logger.debug("Session sight's label is NULL");
			return new ModelAndView(REDIRECT_TO_POSTS);
		}
		ModelAndView mv = new ModelAndView(NEW_POST);
		mv.addObject("post", new Post());
		mv.addObject("sight", currentSight);
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
