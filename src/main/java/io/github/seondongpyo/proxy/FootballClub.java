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
public class FootballClub {

	@Id @GeneratedValue
	@Column(name = "CLUB_ID")
	private Long id;

	@OneToMany(mappedBy = "footballClub")
	private List<FootballPlayer> footballPlayers = new ArrayList<>();

	private String name;

	public FootballClub(String name) {
		this.name = name;
	}
}
