package controller.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "weather")
public class WeatherEntity {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	private Long id;
	@Column
	private String main;
	@Column
	private String description;
	@Column
	private Double temp;
	@Column(name = "tempnight")
	private Double tempNight;
	@Column(name = "date")
	private Date dateTime;
	@Column
	private Date created;
	
}
