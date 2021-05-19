package io.github.seondongpyo.cascade.remove;

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
public class Hotel {

	@Id @GeneratedValue
	@Column(name = "HOTEL_ID")
	private Long id;

	@OneToMany(mappedBy = "hotel", cascade = CascadeType.REMOVE) // 영속성 전이 : 삭제 시에만 적용
	private List<HotelReview> reviews = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addReview(HotelReview hotelReview) {
		reviews.add(hotelReview);
		hotelReview.setHotel(this);
	}

}
