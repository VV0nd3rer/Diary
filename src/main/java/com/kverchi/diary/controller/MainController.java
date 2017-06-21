package com.kverchi.diary.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController	
public class MainController {
/*	@RequestMapping("/error")
	public ModelAndView showErrorPage() {
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("result", "Error");
		return mv;
	}*/
	@RequestMapping("/denied")
	public ModelAndView showDeniedPage() {
		ModelAndView mv = new ModelAndView("error/denied-error");
		mv.addObject("result", "Page denied");
		return mv;
	}
}
