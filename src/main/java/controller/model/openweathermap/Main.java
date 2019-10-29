package controller.model.openweathermap;

import com.fasterxml.jackson.annotation.JsonProperty;

@lombok.Getter
@lombok.Setter
public class Main {

	private Double temp;
	private Long humidity;
	private Long pressure;
	@JsonProperty("temp_min")
	private Long tempMin;
	@JsonProperty("temp_max")
	private Long tempMax;
}
