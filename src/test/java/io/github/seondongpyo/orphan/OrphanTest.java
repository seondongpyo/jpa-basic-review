package io.github.seondongpyo.orphan;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("고아 객체 (orphanRemoval)")
public class OrphanTest {

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

	@DisplayName("true 설정 시 부모 엔티티와 연관관계가 끊어진 자식 엔티티를 자동으로 삭제한다.")
	@Test
	void orphanRemoval() {
		// given
		GameReview review1 = new GameReview(2);
		GameReview review2 = new GameReview(3);
		GameReview review3 = new GameReview(5);

		Game game = new Game("CYBERPUNK 2077");
		game.addReview(review1);
		game.addReview(review2);
		game.addReview(review3);
		em.persist(game);

		em.flush();
		em.clear();

		Game foundGame = em.find(Game.class, game.getId());
		foundGame.getReviews().remove(0); // Game의 reviews에서 리뷰 하나를 삭제

		// when
		List<GameReview> gameReviews = em.createQuery("select gr from GameReview gr", GameReview.class)
											.getResultList();

		// then
		assertThat(gameReviews).hasSize(2);
	}

	@DisplayName("부모 엔티티가 제거되면 자식 엔티티도 제거된다. (like CascadeType.REMOVE)")
	@Test
	void childRemovedAfterParentRemoved() {
		// given
		GameReview review1 = new GameReview(10);
		GameReview review2 = new GameReview(9);

		Game game = new Game("The Witcher 3");
		game.addReview(review1);
		game.addReview(review2);
		em.persist(game);

		em.flush();
		em.clear();

		Game foundGame = em.find(Game.class, game.getId());
		em.remove(foundGame); // 부모 엔티티 제거

		// when
		List<GameReview> gameReviews = em.createQuery("select gr from GameReview gr", GameReview.class)
											.getResultList();

		// then
		assertThat(gameReviews).isEmpty();
	}

}
