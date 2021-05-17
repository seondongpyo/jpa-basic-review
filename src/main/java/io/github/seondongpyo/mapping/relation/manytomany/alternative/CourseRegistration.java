package io.github.seondongpyo.mapping.relation.manytomany.alternative;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@Entity
public class CourseRegistration {

	@Id @GeneratedValue
	@Column(name = "COURSE_REGISTRATION_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COURSE_ID")
	private Course course;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ATTENDEE_ID")
	private Attendee attendee;

	private LocalDateTime registeredAt;
	private String grade;

	public CourseRegistration(Course course, Attendee attendee) {
		this.course = course;
		this.attendee = attendee;
	}
}
