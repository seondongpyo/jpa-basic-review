package io.github.seondongpyo.cascade.persist;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Entity
public class Lecture {

	@Id @GeneratedValue
	@Column(name = "LECTURE_ID")
	private Long id;

	@OneToMany(mappedBy = "lecture", cascade = CascadeType.PERSIST) // 영속성 전이 : 저장 시에만 적용
	private List<LectureReview> reviews = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addReview(LectureReview lectureReview) {
		reviews.add(lectureReview);
		lectureReview.setLecture(this);
	}
	
}
