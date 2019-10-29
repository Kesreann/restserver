package controller.model.openweathermap;

@lombok.Getter
@lombok.Setter
public class WeatherResponse {

	private Long id;
	private String main;
	private String description;
	private String icon;
}
