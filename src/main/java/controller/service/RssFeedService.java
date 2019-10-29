package controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.model.RssFeedEntity;
import controller.repository.RssFeedRepostitory;

@Service
public class RssFeedService {

	@Autowired
	private RssFeedRepostitory repo;
	
	public List<RssFeedEntity> getAll() {
		return repo.getAll();
	}
	
	public RssFeedEntity getById(Long id) {
		return repo.getById(id);
	}
	
	public void addFeed(RssFeedEntity rfe) {
		repo.addFeed(rfe);
	}
	
	public void removeFeed(Integer id) {
		repo.removeFeed(id);
	}
}
