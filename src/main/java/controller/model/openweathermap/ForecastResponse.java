package controller.model.openweathermap;

import java.util.List;

@lombok.Getter
@lombok.Setter
public class ForecastResponse {

	private String cod;
	private Double message;
	private City city;
	private Double cnt;
	private List<ForecastEntry> list;
}
