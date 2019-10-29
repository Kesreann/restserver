package controller.model.openweathermap;

import java.util.Date;

@lombok.Getter
@lombok.Setter
public class Sys {
	private Long type;
	private Long id;
	private String message;
	private String country;
	private Date sunrise;
	private Date sunset;
}
