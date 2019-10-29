package controller.repository;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import controller.exceptions.TemporaryException;
import controller.model.WeatherEntity;
import controller.model.openweathermap.CurrentWeather;
import controller.model.openweathermap.ForecastResponse;
import controller.model.openweathermap.WeatherResponse;
import controller.util.SQLUtils;

@Repository
public class WeatherRepository {

	@Value("${weather.location}")
	private String location;
	
	@Value("${weather.apiKey}")
	private String apiKey;
	
	private RestTemplate restTemplate = new RestTemplate();
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	private String weatherUrl = "http://api.openweathermap.org/data/2.5/$type";
	
	public WeatherEntity getCurrentWeather() {
		String url = weatherUrl.replace("$type", "weather");
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				.queryParam("q", location)
				.queryParam("units", "metric")
				.queryParam("APPID", apiKey);
		
		WeatherEntity we = new WeatherEntity();
		try {
			CurrentWeather cwr = restTemplate.getForObject(builder.build().toUri(), CurrentWeather.class);
			we.setDateTime(cwr.getDt());
			we.setDescription(cwr.getWeather().get(0).getDescription());
			we.setMain(cwr.getWeather().get(0).getMain());
			we.setTemp(cwr.getMain().getTemp());
			
			saveWeather(we);
		} catch(Exception e) {
			throw new TemporaryException("Service temporary unavailable");
		}
		
		return we;
	}
	
	public List<WeatherEntity> getForecast() {
		String url = weatherUrl.replace("$type", "forecast") + "/daily";
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url)
				.queryParam("q", location)
				.queryParam("units", "metric")
				.queryParam("APPID", apiKey);
		
		ForecastResponse fcr = restTemplate.getForObject(builder.build().toUri(), ForecastResponse.class);
		List<WeatherEntity> lwe = new ArrayList<>();		
		fcr.getList().stream().forEach(w -> {
			WeatherEntity we = new WeatherEntity();
			we.setDateTime(w.getDt());
			we.setDescription(w.getWeather().get(0).getDescription());
			we.setMain(w.getWeather().get(0).getMain());
			we.setTemp(w.getTemp().getDay());
			we.setTempNight(w.getTemp().getNight());
			lwe.add(we);
		});
		
		return lwe;
	}
	
	private void saveWeather(WeatherEntity w) {
		String query = "insert into weather (main, description, temp, tempnight, date, created) values(?,?,?,?,?," + SQLUtils.escape(new Date())+")";
		jdbcTemplate.update(query, w.getMain(), w.getDescription(), w.getTemp(), w.getTempNight(), w.getDateTime());
	}
}
