package io.github.seondongpyo.cascade.all;

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
public class Cosmetics {

	@Id @GeneratedValue
	@Column(name = "COSMETICS_ID")
	private Long id;

	@OneToMany(mappedBy = "cosmetics", cascade = CascadeType.ALL) // 영속성 전이 설정
	private List<CosmeticsReview> reviews = new ArrayList<>();

	// 연관관계 편의 메서드
	public void addReview(CosmeticsReview cosmeticsReview) {
		reviews.add(cosmeticsReview);
		cosmeticsReview.setCosmetics(this);
	}

}
