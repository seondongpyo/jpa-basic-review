package io.github.seondongpyo.proxy.loading;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.proxy.basic.FootballClub;
import io.github.seondongpyo.proxy.basic.FootballPlayer;

@DisplayName("즉시 로딩과 지연 로딩")
public class FetchTypeTest {

	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeEach
	void setup() {
		emf = Persistence.createEntityManagerFactory("basic");
		em = emf.createEntityManager();
		em.getTransaction().begin();
	}

	@AfterEach
	void close() {
		em.close();
		emf.close();
	}

	@DisplayName("지연 로딩 - FetchType.LAZY")
	@Test
	void fetchTypeLazy() {
		// given
		FootballClub club = new FootballClub("Real Madrid");
		em.persist(club);

		FootballPlayer player = new FootballPlayer("Eden Hazard");
		player.setFootballClub(club);
		em.persist(player);

		em.flush();
		em.clear();

		// when
		FootballPlayer foundPlayer = em.find(FootballPlayer.class, player.getId()); // 지연 로딩으로 설정 후 엔티티 조회

		// then
		assertThat(foundPlayer.getFootballClub().getClass()).isNotEqualTo(FootballClub.class); // FootballClub 객체는 실제 엔티티가 아닌 프록시로 조회
	}

}
