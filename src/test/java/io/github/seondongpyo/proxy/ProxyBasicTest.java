package io.github.seondongpyo.proxy;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;

import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.proxy.FootballPlayer;

@DisplayName("프록시")
class ProxyBasicTest {

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

	@DisplayName("em.getReference() - 엔티티가 아닌 가짜 객체(프록시)를 조회한다.")
	@Test
	void proxy() {
		// given
		FootballPlayer player = new FootballPlayer("Lionel Messi");
		em.persist(player);

		em.flush();
		em.clear();

		// when
		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId());

		// then
		assertThat(foundReferencePlayer.getClass()).isNotEqualTo(FootballPlayer.class);
	}

	@DisplayName("프록시 객체가 초기화되어도 프록시 객체가 실제 엔티티로 바뀌지는 않는다.")
	@Test
	void proxyAfterInitialization() {
		// given
		FootballPlayer player = new FootballPlayer("Heung-min Son");
		em.persist(player);

		em.flush();
		em.clear();

		// 프록시 객체 가져오기
		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId());

		// when
		String name = foundReferencePlayer.getName(); // 프록시 객체 강제 초기화

		// then
		assertThat(name).isEqualTo("Heung-min Son");
		assertThat(foundReferencePlayer.getClass()).isNotEqualTo(FootballPlayer.class);
	}

	@DisplayName("프록시 객체를 조회한 상태에서 em.find()를 호출해도 프록시 객체가 반환된다.")
	@Test
	void returnProxyIfPresentInPersistenceContext() {
		// given
		FootballPlayer player = new FootballPlayer("Cristiano Ronaldo");
		em.persist(player);

		em.flush();
		em.clear();

		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId()); // 프록시 조회

		// when
		FootballPlayer foundPlayer = em.find(FootballPlayer.class, player.getId()); // 실제 엔티티 조회 시도

		// then
		assertThat(foundReferencePlayer.getClass()).isNotEqualTo(FootballPlayer.class);
		assertThat(foundPlayer.getClass()).isNotEqualTo(FootballPlayer.class); // 엔티티가 아닌 프록시 객체를 반환
	}

	@DisplayName("영속성 컨텍스트에 이미 찾는 엔티티가 있으면 em.getReference()를 호출해도 실제 엔티티가 반환된다.")
	@Test
	void returnEntityIfPresentInPersistenceContext() {
		// given
		FootballPlayer player = new FootballPlayer("Kevin De Bruyne");
		em.persist(player);

		em.flush();
		em.clear();

		FootballPlayer foundPlayer = em.find(FootballPlayer.class, player.getId()); // 실제 엔티티 조회

		// when
		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId()); // 프록시 조회 시도

		// then
		assertThat(foundPlayer.getClass()).isEqualTo(FootballPlayer.class);
		assertThat(foundReferencePlayer.getClass()).isEqualTo(FootballPlayer.class); // 실제 엔티티가 반환
	}

	@DisplayName("준영속 상태일 때 프록시를 초기화하면 예외가 발생한다.")
	@Test
	void throwsLazyInitializationException() {
		// given
		FootballPlayer player = new FootballPlayer("Kylian Mbappe");
		em.persist(player);

		em.flush();
		em.clear();

		// 프록시 객체 요청
		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId()); // 프록시 조회

		// when
		em.clear();	// 영속성 컨텍스트 클리어

		// then
		assertThrows(LazyInitializationException.class, () -> foundReferencePlayer.getName()); // 프록시 초기화 시 예외 발생
	}

	@DisplayName("PersistenceUnitUtil.isLoaded(Object entity) - 프록시 객체 초기화 여부를 확인한다.")
	@Test
	void checkProxyIsLoaded() {
		// given
		FootballPlayer player = new FootballPlayer("Harry Kane");
		em.persist(player);

		em.flush();
		em.clear();

		// when
		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId()); // 프록시 조회
		PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();

		// then
		assertThat(persistenceUnitUtil.isLoaded(foundReferencePlayer)).isFalse(); // 프록시 초기화가 안 됨
	}

	@DisplayName("Hibernate.initialize(Object proxy) - 프록시 객체를 강제로 초기화한다.")
	@Test
	void forceInitializationInHibernate() {
		// given
		FootballPlayer player = new FootballPlayer("Erling Haaland");
		em.persist(player);

		em.flush();
		em.clear();

		FootballPlayer foundReferencePlayer = em.getReference(FootballPlayer.class, player.getId()); // 프록시 조회
		PersistenceUnitUtil persistenceUnitUtil = emf.getPersistenceUnitUtil();

		// when
		Hibernate.initialize(foundReferencePlayer); // 프록시 강제 초기화

		// then
		assertThat(persistenceUnitUtil.isLoaded(foundReferencePlayer)).isTrue(); // 프록시가 초기화 된 상태
	}

}
