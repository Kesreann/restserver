package controller.model.openweathermap;

import java.util.Date;
import java.util.List;

import controller.rest.WeatherRestService;

@lombok.Getter
@lombok.Setter
public class ForecastEntry {

	private Date dt;
	private Temperature temp;
	private Double pressure;
	private Double humidity;
	private List<WeatherResponse> weather;
	
}
