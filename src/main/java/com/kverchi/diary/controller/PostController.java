package com.kverchi.diary.controller;

import com.kverchi.diary.model.Pagination;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Kverchi on 20.6.2018.
 */
@RestController
@RequestMapping("posts")
public class PostController {
    private final static String POSTS = "posts";

    @RequestMapping("/main")
    public ModelAndView showMain(
            @RequestParam(value="name", required=false, defaultValue="Guest") String name) {
        ModelAndView mv = new ModelAndView("main");
        mv.addObject("message", "Welcome");
        mv.addObject("name", name);
        return mv;
    }
    @RequestMapping("/list")
    public ModelAndView showPosts() {
        ModelAndView mv = new ModelAndView(POSTS);
        return mv;
    }
    @RequestMapping(value = "/pagination-posts", method = RequestMethod.POST)
    public ModelAndView showPaginationPosts(@RequestBody Pagination pagination) {

        ModelAndView mv = new ModelAndView("fragments/content :: postContent");
        return mv;
    }
}
