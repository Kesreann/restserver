package controller.model.openweathermap;

import java.util.List;

@lombok.Getter
@lombok.Setter
public class Temperature {

	private Double day;
	private Double min;
	private Double max;
	private Double night;
	private Double eve;
	private Double morn;
}
