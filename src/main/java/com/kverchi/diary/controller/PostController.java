package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.kverchi.diary.domain.Comment;
import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.domain.ServiceResponse;
import com.kverchi.diary.service.CommentService;
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
		
		mv.addObject("post", post);
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
       ModelAndView mv = new ModelAndView("new-post");
       mv.addObject("post", post);
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
		ModelAndView mv = new ModelAndView("new-post");
		mv.addObject("post", new Post());
		return mv;
	}
	@RequestMapping(value="/add-comment", method = RequestMethod.POST)
	public ServiceResponse addComment(@RequestBody Comment comment) {
		ServiceResponse response = new ServiceResponse<Comment>();
		response = commentService.addComment(comment);
		return response;
	}
	
}
