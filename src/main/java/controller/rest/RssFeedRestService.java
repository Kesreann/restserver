package controller.rest;

import java.util.List;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.model.RssFeedEntity;
import controller.service.RssFeedService;

@RestController
@RequestMapping("/api/rss")
public class RssFeedRestService {

	@Autowired
	private RssFeedService rfs;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public List<RssFeedEntity> getFeeds() {
		return rfs.getAll();
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.GET)
	@ResponseBody
	public RssFeedEntity getFeedById(@PathVariable Long id) {
		return rfs.getById(id);
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public void addFeed(@RequestBody RssFeedEntity rfe) {
		rfs.addFeed(rfe);
	}
	
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void removeFeed(@PathVariable Integer id) {
		rfs.removeFeed(id);
	}
}
