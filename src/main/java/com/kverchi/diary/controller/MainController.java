package com.kverchi.diary.controller;

import com.sun.org.apache.xpath.internal.operations.Mod;
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
	@RequestMapping("/transaction-error")
	public ModelAndView showTransactionErrorPage() {
		ModelAndView mv = new ModelAndView("error/transaction-error");
		return mv;
	}
	@RequestMapping("/denied")
	public ModelAndView showDeniedPage() {
		ModelAndView mv = new ModelAndView("error/denied-error");
		mv.addObject("result", "Page denied");
		return mv;
	}
	@RequestMapping("/")
	public ModelAndView showMainPage() {
		ModelAndView mv = new ModelAndView("index");
		return mv;
	}
}
