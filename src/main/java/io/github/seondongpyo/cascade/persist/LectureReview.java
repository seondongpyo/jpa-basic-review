package io.github.seondongpyo.cascade.persist;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class LectureReview {

	@Id @GeneratedValue
	@Column(name = "LECTURE_REVIEW_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "LECTURE_ID")
	private Lecture lecture;

}
