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
public class FootballPlayer {

	@Id @GeneratedValue
	@Column(name = "PLAYER_ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY) // 지연 로딩 설정
	@JoinColumn(name = "CLUB_ID")
	private FootballClub footballClub;

	private String name;

	public FootballPlayer(String name) {
		this.name = name;
	}
}
