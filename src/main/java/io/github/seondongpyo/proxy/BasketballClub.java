package io.github.seondongpyo.proxy;

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
public class BasketballClub {

	@Id @GeneratedValue
	@Column(name = "BASKETBALL_CLUB_ID")
	private Long id;

	@OneToMany(mappedBy = "basketballClub")
	private List<BasketballPlayer> basketballPlayers = new ArrayList<>();

	private String name;
	private String hometown;

	public BasketballClub(String name, String hometown) {
		this.name = name;
		this.hometown = hometown;
	}
}
