package io.github.seondongpyo.proxy;

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
public class BasketballPlayer {

	@Id @GeneratedValue
	@Column(name = "BASKETBALL_PLAYER_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.EAGER) // 즉시 로딩 (기본값)
	@JoinColumn(name = "BASKETBALL_CLUB_ID")
	private BasketballClub basketballClub;

	private String name;
	private String position;

	public BasketballPlayer(String name, String position) {
		this.name = name;
		this.position = position;
	}

}
