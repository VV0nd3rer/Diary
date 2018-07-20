package com.kverchi.diary.controller;

import com.kverchi.diary.model.PostSearchRequest;
import com.kverchi.diary.model.entity.CountriesSight;
import com.kverchi.diary.model.entity.Post;
import com.kverchi.diary.model.entity.User;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.PostService;
import com.kverchi.diary.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Kverchi on 20.6.2018.
 */
@RestController
@RequestMapping("posts")
public class PostController {
    private final static String POSTS = "posts";
    @Autowired
    PostService postService;
    @Autowired
    UserService userService;
    @Autowired
    CountriesSightService countriesSightService;

    @RequestMapping("/main")
    public ModelAndView showMain(
            @RequestParam(value="name", required=false, defaultValue="Guest") String name) {
        ModelAndView mv = new ModelAndView("main");
        mv.addObject("message", "Welcome");
        mv.addObject("name", name);
        return mv;
    }
    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView loadPostsPage() {
        ModelAndView mv = new ModelAndView(POSTS);
        List<User> users = userService.findAll();
        List<CountriesSight> sights = countriesSightService.findAll();
        mv.addObject("authors", users);
        mv.addObject("sights", sights);
        return mv;
    }
    @RequestMapping(value = "/paginated-posts/{currentPage}")
    public ModelAndView showPaginatedPosts(@PathVariable("currentPage") int currentPage) {
        ModelAndView mv = new ModelAndView("fragments/content/postContent");
        Page<Post> paginatedPosts = postService.getAllPosts(currentPage);
        List<Post> posts = paginatedPosts.getContent();
        int totalPages = paginatedPosts.getTotalPages();
        mv.addObject("posts", posts);
        mv.addObject("totalPages", totalPages);
        return mv;
    }
    @RequestMapping(value="/search-paginated-posts", method = RequestMethod.POST)
    public ModelAndView searchPaginatedPosts(@RequestBody PostSearchRequest postSearchRequest) {
        ModelAndView mv = new ModelAndView("fragments/content/postContent");
        Page<Post>  paginatedPosts = postService.searchPosts(postSearchRequest);
        List<Post> posts = paginatedPosts.getContent();
        int totalPages = paginatedPosts.getTotalPages();
        mv.addObject("posts", posts);
        mv.addObject("totalPages", totalPages);
        return mv;
    }
}
