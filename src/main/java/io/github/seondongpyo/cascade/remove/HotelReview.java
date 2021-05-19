package io.github.seondongpyo.cascade.remove;

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
public class HotelReview {

	@Id @GeneratedValue
	@Column(name = "HOTEL_REVIEW_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "HOTEL_ID")
	private Hotel hotel;

}
