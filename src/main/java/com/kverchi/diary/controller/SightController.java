package com.kverchi.diary.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

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

import com.kverchi.diary.domain.CountriesSight;
import com.kverchi.diary.domain.Country;
import com.kverchi.diary.domain.Post;
import com.kverchi.diary.service.CountriesSightService;
import com.kverchi.diary.service.CountryService;
import com.kverchi.diary.service.PostService;

@RestController	
@RequestMapping("sights")
public class SightController {
	@Autowired
	CountriesSightService countriesSightService;
	@Autowired
	CountryService countryService;
	@Autowired
	PostService postService;
	
	@RequestMapping("/map")
	public ModelAndView showTestBootstrapModal() {
		ModelAndView mv = new ModelAndView("map");
		return mv;
	}
	@RequestMapping("/gmap")
	public ModelAndView showGmap() {
		ModelAndView mv = new ModelAndView("gmap");
		return mv;
	}
	@RequestMapping("/get-sights") 
	public List<CountriesSight> getSights() {
		List<CountriesSight> sights = countriesSightService.getCountrySights("cz");
		return sights;
	}
	@RequestMapping("/is-coord-stored") 
	public boolean isCoordStored(float x, float y) {
		boolean res = false;
		CountriesSight sight = countriesSightService.getSightByCoord(x, y);
		if(sight != null) {
			res =  true;
		}
		return res;
	}
	//Test page with carousel plagin
	/*@RequestMapping("/carousel")
	public ModelAndView country(@RequestParam("country_code") String code) {
		Country country = countryService.getCountryById(code);
		Set<CountriesSight> country_sights = country.getCountriesSight();
		
		ModelAndView mv = new ModelAndView("country");
		mv.addObject("country", country);
		mv.addObject("country_sights", country_sights);
		return mv;
	}*/
	@RequestMapping("/country")
	public ModelAndView sights(@RequestParam("country_code") String code) {
		Country country = countryService.getCountryById(code);
		Set<CountriesSight> country_sights = country.getCountriesSight();
		
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpSession session = attr.getRequest().getSession(true);
		session.setAttribute("country_code", code);
		
		ModelAndView mv = new ModelAndView("sights");
		mv.addObject("country", country);
		mv.addObject("country_sights", country_sights);
		return mv;
	}
	
	@RequestMapping("/posts/{sight_id}")
	public ModelAndView showSightPosts(@PathVariable("sight_id") int sight_id) {
		List<Post> sight_posts = null;
		sight_posts = postService.getSightPosts(sight_id);
		ModelAndView mv = new ModelAndView("posts");
		mv.addObject("posts", sight_posts);
		return mv;
	}
	
	@RequestMapping("/edit/{sight_id}")
	public CountriesSight editSight(@PathVariable("sight_id") int sight_id, Model model) {
		CountriesSight sight = countriesSightService.getSightById(sight_id);
		return sight;
	}
	//TODO return ServiceResponse
	@RequestMapping("/remove/{sight_id}")
	public String removeSight(@PathVariable("sight_id") int sight_id) {
		countriesSightService.deleteSight(sight_id);
		String res = "OK";
		return res;
	}
	@RequestMapping(value="/add-sight", method = RequestMethod.POST)
	public CountriesSight addSight(@RequestBody CountriesSight sight) {
		CountriesSight addedSight;
		if(sight.getSight_id() == 0) {
			addedSight = countriesSightService.addSight(sight);
		}
		else {
			addedSight = countriesSightService.updateSight(sight);
		}
		return addedSight;
	}
}
