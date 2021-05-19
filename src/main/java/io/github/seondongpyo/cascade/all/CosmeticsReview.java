package io.github.seondongpyo.cascade.all;

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
public class CosmeticsReview {

	@Id @GeneratedValue
	@Column(name = "COSMETICS_REVIEW_ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "COSMETICS_ID")
	private Cosmetics cosmetics;

}
