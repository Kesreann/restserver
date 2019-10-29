package controller.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.type.EnumType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "todo")
public class TodoEntity {

	@Id
	@Column
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String title;
	@Column(nullable = false)
	private String description;
	@Column
	private Boolean complete;
	@Column
	@Enumerated(javax.persistence.EnumType.STRING)
	private TodoStatus status;
	@Column
	private Integer index;
	
}
