package io.github.seondongpyo.mapping.relation.manytomany.alternative;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Attendee {

	@Id @GeneratedValue
	@Column(name = "ATTENDEE_ID")
	private Long id;

	@OneToMany(mappedBy = "attendee")
	private List<CourseRegistration> courses = new ArrayList<>();

	private String name;

	public Attendee(String name) {
		this.name = name;
	}
}
