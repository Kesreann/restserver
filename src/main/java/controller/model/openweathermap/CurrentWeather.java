package controller.model.openweathermap;

import java.util.Date;
import java.util.List;

@lombok.Getter
@lombok.Setter
public class CurrentWeather {

	private Coordinats coord;
	private Sys sys;
	private List<WeatherResponse> weather;
	private String base;
	private Main main;
	private Wind wind;
	private Clouds clouds;
	private Date dt;
	private Long id;
	private String name;
	private Long cod;
}
