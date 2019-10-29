package controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import controller.model.WeatherEntity;
import controller.service.WeatherService;

@RestController
@RequestMapping("/api/weather")
public class WeatherRestService {

	@Autowired
	private WeatherService ws;
	
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public WeatherEntity currentWeather() {
		return ws.getCurrentWeather();
	}
	
	@RequestMapping(value="/forecast", method = RequestMethod.GET)
	@ResponseBody
	public List<WeatherEntity> getForecast() {
		return ws.getForecast();
	}
}
