package controller.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.model.news.News;
import controller.service.NewsService;

@RestController
@RequestMapping("/api/news")
public class NewsRestService {

	@Autowired
	private NewsService ns;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public News getNews() {
		return ns.getNews();
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public News currentWeather(@PathVariable Long id) {
		return ns.getNewsById(id);
	}
}
