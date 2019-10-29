package controller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.model.news.News;
import controller.repository.NewsRepository;

@Service
public class NewsService {

	@Autowired
	private NewsRepository repository;
	
	public News getNews() {
		return repository.getNews();
	}
	
	public News getNewsById(Long id) {
		return repository.getNewsById(id);
	}
}
