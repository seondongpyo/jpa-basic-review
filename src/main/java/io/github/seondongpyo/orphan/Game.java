package io.github.seondongpyo.orphan;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
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
public class Game {

	@Id @GeneratedValue
	private Long id;

	@OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<GameReview> reviews = new ArrayList<>();

	private String name;

	public Game(String name) {
		this.name = name;
	}

	// 연관관계 편의 메서드
	public void addReview(GameReview gameReview) {
		reviews.add(gameReview);
		gameReview.setGame(this);
	}

}
