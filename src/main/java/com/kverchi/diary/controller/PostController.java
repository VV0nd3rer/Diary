package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

import com.kverchi.diary.domain.*;
import com.kverchi.diary.enums.Counter;
import com.kverchi.diary.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.enums.ServiceMessageResponse;

@RestController
@RequestMapping("posts")
@SessionAttributes("currentSight")
public class PostController {
	private final static Logger logger = Logger.getLogger(PostController.class);

	private final static String LOGIN = "login";
	private final static String POSTS = "posts";
	private final static String NEW_POST = "save-post";
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
	@Autowired
	CounterService counterService;
	
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
	public ModelAndView showPosts(@ModelAttribute("currentSight") CountriesSight currentSight) {
		currentSight = new CountriesSight();
		ModelAndView mv = new ModelAndView(POSTS);
		mv.addObject("currentSight", currentSight);
		mv.addObject("authors", userService.getAllUsers());
		mv.addObject("sights", countriesSightService.getAllSights());
		return mv;
	}

	@RequestMapping(value = "/pagination-posts", method = RequestMethod.POST)
	public ModelAndView showPaginationPosts(@RequestBody PostSearchAttributes searchAttributes) {
		PostSearchResults results = postService.search(searchAttributes);
		ModelAndView mv = new ModelAndView("fragments :: page");
		mv.addObject("posts", results.getResults());
		mv.addObject("totalPages", results.getTotalPages());
		return mv;
	}

	@RequestMapping("/sight/{sight_id}")
	public ModelAndView showPaginatedSightPosts(@PathVariable("sight_id") int sight_id) {
		ModelAndView mv = new ModelAndView(POSTS);
		CountriesSight sight = countriesSightService.getSightById(sight_id);
		int wishCounter = counterService.getCounterValue(sight_id, Counter.WISHES);
		int visitCounter = counterService.getCounterValue(sight_id, Counter.VISITS);
		boolean isVisitedValueExists = false;
		boolean isWishedValueExists = false;
		User currentUser = userService.getUserFromSession();
		if(currentUser != null) {
			isVisitedValueExists = counterService.isCounterValueExists(sight_id, currentUser.getUserId(), Counter.VISITS);
			isWishedValueExists = counterService.isCounterValueExists(sight_id, currentUser.getUserId(), Counter.WISHES);
		}

		mv.addObject("authors", userService.getAllUsers());
		mv.addObject("currentSight", sight);
		mv.addObject("wishCounter", wishCounter);
		mv.addObject("visitCounter", visitCounter);
		mv.addObject("isWishedValueExists", isWishedValueExists);
		mv.addObject("isVisitedValueExists", isVisitedValueExists);
		return mv;
	}
	@RequestMapping("/single-post/{post_id}")
	public ModelAndView showSinglePost(@PathVariable("post_id") int post_id, @ModelAttribute("currentSight") CountriesSight currentSight) {
		ModelAndView mv = new ModelAndView(SINGLE_POST);

		Post post = postService.getPostById(post_id);
		Set<Comment> comments = post.getPostComments();
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
      
	   CountriesSight sight =  post.getCountriesSight();
	   
       ModelAndView mv = new ModelAndView(NEW_POST);
       mv.addObject("post", post);
       mv.addObject("sight", sight);
       return mv;
    }
	
	@RequestMapping("/remove/{post_id}")
	public ModelAndView removePost(@PathVariable("post_id") int post_id) {
		ModelAndView mv = new ModelAndView(REDIRECT_TO_POSTS);
		User currentUser = userService.getUserFromSession();
		if(currentUser == null) {
			return mv;
		}
		Post postFromDB = postService.getPostById(post_id);
		String postAuth = postFromDB.getUser().getUsername();
		
		boolean isAuthor = currentUser.getUsername().equals(postAuth);
		if(!isAuthor) {
			return mv;
		}
		postService.deletePost(post_id);
		return mv;
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
		//post.setSight_id(currentSight.getSight_id());
		//New post
		if(post.getPostId() == 0) {
			if(currentSight.getSightId() == 0) {
				return response;
			}
			post.setCountriesSight(currentSight);
			response = postService.addPost(post);
			return response;
		}
		//Update existing post
		response = postService.updatePost(post);
		return response;
	}
	
	@RequestMapping(value="/save-post")
	public ModelAndView newPostForm(@ModelAttribute("currentSight") CountriesSight currentSight) {
		User currentUser = userService.getUserFromSession();
		/*if(currentUser == null) {
			return new ModelAndView(LOGIN);
		}*/
		if(currentSight.getLabel() == null) {
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
	@RequestMapping(value="/add-visit")
	public ModelAndView addVisitCounterValue(@ModelAttribute("currentSight") CountriesSight currentSight) {
		ModelAndView mv = new ModelAndView("fragment/counter-buttons::visitLabel");
		User currentUser = userService.getUserFromSession();
		if(currentSight.getSightId() == 0 || currentUser == null) {
			return new ModelAndView("fragment/counter-buttons::addVisitButton");
		}
		int sight_id = currentSight.getSightId();
		int user_id = currentUser.getUserId();
		counterService.addCounterValue(sight_id, user_id, Counter.VISITS);
		int visitCounter = counterService.getCounterValue(sight_id, Counter.VISITS);
		mv.addObject("visitCounter", visitCounter);
		return mv;
	}
	@RequestMapping(value="/add-wish")
	public ModelAndView addWishCounterValue(@ModelAttribute("currentSight") CountriesSight currentSight) {
		ModelAndView mv = new ModelAndView("fragment/counter-buttons::wishLabel");
		User currentUser = userService.getUserFromSession();
		if(currentSight.getSightId() == 0 || currentUser == null) {
			return new ModelAndView("fragment/counter-buttons::addWishButton");
		}
		int sight_id = currentSight.getSightId();
		int user_id = currentUser.getUserId();
		counterService.addCounterValue(sight_id, user_id, Counter.WISHES);
		int wishCounter = counterService.getCounterValue(sight_id, Counter.WISHES);
		mv.addObject("wishCounter", wishCounter);
		return mv;
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
