package controller.model;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.domain.Persistable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Table(name = "rssfeeds")
public class RssFeedEntity implements Persistable<Integer>{

	@Id
	@Column
	private Integer id;
	
	@Column
	private String url;
	
	@Column
	private String description;

	@JsonIgnore
	@Override
	public boolean isNew() {
		return id == null;
	}

}
