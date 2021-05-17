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
public class Course {

	@Id @GeneratedValue
	@Column(name = "COURSE_ID")
	private Long id;

	@OneToMany(mappedBy = "course")
	private List<CourseRegistration> courseRegistrations = new ArrayList<>();

	private String name;

	public Course(String name) {
		this.name = name;
	}
}
