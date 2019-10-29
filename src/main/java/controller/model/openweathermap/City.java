package controller.model.openweathermap;

import java.util.List;

@lombok.Getter
@lombok.Setter
public class City {

	private Long id;
	private String name;
	private Coordinats coord;
	private String country;
}
