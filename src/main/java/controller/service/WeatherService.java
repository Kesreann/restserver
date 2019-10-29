package controller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import controller.model.WeatherEntity;
import controller.repository.WeatherRepository;

@Service
public class WeatherService {

	@Autowired
	WeatherRepository repository;
	
	public WeatherEntity getCurrentWeather() {
		return repository.getCurrentWeather();
	}
	
	public List<WeatherEntity> getForecast() {
		return repository.getForecast();
	}
}
